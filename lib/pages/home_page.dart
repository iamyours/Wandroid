import 'package:flutter/material.dart';
import '../repository/wan_repository.dart';
import '../utils/my_colors.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import '../widgets/article_item.dart';
import '../vo/article_vo.dart';
import '../utils/widget_util.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  RefreshController _controller = RefreshController(initialRefresh: true);
  List<ArticleVO> articles = [];
  int page = 0;

  void _loadData() async {
    try {
      var data = await WanRepository.articlePage(page);
      if (data.curPage == 1) {
        articles.clear();
      }
      articles.addAll(data.datas);
      setState(() {});
    } catch (e) {}
    if (page == 0)
      _controller.refreshCompleted();
    else
      _controller.loadComplete();
  }

  Widget buildBanner() {
    return Container(
      height: 160,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: MyColors.bgDark,
        body: SafeArea(
            child: SmartRefresher(
          controller: _controller,
          enablePullUp: true,
          onRefresh: () {
            page = 0;
            _loadData();
          },
          onLoading: () {
            page++;
            _loadData();
          },
          header: ClassicHeader(refreshingIcon: WidgetUtil.loading20),
          footer: ClassicFooter(
            loadingIcon: WidgetUtil.loading20,
          ),
          child: ListView.builder(
              itemCount: articles.length + 1,
              itemBuilder: (ctx, index) {
                if (index == 0) {
                  return buildBanner();
                } else {
                  return ArticleItem(item: articles[index - 1]);
                }
              }),
        )));
  }
}
