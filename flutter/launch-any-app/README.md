# Launch any App

This project is a starting point for a Flutter application that targets Android.

This app allows setting an arbitrary other app as the default launcher, even if it doesn't have built-in launcher support.

- It is declared as a launcher in [AndroidManifest.xml](android/app/src/main/AndroidManifest.xml) (`HOME` intent).
- It displays a simple dropdown menu where you can select the target App.
- The selected App is persisted after closing the App.
- On each Home Button press, the target App will be opened.
- Double click the Home Button quickly, to change the target App.

## Getting started

1. Install [Flutter](https://docs.flutter.dev/get-started/install).
2. Create a new device in Android Studio's [Virtual Device Manager](https://developer.android.com/studio/run/managing-avds).
3. Start the virtual Android device.
4. Execute `flutter run`.

## Build an APK

- Execute `flutter build apk`.

## References

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.
