QR Reader based on Android Vision API
========================

This is a small sample library that scan QR Codes using the new [Mobile Vision API for Android](https://developers.google.com/vision/) included in latest release of [Google Play Services](http://android-developers.blogspot.com.es/2015/08/barcode-detection-in-google-play.html), which avoid you to use external libraries.

Feel free to use this project as a small sample of how to use this API for scanning QR Codes and customize it however you want ;)

Integration instructions
------------------------

### Requirements

*   Android SDK version 9 or later.

### Setup


##### If you use gradle, then add the following dependency:

```
compile project(':AndroidVisionQRReader')
```

##### Or you can put the `AndroidVisionQRReader.aar` file inside libs directory and add the following dependency:


```
repositories {
    flatDir {
        dirs 'libs'
    }
}
dependencies {
    compile(name:'AndroidVisionQRReader', ext:'aar')
}
```


##### If you use something other than gradle, then:

1. Edit AndroidManifest.xml. We're going to add a few additional items in here:

    ```xml
    <uses-sdk android:minSdkVersion="9" />
    ```

2. Also in your `<manifest>` element, make sure the following permissions and features are present:

    ```xml
    <uses-permission android:name="android.permission.CAMERA" />

    <meta-data
        android:name="com.google.android.gms.vision.DEPENDENCIES"
        android:value="barcode" />
    ```

3. Within the `<application>` element, add activity entries:

    ```xml
    <!-- Activities responsible for scan QR code -->
    <activity android:name="com.gnzlt.AndroidVisionQRReader.QRActivity" />
    ```

### Sample code  (See the QRTest App for an example)

First, we'll assume that you're going to launch the request from a button,
and that you've set the button's `onClick` handler in the layout XML via `android:onClick="requestQRCodeScan"`.
Then, add the method as:

```java
public void requestQRCodeScan(View v) {
    Intent qrScanIntent = new Intent(this, QRActivity.class);
    startActivityForResult(qrScanIntent, QR_REQUEST);
}
```

Next, we'll override `onActivityResult()` to get the request result.

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST) {
            if (resultCode == RESULT_OK) {
            String qrData = data.getStringExtra(QRActivity.EXTRA_QR_RESULT);
            // do something with the QR data String
            } else {
                mResultTextView.setText("Error");
            }
        }
    }
```



### License
```
    Copyright (C) 2015 Gonzalo Toledano

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
```
