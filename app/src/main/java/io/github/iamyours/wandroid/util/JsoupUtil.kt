package io.github.iamyours.wandroid.util

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.web.*
import org.jsoup.Jsoup

object JsoupUtil {
    val WAN_ANDROID = "wanandroid.com"
    val JIAN_SHU = "https://www.jianshu.com"
    val JUE_JIN = "https://juejin.im/"
    val WEI_XIN = "https://mp.weixin.qq.com/s"
    val CSDN = "blog.csdn.net/"
    val cssMap = HashMap<String, String>()

    val script = """
        // onload是等所有的资源文件加载完毕以后再绑定事件
        window.onload = function(){
        	// 获取图片列表，即img标签列表
        	var imgs = document.querySelectorAll('img');

        	// 获取到浏览器顶部的距离
        	function getTop(e){
        		return e.offsetTop;
        	}
            var bodyWidth = document.body.offsetWidth-16;
            
            function getDomWidth(dom){
                var width = dom.getBoundingClientRect().width;
                if(width==0)return getDomWidth(dom.parentElement);
                return width;
            }
        
            for(var i=0;i<imgs.length;i++){
                var img = imgs[i];
                var width = getDomWidth(img);
                var dataset = img.dataset;
                var w = dataset.width||dataset.w;
                var h = dataset.height||dataset.h;
                var height = width * h /w;
                if(dataset.ratio){
                    height = width * dataset.ratio;
                    h = w * dataset.ratio;
                }
                var style = img.style;
                style.width = w>width?width+"px":w+"px";
                style.height = w>width?height+"px":h+"px";
                img.className="";
                img.src="data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==";
            }

        	// 懒加载实现
        	function lazyload(imgs){
        		// 可视区域高度
        		var h = window.innerHeight;
        		//滚动区域高度
        		var s = document.documentElement.scrollTop || document.body.scrollTop;
        		for(var i=0;i<imgs.length;i++){
        			//图片距离顶部的距离大于可视区域和滚动区域之和时懒加载
        			if ((h+s)>getTop(imgs[i])) {
        				// 真实情况是页面开始有2秒空白，所以使用setTimeout定时2s
        				(function(i){
        					setTimeout(function(){
        						// 不加立即执行函数i会等于9
        						// 隐形加载图片或其他资源，
        						//创建一个临时图片，这个图片在内存中不会到页面上去。实现隐形加载
        						var temp = new Image();
        						temp.src = imgs[i].getAttribute('data-src');//只会请求一次
        						// onload判断图片加载完毕，真是图片加载完毕，再赋值给dom节点
        						temp.onload = function(){
        							// 获取自定义属性data-src，用真图片替换假图片
        							imgs[i].src = imgs[i].getAttribute('data-src');
                                    imgs[i].style.backgroundImage="";
                                    imgs[i].style.background="transparent";
        						}
        					},1000)
        				})(i)
        			}
        		}
        	}
        	lazyload(imgs);
        	// 滚屏函数
        	window.onscroll =function(){
        		lazyload(imgs);
        	}
        }

    """.trimIndent()


    fun parseHtml(url: String): LiveData<String> {
        return object : LiveData<String>() {
            override fun onActive() {
                super.onActive()
                Constants.IO.execute {
                    postValue(parseArticleHtml(url))
                }
            }
        }
    }

    fun parseArticleHtml(url: String): String {
        val html = Wget.get(url)
        val doc = Jsoup.parse(html)
        var content = ""
        var css: String? = ""
        when {
            url.contains(WAN_ANDROID) -> {

            }
            url.startsWith(JIAN_SHU) -> {

            }
            url.startsWith(JUE_JIN) -> {
                val elements = doc.select("div.article-area")
                css = cssMap[JUE_JIN]
                if (css == null) {
                    css = FileUtil.readStringInAssets("juejin/css/juejin.css")
                }
                if (elements.size == 1) {
                    content = elements[0].outerHtml()
                }
            }
            url.startsWith(WEI_XIN) -> {
                val elements = doc.select("div.rich_media_inner")
                css = cssMap[JUE_JIN]
                if (css == null) {
                    css = FileUtil.readStringInAssets("weixin/weixin2.css")
                }
                if (elements.size == 1) {
                    content = elements[0].outerHtml()
                }
            }
            url.contains(CSDN) -> {

            }
        }
        return """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                    <script type="text/javascript">
                        $script
                    </script>
                    <style>
                        $css
                    </style>
                </head>
                <body>
                    $content
                </body>
            </html>
        """.trimIndent()
    }
}