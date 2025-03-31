import 'package:flutter/material.dart';
import 'package:launch_any_app/homepage/dropdown.dart';
import 'package:launch_any_app/homepage/launcher.dart';

// This widget is the home page of your application.
class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(title),
      ),
      body: const Center(
        // Center is a layout widget. It takes a single child and positions it in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            SizedBox(height: 50),
            Launcher(),
            Dropdown(),
          ],
        ),
      ),
    );
  }
}
