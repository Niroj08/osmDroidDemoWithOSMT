<<<<<<< HEAD
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
=======
[![Build Status](https://travis-ci.org/bitcoinj/bitcoinj.png?branch=master)](https://travis-ci.org/bitcoinj/bitcoinj)   [![Coverage Status](https://coveralls.io/repos/bitcoinj/bitcoinj/badge.png?branch=master)](https://coveralls.io/r/bitcoinj/bitcoinj?branch=master) 

[![Visit our IRC channel](https://kiwiirc.com/buttons/irc.freenode.net/bitcoinj.png)](https://kiwiirc.com/client/irc.freenode.net/bitcoinj)

### Welcome to bitcoinj

The bitcoinj library is a Java implementation of the Bitcoin protocol, which allows it to maintain a wallet and send/receive transactions without needing a local copy of Bitcoin Core. It comes with full documentation and some example apps showing how to use it.

### Technologies

* Java 6 for the core modules, Java 8 for everything else
* [Maven 3+](http://maven.apache.org) - for building the project
* [Google Protocol Buffers](https://github.com/google/protobuf) - for use with serialization and hardware communications

### Getting started

To get started, it is best to have the latest JDK and Maven installed. The HEAD of the `master` branch contains the latest development code and various production releases are provided on feature branches.

#### Building from the command line

To perform a full build use
```
mvn clean package
```
You can also run
```
mvn site:site
```
to generate a website with useful information like JavaDocs.

The outputs are under the `target` directory.

#### Building from an IDE

Alternatively, just import the project using your IDE. [IntelliJ](http://www.jetbrains.com/idea/download/) has Maven integration built-in and has a free Community Edition. Simply use `File | Import Project` and locate the `pom.xml` in the root of the cloned project source tree.

### Example applications

These are found in the `examples` module.

#### Forwarding service

This will download the block chain and eventually print a Bitcoin address that it has generated.

If you send coins to that address, it will forward them on to the address you specified.

```
  cd examples
  mvn exec:java -Dexec.mainClass=org.bitcoinj.examples.ForwardingService -Dexec.args="<insert a bitcoin address here>"
```

Note that this example app *does not use checkpointing*, so the initial chain sync will be pretty slow. You can make an app that starts up and does the initial sync much faster by including a checkpoints file; see the documentation for
more info on this technique.

### Where next?

Now you are ready to [follow the tutorial](https://bitcoinj.github.io/getting-started).
>>>>>>> b6360e085821398c7495027a7b7254428c7592b0
