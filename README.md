# Android Location Services

Sample app that will demonstrate the basics in retrieving a user's location and requesting permission at runtime. 

## Setup Google Play Services In Android Studio

If you open up Android Studio Settings, go to `File -> Settings -> Appearance & Behavior -> System Settings -> Android SDK`. Then click on the `SDK Tools` tab and check `Google Play services`

![](/pictures/play-services-as.png)

## Setup Google Map Fragment

Add the dependency to the application's `build.gradle` file: 

```groovy
dependencies {

    implementation 'com.google.android.gms:play-services-maps:17.0.0'

}
```

Get an API key for maps by visiting this [site](https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r=CB:9C:39:E3:D2:99:7B:8E:88:10:39:D1:C2:07:72:19:13:38:97:ED;com.plusmobileapps.mapexample&pli=1) and creating a project. Once you have the key, for debugging purposes we will add this key to a string resource file in the `debug` folder. 

```xml
 <!--  app/src/debug/res/values/google_maps_api.xml-->
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_KEY_HERE</string>
</resources>
```

Now in the `AndroidManifest.xml`, place the following inside the `application` tag. 

```xml
<application>

    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="@string/google_maps_key" />

</application>
```

Now create a [`SupportMapFragment`](https://developers.google.com/android/reference/com/google/android/gms/maps/SupportMapFragment) by creating a `fragment` tag in an xml layout file. 

```xml
<?xml version="1.0" encoding="utf-8"?>
<fragment xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:map="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/map"
    android:name="com.google.android.gms.maps.SupportMapFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MapsActivity" />
```

Now run the app, and the map should load up!

### Doing More with the `SupportMapFragment`

In your fragment or activity, use the `FragmentManager` to find the fragment. and call `SupportMapFragment.getMapAsync {}` to perform actions once the map itself is ready. 

```kotlin
 val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap: GoogleMap? ->
            googleMap ?: return@getMapAsync
            val paddingBottom =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
                    .roundToInt()
            googleMap.apply {
                setPadding(0,0,0, paddingBottom)
                // Add a marker in Sydney and move the camera
                val sydney = LatLng(-34.0, 151.0)
                addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                animateCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }
```

This sets a padding on the bottom so the Google Logo and buttons will render above the FAB. Then it creates a marker for Sydney Australia and pans to the camera to this location for the user. 

## Resources 

* [Location and Context Overview - Android Docs](https://developer.android.com/training/location) - good starting point to see what is available for Android Location API's
* [Getting started with Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/start)
* [Making Android sensors and location work for you - Google I/O 2016](https://youtu.be/OEvycEMoLUg)
* [Android Sensors & Location: What's New & Best Practices (Google I/O '17)](https://www.youtube.com/watch?v=5MIBNOTD_mM)
* [How to get one-meter location-accuracy from Android devices (Google I/O '18)](https://www.youtube.com/watch?v=vywGgSrGODU) - interesting talk, but more suited if you are attempting to create an app with indoor navigation 
* [Updating Your Apps for Location Permission Changes in Android Q (Google I/O'19)](https://www.youtube.com/watch?v=L7zwfTwrDEs)