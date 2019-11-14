import 'package:flutter/material.dart';
import 'my_colors.dart';

class WidgetUtil {
  static const loading20 = const SizedBox(
    width: 20,
    height: 20,
    child: CircularProgressIndicator(
      strokeWidth: 1.5,
      valueColor: AlwaysStoppedAnimation(MyColors.textColor),
    ),
  );
}
