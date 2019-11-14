import 'package:flutter/services.dart';
import 'package:flutter/material.dart';

class StatusUtil {
  static void setColor(bool dark, Color color) {
    var style = dark
        ? SystemUiOverlayStyle.dark.copyWith(statusBarColor: color)
        : SystemUiOverlayStyle.light.copyWith(statusBarColor: color);
    SystemChrome.setSystemUIOverlayStyle(style);
  }
}
