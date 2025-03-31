import 'package:flutter/material.dart';

import 'dropdown.dart';

class Launcher extends StatefulWidget {
  const Launcher({super.key});

  @override
  State<Launcher> createState() => _LauncherState();
}

class _LauncherState extends State<Launcher> with WidgetsBindingObserver {
  int _lastTap = 0;
  final int _threshold = 500;

  @override
  void initState() {
    WidgetsBinding.instance.addObserver(this);
    super.initState();
    print("INIT STATE");
    startSelectedApp();
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    print('state = $state');
    if (state == AppLifecycleState.resumed) {
      if (now() - _lastTap > _threshold) {
        startSelectedApp();
      }
      _lastTap = now();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}

int now() {
  return DateTime.now().millisecondsSinceEpoch;
}
