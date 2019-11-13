// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'article_vo.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ArticleVO _$ArticleVOFromJson(Map<String, dynamic> json) {
  return $checkedNew('ArticleVO', json, () {
    final val = ArticleVO();
    $checkedConvert(json, 'id', (v) => val.id = v as int);
    $checkedConvert(json, 'author', (v) => val.author = v as String);
    $checkedConvert(json, 'chapterId', (v) => val.chapterId = v as int);
    $checkedConvert(json, 'chapterName', (v) => val.chapterName = v as String);
    $checkedConvert(json, 'collect', (v) => val.collect = v as bool);
    $checkedConvert(json, 'fresh', (v) => val.fresh = v as bool);
    $checkedConvert(json, 'courseId', (v) => val.courseId = v as int);
    $checkedConvert(json, 'desc', (v) => val.desc = v as String);
    $checkedConvert(json, 'link', (v) => val.link = v as String);
    $checkedConvert(json, 'niceDate', (v) => val.niceDate = v as String);
    $checkedConvert(json, 'publishTime', (v) => val.publishTime = v as int);
    $checkedConvert(
        json, 'superChapterId', (v) => val.superChapterId = v as int);
    $checkedConvert(
        json, 'superChapterName', (v) => val.superChapterName = v as String);
    $checkedConvert(json, 'title', (v) => val.title = v as String);
    $checkedConvert(json, 'envelopePic', (v) => val.envelopePic = v as String);
    $checkedConvert(json, 'read', (v) => val.read = v as bool);
    $checkedConvert(json, 'originId', (v) => val.originId = v as int);
    return val;
  });
}

Map<String, dynamic> _$ArticleVOToJson(ArticleVO instance) => <String, dynamic>{
      'id': instance.id,
      'author': instance.author,
      'chapterId': instance.chapterId,
      'chapterName': instance.chapterName,
      'collect': instance.collect,
      'fresh': instance.fresh,
      'courseId': instance.courseId,
      'desc': instance.desc,
      'link': instance.link,
      'niceDate': instance.niceDate,
      'publishTime': instance.publishTime,
      'superChapterId': instance.superChapterId,
      'superChapterName': instance.superChapterName,
      'title': instance.title,
      'envelopePic': instance.envelopePic,
      'read': instance.read,
      'originId': instance.originId
    };
