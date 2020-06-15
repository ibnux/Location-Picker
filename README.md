# Android Maps/Location Picker

Android library to select or search location and return coordinate of the selected place

this Library use WebView, so it doesn't require Maps API.

#DEPEDENCIES

[![](https://jitpack.io/v/ibnux/Location-Picker.svg)](https://jitpack.io/#ibnux/Location-Picker)

Add the JitPack repository to your build file, Add it in your root build.gradle at the end of repositories

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency

```
dependencies {
	implementation 'com.github.ibnux:Location-Picker:1.0'
}
```

# HOW TO USE

call it in your code

```
startActivityForResult(new Intent(this, MapsPickerActivity.class), 4268);

```

get the result

```
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK) {
        if (requestCode == 4268){
            String latitude = data.getStringExtra("lat");
            String longitude = data.getStringExtra("lon");
            //to double
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
        }
    }

}
```

### Created by ibnux
