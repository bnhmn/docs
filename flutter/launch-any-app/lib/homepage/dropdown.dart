import 'package:collection/collection.dart';
import 'package:device_apps/device_apps.dart';
import 'package:flutter/material.dart';
import 'package:launch_any_app/layout/padding.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'apps.dart';

class Dropdown extends StatefulWidget {
  const Dropdown({super.key});

  @override
  State<Dropdown> createState() => _DropdownState();
}

class _DropdownState extends State<Dropdown> {
  final TextEditingController appController = TextEditingController();
  Application? selectedApp;

  @override
  // This method is rerun every time setState is called, for instance as done
  // by the _incrementCounter method above.
  Widget build(BuildContext context) {
    return FutureBuilder<DropdownInfo>(
        future: buildDropdownEntries(),
        builder: (context, AsyncSnapshot<DropdownInfo> snapshot) {
          if (snapshot.hasData) {
            var dropdownInfo = snapshot.data!;
            return Column(
              children: [
                padded(const Text("Select the Application to be used as Launcher")),
                DropdownMenu<Application>(
                  width: MediaQuery.of(context).size.width * 0.8,
                  menuHeight: MediaQuery.of(context).size.height * 0.5,
                  label: const Text('App'),
                  dropdownMenuEntries: dropdownInfo.entries,
                  initialSelection: dropdownInfo.selectedApp,
                  controller: appController,
                  onSelected: (Application? app) {
                    setState(() {
                      if (app != null) {
                        selectedApp = app;
                        saveSelectedApp(app);
                      }
                    });
                  },
                )
              ],
            );
          } else {
            return const CircularProgressIndicator();
          }
        });
  }
}

class DropdownInfo {
  final List<DropdownMenuEntry<Application>> entries;
  final Application? selectedApp;

  const DropdownInfo({
    required this.entries,
    required this.selectedApp,
  });
}

Future<DropdownInfo> buildDropdownEntries() async {
  final apps = await getDeviceApps();
  final entries =
      apps.map((app) => DropdownMenuEntry(value: app, label: app.appName, leadingIcon: getAppIcon(app))).toList();

  final selectedAppName = await getSelectedApp();
  final selectedApp = apps.firstWhereOrNull((app) => app.packageName == selectedAppName);

  return DropdownInfo(entries: entries, selectedApp: selectedApp);
}

Future<void> saveSelectedApp(Application app) async {
  final prefs = await SharedPreferences.getInstance();

  await prefs.setString("selectedAppId", app.packageName);
}

Future<String?> getSelectedApp() async {
  final prefs = await SharedPreferences.getInstance();

  return prefs.getString("selectedAppId");
}

Future<bool> startSelectedApp() async {
  var appId = await getSelectedApp();
  if (appId != null) {
    return await startApp(appId);
  }
  return false;
}
