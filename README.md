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
	implementation 'com.github.ibnux:Location-Picker:1.7'
}
```

# HOW TO USE

## Location picker

call it in your code

```
startActivityForResult(new Intent(this, MapsPickerActivity.class), 4268);

```

**RESULT**

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

## Get Direction

Single destination

```
DirectionActivity.goTo("-6.3763443", "106.7190438", this);

```
with from coordinate

```
DirectionActivity.goTo("-6.3740574", "106.6355415", "-6.3763443", "106.7190438", this);

```

Multiple Destination

```
List<String> location = new ArrayList<>();
location.add("-6.3763443,106.7190438");
location.add("-6.3740574,106.6355415");
location.add("-6.3455664,106.7641159");
DirectionActivity.goTo(location, this);
```

Multiple Destination with from coordinate


```
List<String> location = new ArrayList<>();
location.add("-6.3763443,106.7190438");
location.add("-6.3740574,106.6355415");
location.add("-6.3455664,106.7641159");
DirectionActivity.goTo("-6.3740574", "106.6355415", location, this);
```


## Get Distance
Single destination

```
DirectionActivity.disTanceOf("-6.3763443", "106.7190438", 8264, this);

```
with from coordinate

```
DirectionActivity.disTanceOf("-6.3740574", "106.6355415", "-6.3763443", "106.7190438", 8264, this);

```


Multiple Destination

```
List<String> location = new ArrayList<>();
location.add("-6.3763443,106.7190438");
location.add("-6.3740574,106.6355415");
location.add("-6.3455664,106.7641159");
DirectionActivity.disTanceOf(location, 8264, this);
```

Multiple Destination with from coordinate, max 2 destination

```
List<String> location = new ArrayList<>();
location.add("-6.3763443,106.7190438");
location.add("-6.3740574,106.6355415");
DirectionActivity.disTanceOf("-6.3455664", "106.7641159", 8264, location, this);
```

**RESULT**

```
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK) {
        if (requestCode == 8264){
            String meter = data.getStringExtra("meters");
            String times = data.getStringExtra("times");
        }
    }

}
```

### Created by ibnux
