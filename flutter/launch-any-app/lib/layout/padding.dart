import 'package:flutter/material.dart';

Widget padded(Widget child) {
  return Container(
    margin: const EdgeInsets.only(bottom: 20.0), // Add spacing between Child 1 and Child 2
    child: child,
  );
}
