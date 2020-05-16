import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class IconPacks {
  static const MethodChannel _channel =
      const MethodChannel('iconpacks');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<List> get iconPacks async{
    String packs = await _channel.invokeMethod("getIconPacks");
    packs = packs.replaceAll("[", "").replaceAll("]", "").replaceAll(" ", "");
    return packs.split(",");
  }

  static Future<Image> getIcon(String packageName, String iconPackPackage, {double width = 50, double height = 50}) async{
    var args = {'application': packageName, 'icon_pack': iconPackPackage};
    var val = await _channel.invokeMethod("getIcon", args);
    if (val is String) return null;
    Uint8List list = val;
    return Image.memory(list, width: width, height: height, fit: BoxFit.contain,);
  }


}
