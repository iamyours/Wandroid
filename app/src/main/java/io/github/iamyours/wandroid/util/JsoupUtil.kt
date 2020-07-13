package io.github.iamyours.wandroid.util

import androidx.lifecycle.LiveData
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
        	var imgs = document.getElementsByTagName('img');

        	// 获取到浏览器顶部的距离
        	function getTop(e){
        		return e.offsetTop;
        	}
            var bodyWidth = document.body.offsetWidth-16;
            
            function getDomWidth(dom){
                var width = dom.clientWidth;
                var parent = dom.parentElement;
                var parentTag = parent.tagName;
                if(parentTag=="FIGURE"){//掘金
                    return parent.clientWidth;
                }
                if(parent.className=="image-view"){//简书
                    return parent.clientWidth;
                }
                if(width==0)return getDomWidth(dom.parentElement);
                return width;
            }
            
            function setImgStyle(img){
                var width = getDomWidth(img);
                var dataset = img.dataset;
                var w = dataset.width||dataset.w||dataset.originalWidth;
                var h = dataset.height||dataset.h||dataset.originalHeight;
                var height = width * h /w;
                if(dataset.ratio){
                    height = width * dataset.ratio;
                    h = w * dataset.ratio;
                }
                img.className="";
                if(dataset&&w){
                    var style = img.style;
                    img.style.width = w>width?width+"px":w+"px";
                    img.style.height = w>width?height+"px":h+"px";
                    img.src="data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==";
                }
            }
        
            for(var i=0;i<imgs.length;i++){
                var img = imgs[i];
                (function(img){
                    setImgStyle(img);
                })(img);
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
                                var dataset = imgs[i].dataset;
        						// onload判断图片加载完毕，真是图片加载完毕，再赋值给dom节点
                                var src = dataset.src||dataset.originalSrc;
                                if(src){
                                    var temp = new Image();
                                    temp.src = src;//只会请求一次
                                    temp.onload = function(){
                                        // 获取自定义属性data-src，用真图片替换假图片
                                        imgs[i].src = src;
                                        imgs[i].style.backgroundImage="none";
                                        imgs[i].style.background="transparent";
                                    }
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

    val siteKeys = arrayOf(
        WAN_ANDROID, JIAN_SHU, JUE_JIN, WEI_XIN, CSDN
    )

    fun parseArticleHtml(url: String): String {
        val html = Wget.get(url)
        val doc = Jsoup.parse(html)
        var content = ""
        var css: String? = ""
        var customScript = ""
        var customJs = ""
        var selector = ""
        var cssPath = ""
        var siteKey = ""
        var customBodyClass = ""
        var titleSelector = ""
        var title = ""
        when {
            url.contains(WAN_ANDROID) -> {
                selector = "div#blogDetail"
                siteKey = WAN_ANDROID
                titleSelector = "h1.blog_title"
                cssPath = "web2/wanandroid.css"
                customScript = "hljs.initHighlightingOnLoad();"
                customJs = """
                        <script type="text/javascript" src="/resources/js/highlight.pack.js"></script>
                    """.trimIndent()
            }
            url.startsWith(JIAN_SHU) -> {
                selector = "div.main-view"
                titleSelector = "h1.title"
                cssPath = "web2/dark/jianshu.css"
                customBodyClass = "reader-night-mode"
                customJs = """
                        <script type="text/javascript" src="/resources/js/highlight.pack.js"></script>
                    """.trimIndent()
                customScript = "hljs.initHighlightingOnLoad();"
            }
            url.startsWith(JUE_JIN) -> {
                titleSelector = "h1.article-title"
                selector = "div.article-area"
                cssPath = "web2/juejin.css"
            }
            url.startsWith(WEI_XIN) -> {
                selector = "div.rich_media_inner"
                cssPath = "weixin/weixin2.css"
                titleSelector = ".rich_media_title"
            }
            url.contains(CSDN) -> {
                selector="#article"
                titleSelector="h1.article_title"
                cssPath="web2/dark/csdn.css"
            }
        }

        val elements = doc.select(selector)
        val titleElements = doc.select(titleSelector)
        css = cssMap[siteKey]
        if (css == null) {
            css = FileUtil.readStringInAssets(cssPath)
        }
        if (elements.size == 1) {
            val ele = elements[0]
            ele.select("script").remove()
            ele.select("link").remove()
            content = elements[0].outerHtml()
        }
        if(titleElements.size==1){
            title = titleElements[0].text()
        }
        return """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                    <title>$title</title>
                    $customJs
                    <script type="text/javascript">
                        $script
                        $customScript
                    </script>
                    <style>
                        $css
                    </style>
                </head>
                <body class="$customBodyClass">
                    $content
                </body>
            </html>
        """.trimIndent()
    }
}