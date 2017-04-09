package com.technotroop.android_osmdroiddemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.mtp.OSMMapTilePackager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;

    private String fileAppendix = "";
    // url from where map will be downloaded
    private String url = "http://b.tile.openstreetmap.org/%d/%d/%d.png";
    // sotrage location
    private String destinationFilePath = "/data/data/com.technotroop.android_osmdroiddemo/osmdroid/Mapnik";

    private int zoomMin = 8;
    private int zoomMax = 20;
    private int threadCount = 2;

    private Double north;
    private Double south;
    private Double east;
    private Double west;

    private OSMMapTilePackager.ProgressNotification progressNotification;

    private MapView mapView;

    private Button btnCancel;
    private Button btnConfirm;
    private Button btnDownload;

    EditText editNorth;
    EditText editSouth;
    EditText editWest;
    EditText editEast;

    ProgressBar progressBar;

    RelativeLayout containerGetDetails;

    private GeoPoint startPoint;
    private IMapController mapController;

    private CompassOverlay compassOverlay;
    private MyLocationNewOverlay locationOverlay;
    private RotationGestureOverlay rotationGestureOverlay;

    private Context context;

    private Location userLocation;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        // Load default config for the mapView
        // Important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnDownload = (Button) findViewById(R.id.btnDownload);

        editNorth = (EditText) findViewById(R.id.editNorth);
        editSouth = (EditText) findViewById(R.id.editSouth);
        editWest = (EditText) findViewById(R.id.editWest);
        editEast = (EditText) findViewById(R.id.editEast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        containerGetDetails = (RelativeLayout) findViewById(R.id.containerGetDetails);

        // Get permission for external storage
        int permissionCheckExternalStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Get permission for coarse location
        int permissionCheckAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // Get permission for fine location
        int permissionCheckAccessFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        // permission are needed in devices running Marshmallow and above.
        // Only dangerous permission needs to be checked during runtime. More on developers.google.com

        if (permissionCheckExternalStorage != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }

        if (permissionCheckAccessCoarseLocation != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        }

        if (permissionCheckAccessFineLocation != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        // Set the map UI
        setUpMapView();

        // Set onClick Listeners for the Download button
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show the UI to get the details of map to download which is hidden by default and hide the clicked button
                if (containerGetDetails.getVisibility() == View.GONE) {

                    containerGetDetails.setVisibility(View.VISIBLE);
                    btnDownload.setVisibility(View.GONE);
                }
            }
        });

        // Set onClick Listener for the Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show Download button and close the get details UI
                if (btnDownload.getVisibility() == View.GONE) {

                    btnDownload.setVisibility(View.VISIBLE);
                    containerGetDetails.setVisibility(View.GONE);
                }

            }
        });

        // Set onClick Listener for the Confirm button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editNorth.getText().toString())) {

                    editNorth.setError("Value cannot be empty.");
                    return;

                } else if (TextUtils.isEmpty(editWest.getText().toString())) {

                    editWest.setError("Value cannot be empty.");
                    return;

                } else if (TextUtils.isEmpty(editSouth.getText().toString())) {

                    editSouth.setError("Value cannot be empty.");
                    return;

                } else if (TextUtils.isEmpty(editEast.getText().toString())) {

                    editEast.setError("Value cannot be empty.");
                    return;
                }

                // Get the values from edit text
                north = Double.valueOf(editNorth.getText().toString());
                west = Double.valueOf(editWest.getText().toString());
                south = Double.valueOf(editSouth.getText().toString());
                east = Double.valueOf(editEast.getText().toString());

                if (north > 90 || north < -90) {

                    editNorth.setError("Value must range -90 to 90");
                    return;

                } else if (west > 180 || west < -180) {

                    editWest.setError("Value must range -180 to 180");
                    return;

                } else if (south > 90 || south < -90) {

                    editSouth.setError("Value must range -90 to 90");
                    return;

                } else if (east > 180 || east < -180) {

                    editEast.setError("Value must range -180 to 180");
                    return;
                }

                // Set the destination folder
                String destinationFileName = destinationFilePath + ".zip";

                // Set the temporary destination folder
                String tempFolder = destinationFilePath;

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Disable the buttons
                btnConfirm.setEnabled(false);
                btnCancel.setEnabled(false);

                // Disable the edit fields
                editNorth.setEnabled(false);
                editWest.setEnabled(false);
                editSouth.setEnabled(false);
                editEast.setEnabled(false);

                // Download MAPNIK map source with OSMMapTilePckager
                OSMMapTilePackager.execute(url, destinationFileName, tempFolder, threadCount, fileAppendix, zoomMin, zoomMax, north, south, east, west, progressNotification);
            }
        });

        // Show the progress status of the downloaded map
        progressNotification = new OSMMapTilePackager.ProgressNotification() {
            @Override
            public void updateProgress(String s) {

                if (s.equalsIgnoreCase("Arching complete, deleting temp files")) {

                    progressBar.setVisibility(View.GONE);

                    // Enable the buttons
                    btnConfirm.setEnabled(true);
                    btnCancel.setEnabled(true);

                    // Enable the edit fields
                    editNorth.setEnabled(true);
                    editWest.setEnabled(true);
                    editSouth.setEnabled(true);
                    editEast.setEnabled(true);

                    // clear the edit fields
                    editNorth.setText("");
                    editWest.setText("");
                    editSouth.setText("");
                    editEast.setText("");

                    north = null;
                    west = null;
                    south = null;
                    east = null;

                } else {

                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void setUpMapView() {

        // Set tile source to MAPNIK. For more map sources visit https://github.com/osmdroid/osmdroid/wiki/Map-Sources
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Set zoom feature
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set min and max zoom level
        mapView.setMinZoomLevel(0);
        mapView.setMaxZoomLevel(22);


        mapController = mapView.getController();
        mapController.setZoom(16);

        // Set marker of selected user location
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapView);

        Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder);
        locationOverlay.setPersonIcon(placeholder);

        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationOverlay);

        // Get the location of the user
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // user location change listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                userLocation = location; // set the userLocation to newly changed location

                if (userLocation != null) {

                    startPoint = new GeoPoint(userLocation);
                    mapController.setCenter(startPoint);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 1000, locationListener);

        // Set compass in UI
        compassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // Enable rotation on multitouch
        rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(this.rotationGestureOverlay);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}
