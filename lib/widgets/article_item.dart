import 'package:flutter/material.dart';
import '../vo/article_vo.dart';
import '../utils/image_helper.dart';
import '../utils/my_colors.dart';

class ArticleItem extends StatefulWidget {
  final ArticleVO item;

  const ArticleItem({Key key, this.item}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _ArticleItemState(item);
  }
}

class _ArticleItemState extends State<ArticleItem> {
  final ArticleVO item;

  _ArticleItemState(this.item);

  Widget buildTag(String name, int id) {
    return Container(
      height: 30,
      padding: EdgeInsets.only(left: 8, right: 8),
      margin: EdgeInsets.only(left: 10),
      decoration: BoxDecoration(
        color: MyColors.dividerColor,
        borderRadius: BorderRadius.circular(4)
      ),
      child: Center(
        child: Text(
          name,
          style: const TextStyle(fontSize: 12, color: MyColors.textColor),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Stack(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.only(left: 10, top: 7),
                child: ImageHelper.icon(item.getIcon(), width: 14),
              ),
              Padding(
                padding: EdgeInsets.only(left: 10),
                child: Text(
                  "     ${item.title}",
                  style: TextStyle(fontSize: 16, color: item.read ? MyColors.readColor : MyColors.unReadColor),
                ),
              )
            ],
          ),
          Padding(
            padding: EdgeInsets.only(top: 5),
            child: Row(
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.only(left: 10),
                  child: Text(
                    item.getUser(),
                    style: const TextStyle(fontSize: 14, color: MyColors.textColor),
                  ),
                ),
                buildTag(item.chapterName, item.chapterId),
                buildTag(item.superChapterName, item.superChapterId),
              ],
            ),
          ),
          Divider(
            thickness: 0.5,
            color: MyColors.dividerColor,
          )
        ],
      ),
    );
  }
}
