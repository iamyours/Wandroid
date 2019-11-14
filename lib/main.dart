import 'package:flutter/material.dart';
import 'utils/image_helper.dart';
import 'utils/my_colors.dart';
import 'pages/home_page.dart';
import 'utils/status_util.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primaryColor: MyColors.textColor
      ),
      home: _Main(),
    );
  }
}

class _Main extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _MainState();
  }
}

List<Widget> pages = [HomePage()];

class _MainState extends State<_Main> {
  int _index = 0;
  PageController _controller = PageController();

  BottomNavigationBarItem buildTab(String name, String normalIcon, String selectedIcon) {
    return BottomNavigationBarItem(
        icon: ImageHelper.icon(normalIcon, width: 24),
        activeIcon: ImageHelper.icon(selectedIcon, width: 24),
        title: Text(name));
  }

  @override
  Widget build(BuildContext context) {
    StatusUtil.setColor(false, MyColors.bgDark);
    return Scaffold(
      backgroundColor: MyColors.bgDark,
      body: PageView.builder(controller: _controller, itemBuilder: (ctx, index) => pages[index]),
      bottomNavigationBar: BottomNavigationBar(
          unselectedItemColor: MyColors.tabUnSelected,
          selectedItemColor: MyColors.tabSelected,
          selectedFontSize: 14,
          backgroundColor: MyColors.bgDark,
          unselectedFontSize: 14,
          type: BottomNavigationBarType.fixed,
          currentIndex: _index,
          items: [
            buildTab("首页", "ic_tab_home_n", "ic_tab_home_s"),
            buildTab("项目", "ic_tab_projects_n", "ic_projects_home_s"),
            buildTab("公众号", "ic_tab_wx_n", "ic_tab_wx_s"),
            buildTab("我的", "ic_tab_mine_n", "ic_tab_mine_s")
          ]),
    );
  }
}
