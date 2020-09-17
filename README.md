# App Manager

# \[ 🚧 Work in progress 👷‍♀️⛏👷🔧️👷🔧 🚧 \] 
An android app manager with MVVM architecture. 

## App Link

https://play.google.com/store/apps/details?id=com.vau.myappmanager

## Library
Jetpack
(Room, Lifecycle, LiveData, Viewmodel, Data Binding...)

Glide,

Coroutine,

Flow , etc...

### Gradle build
target sdk 28
minSdkversion 21

### Do not run this on version 29
Environment.getExternalStorageDir() is deprecated, would not create any file on your external storage.

# Structure Explain
DataSource -> Repostitory -> viewmodel -> view

### Data Layout  

DataSource & Repository (Room database, SharePreference, local file, Flow stream)
### Logic Layout 

ViewModel
(Get Data, filter list, livedata, etc)

### View 

ViewPager2, Data Binding


