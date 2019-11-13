import 'package:dio/dio.dart';
import '../utils/constants.dart';

/*
 * 网络管理
 */
class HttpManager {
  static HttpManager _instance;

  static HttpManager getInstance() {
    if (_instance == null) {
      _instance = HttpManager();
    }
    return _instance;
  }

  Dio dio = Dio();

  HttpManager() {
    dio.options.baseUrl = "https://www.wanandroid.com/";
    dio.options.connectTimeout = 10000;
    dio.options.receiveTimeout = 5000;
    dio.interceptors.add(LogInterceptor(responseBody: Constants.isDebug));
  }

  static Future<Map<String, dynamic>> get(String path, Map<String, dynamic> map) async {
    var response = await getInstance().dio.get(path, queryParameters: map);
    return processResponse(response);
  }

  static Future<Map<String, dynamic>> post(String path, Map<String, dynamic> map) async {
    var response = await getInstance().dio.post(path, queryParameters: map);
    return processResponse(response);
  }

  /*
    表单形式
   */
  static Future<Map<String, dynamic>> post2(String path, Map<String, dynamic> map) async {
    var response = await getInstance().dio.post(path,
        data: map,
        options: Options(
            contentType: "application/x-www-form-urlencoded",
            headers: {"Content-Type": "application/x-www-form-urlencoded"}));
    return processResponse(response);
  }

  static Future<Map<String, dynamic>> processResponse(Response response) async {
    if (response.statusCode == 200) {
      var data = response.data;
      var code = data["errorCode"] as int;
      var msg = data["errorMsg"] as String;
      if (code == 0) {
        return data;
      }
      throw Exception(msg);
    }
    throw Exception("server error");
  }
}
