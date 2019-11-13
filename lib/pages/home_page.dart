import 'package:flutter/material.dart';
import '../repository/wan_repository.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  @override
  void initState() {
    super.initState();
    _loadData();
  }

  void _loadData() async {
    var page = await WanRepository.articlePage(1);
    print("page:$page");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: GestureDetector(
          onTap: _loadData,
          child: Container(
            margin: EdgeInsets.only(top:44),
            height: 44,
            color: Colors.lightBlue,
          ),
        ),
      ),
    );
  }
}
