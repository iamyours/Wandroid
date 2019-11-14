import 'package:json_annotation/json_annotation.dart';

part 'article_vo.g.dart';

@JsonSerializable(checked: true)
class ArticleVO {
  int id;
  String author;
  int chapterId;
  String chapterName;
  bool collect;
  bool fresh;
  int courseId;
  String desc;
  String link;
  String niceDate;
  int publishTime;
  int superChapterId;
  String superChapterName;
  String title;
  String shareUser;
  String envelopePic;

  bool read = false;
  int originId;

  ArticleVO();

  factory ArticleVO.fromJson(Map<dynamic, dynamic> map) => _$ArticleVOFromJson(map);

  String getIcon() {
    if (link.contains("wanandroid.com")) {
      return "ic_logo_wan";
    } else if (link.contains("jianshu.com")) {
      return "ic_logo_jianshu";
    } else if (link.contains("juejin.im")) {
      return "ic_logo_juejin";
    } else if (link.contains("blog.csdn.net")) {
      return "ic_logo_csdn";
    } else if (link.contains("weixin.qq.com")) {
      return "ic_logo_wxi";
    } else {
      return "ic_logo_other";
    }
  }

  String getUser() {
    if (author.isEmpty)
      return "分享:$shareUser";
    else
      return author;
  }
}
