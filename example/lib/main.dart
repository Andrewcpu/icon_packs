import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:iconpacks/iconpacks.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String iconPacks = "";
  Widget spotifyIcon = Container();
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await IconPacks.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    List iconPacks = await IconPacks.iconPacks;
    this.iconPacks = iconPacks.toString();
    spotifyIcon = await IconPacks.getIcon("com.spotify.music", iconPacks[0]);

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> content = [
      Text('Running on: $_platformVersion\n'),
    ];
    content.add(Text(iconPacks));
    content.add(spotifyIcon);
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin: Icon_Packs'),
        ),
        body: Center(
          child: Column(
            children: content
          ),
        ),
      ),
    );
  }
}
