# Leo App

A little info about your project and/ or overview that explains **what** the project is about.

## Installation

1. Download the Zip from the website or clone from Github Desktop.
2. Unzip the github project to a folder. (If you have downloaded it)
3. Open Android Studio. Go to File -> New -> Import Project. Then choose the Leo_Kotlin folder and then click Next->Finish.
4. It will build the Gradle automatically and'll be ready for you to use. (Make sure you are connected to internet)
5.  In some versions of Android Studio a certain error occurs-
error:package android.support.v4.app does not exist.
To fix it go to Gradle Scripts->build.gradle(Module:app) and the add the dependecies:
```
dependencies {      
    compile fileTree(dir: 'libs', include: ['*.jar'])  
    implementation 'androidx.appcompat:appcompat:1.1.0' 
}
```

## Screenshots

Include logo/demo screenshot etc.

## Features

- Feature1
  - First point explaining it
  - Second point explaining it
- Feature2
  - First point explaining it
  - Second point explaining it
    and so on

## API Documentation

Not available

## Directory structure

Use `tree -L 2` (increase the level if your project goes deeper) and paste it's output and try to explain your logic of how you modularised the project

Example

```
src/   --> Source files
docs/  --> Documents
bin/   --> Binaries and executables
```

## Deployment

1. Go to Build -> Generate Signed APK
2. Choose APK -> Next
3. If you already have keystore use that otherwise Click on create new. fill in the form with the required details.
Click on the icon on the right with the 3 dots ("..."), which will open up a navigator window asking you to navigate and select a .jks file.Navigate to a folder where you want your keystore file saved.
4. Create a key for your application and fill in all the required details.
5. Click on Next and then select Release.
6. You will get popup indicating the apk file path when android studio finishes build.

## Built With

- [Jetpack Navigation](https://developer.android.com/guide/navigation) - Helps in implementing navigation across different fragments.
- [Android lifecycle extension](https://developer.android.com/jetpack/androidx/releases/lifecycle) -  These components help you produce better-organized, and often lighter-weight code, that is easier to maintain.
- [Retrofit](https://square.github.io/retrofit/) - Retrofit is the class through which your API interfaces are turned into callable objects. Used to make network requests in android.
- [Moshi](https://github.com/square/moshi) - Modern JSON library for Android and Java
- [Lottie](https://github.com/airbnb/lottie-android) -  Mobile library for Android and iOS that parses Adobe After Effects animations
- [Glide](https://github.com/bumptech/glide) - Fast and efficient open source media management and image loading framework
- [Room Persistence](https://developer.android.com/topic/libraries/architecture/room) - Provides an abstraction layer over SQLite.

## Contributing

To be filled later

## Authors

- [Mayank Jain](https://github.com/mayank-02)
- [Rohit Chaudhari](https://github.com/chaudharirohit2810)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

- Hat tip to anyone whose code was used
- Inspiration
- etc
