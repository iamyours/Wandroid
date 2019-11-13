import '../http/http_manager.dart';
import 'dart:async';
import '../vo/page_vo.dart';
import '../vo/article_vo.dart';

class WanRepository {
  static Future<PageVO<ArticleVO>> articlePage(int page) async {
    var res = await HttpManager.get("article/list/$page/json", {});
    var data = res["data"];
    var list = data["datas"].map((item) => ArticleVO.fromJson(item));
    return PageVO(data["curPage"], list, data["offset"], data["over"], data["pageCount"], data["size"], data["total"]);
  }
}
