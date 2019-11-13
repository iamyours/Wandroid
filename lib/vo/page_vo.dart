import 'package:json_annotation/json_annotation.dart';

@JsonSerializable()
class PageVO<T>{
  final int curPage;
  final List<T> datas;
  final int offset;
  final bool over;
  final int pageCount;
  final int size;
  final int total;

  PageVO(this.curPage, this.datas, this.offset, this.over, this.pageCount, this.size, this.total);

}