# android-osmDroidDemo

This demo uses osmDroid(https://github.com/osmdroid/osmdroid) 

&

OSMMapTilePackager(https://github.com/osmdroid/osmdroid/tree/master/OSMMapTilePackager).

#android #osmDroidDemo #android-osmDroidDemo

OSMMapTilePackager.jar is include in the libs folder for easy usability. The map is downloaded from the android device itself.

Also for the devices with Marshmallow and above; the dangerous permission checks are included


Try with values from https://github.com/osmdroid/osmdroid/tree/master/OSMMapTilePackager
Indeed the file size will be large 
you can also input your own values for north,  west, south, east.

Feel free to look at the project and make changes accordingly if you think there should be some fix and send a pull request.

This is the demo project while i learned osmDroid. Will be working with navigation in next updates, if I got time.

The View Offline button logic works perfectly for stored maps. but is not displaying since have to set the geopoint

Add desired lat and long
mapView.setCenter(new GeoPoint(lat, long))

Code was just for a demo purpose, sorry of it seems rough
