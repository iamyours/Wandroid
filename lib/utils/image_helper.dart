import 'package:flutter/material.dart';

class ImageHelper {
  static String png(String name) {
    return "assets/images/$name.png";
  }

  static Widget icon(String name, {double width, double height, BoxFit boxFit}) {
    return Image.asset(
      png(name),
      width: width,
      height: height,
      fit: boxFit,
    );
  }
}
