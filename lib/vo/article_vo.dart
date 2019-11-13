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

  String envelopePic;

  bool read;
  int originId;

  ArticleVO();

  factory ArticleVO.fromJson(Map<String, dynamic> map) => _$ArticleVOFromJson(map);
}
