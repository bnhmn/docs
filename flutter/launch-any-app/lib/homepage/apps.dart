import 'package:device_apps/device_apps.dart';
import 'package:flutter/material.dart';

Future<List<Application>> getDeviceApps() async {
  List<Application> apps = await DeviceApps.getInstalledApplications(
    includeAppIcons: true,
    includeSystemApps: true,
    onlyAppsWithLaunchIntent: true,
  );
  return apps;
}

Widget? getAppIcon(Application app) {
  if (app is ApplicationWithIcon) {
    return Image.memory(app.icon, width: 32, height: 32);
  }
  return null;
}

Future<bool> startApp(String appId) async {
  print("Starting App $appId");
  return DeviceApps.openApp(appId);
}
