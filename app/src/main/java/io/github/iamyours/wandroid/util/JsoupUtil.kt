package io.github.iamyours.wandroid.util

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.extension.logE
import org.jsoup.Jsoup

object JsoupUtil {
    val WAN_ANDROID = "wanandroid.com"
    val JIAN_SHU = "https://www.jianshu.com"
    val JUE_JIN = "https://juejin.im/"
    val WEI_XIN = "https://mp.weixin.qq.com/s"
    val CSDN = "blog.csdn.net/"
    val cssMap = HashMap<String, String>()
    val dom2imageJs = """
        <script type="text/javascript" src="/resources/js/dom-to-image.js"></script>
    """.trimIndent()
    val ajax = """
        function ajax(options) {
        options = options || {};
        options.type = (options.type || "GET").toUpperCase();
        options.dataType = options.dataType || "json";
        var params = formatParams(options.data);

        //创建 - 非IE6 - 第一步
        if (window.XMLHttpRequest) {
            var xhr = new XMLHttpRequest();
        } else { //IE6及其以下版本浏览器
            var xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }

        //接收 - 第三步
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                var status = xhr.status;
                if (status >= 200 && status < 300) {
                    options.success && options.success(xhr.responseText, xhr.responseXML);
                } else {
                    options.fail && options.fail(status);
                }
            }
        }

        //连接 和 发送 - 第二步
        if (options.type == "GET") {
            xhr.open("GET", options.url + "?" + params, true);
            xhr.send(null);
        } else if (options.type == "POST") {
            xhr.open("POST", options.url, true);
            //设置表单提交时的内容类型
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send(params);
        }
    }
    //格式化参数
    function formatParams(data) {
        var arr = [];
        for (var name in data) {
            arr.push(encodeURIComponent(name) + "=" + encodeURIComponent(data[name]));
        }
        arr.push(("v=" + Math.random()).replace(".",""));
        return arr.join("&");
    }
    """.trimIndent()

    val script = """
        // onload是等所有的资源文件加载完毕以后再绑定事件
        function addLoadEvent(func){ 
            var oldonload = window.onload;  
            if(typeof window.onload !='function'){  
                window.onload = func;  
            }else{  
                window.onload = function(){  
                    oldonload();  
                    func();  
                }  
            }  
        }
        addLoadEvent(function(){
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
                var parentWidth = parent.clientWidth;
                if(parentTag=="FIGURE"){//掘金
                    return parentWidth;
                }
                if(parent.className=="image-view"){//简书
                    return parentWidth;
                }
                if(width==0)return getDomWidth(dom.parentElement);
                return width;
            }
            
            function setDefaultSrc(img){
                img.src="data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==";
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
                    if(img.parentElement
                        &&img.parentElement.parentElement
                        &&img.parentElement.parentElement.tagName=="TH"){
                        return;        
                    }
                    var style = img.style;
                    img.style.width = w>width/2?width+"px":w+"px";
                    img.style.height = w>width/2?height+"px":h+"px";
                }
            }
            
            var images = document.getElementsByTagName('img');
                for(var i=0;i<images.length;i++){
                    var img = images[i];
                    if(!img.src){
                        setDefaultSrc(img);
                    }
                    (function(img){
                        setImgStyle(img);
                    })(img);
                }
            lazyload(imgs);

        	// 懒加载实现
        	function lazyload(imgs){
                console.log("lazyload...");
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
        	// 滚屏函数
        	window.onscroll =function(){
        		lazyload(imgs);
        	}
        });

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
        val t = System.currentTimeMillis()
        val html = Wget.get(url)
        val time = System.currentTimeMillis() - t
        "Wget time:${time},url:$url".logE()
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
                customScript = """//加载头像
                    function addUser(response){
                        var divs = document.getElementsByClassName("author-info-box");
                        var blocks = document.getElementsByClassName("author-info-block");
                        var post = JSON.parse(response);
                        var user = post.d.user;
                        if(divs.length>0){
                            var div = divs[0];
                            var userDom = div.children[0];
                            userDom.innerText = user.username;
                        }
                        if(blocks.length>0){
                             var iconDom = blocks[0].children[0].children[0];
                             iconDom.style.backgroundImage = "url("+user.avatarLarge+")";
                        }
                    }
                    addLoadEvent(function(){
                        var href = location.href;
                        var postId = href.substring(href.lastIndexOf("/")+1);
                        ajax({
                            url:"https://post-storage-api-ms.juejin.im/v1/getDetailData",
                            data:{src:"web",type:"entry",postId:postId},
                            success: function (response, xml) {
                                addUser(response);
                            }
                        });
                    });
                """.trimIndent()
            }
            url.startsWith(WEI_XIN) -> {
                selector = "div.rich_media_inner"
                cssPath = "web2/dark/weixin.css"
                titleSelector = ".rich_media_title"
            }
            url.contains(CSDN) -> {
                selector = "#article"
                titleSelector = "h1.article_title"
                cssPath = "web2/dark/csdn.css"
                customScript = "hljs.initHighlightingOnLoad();"
                customJs = """
                        <script type="text/javascript" src="/resources/js/highlight.pack.js"></script>
                    """.trimIndent()
            }
        }

        val elements = doc.select(selector)

        css = cssMap[siteKey]
        if (css == null) {
            css = FileUtil.readStringInAssets(cssPath)
        }
        if (elements.size == 1) {
            val ele = elements[0]
            ele.select("script").remove()
            ele.select("link").remove()
            ele.select("article-type").remove()
            val titleElements = ele.select(titleSelector)
            content = ele.outerHtml()
            if (titleElements.size == 1) {
                title = titleElements[0].ownText()
            }
        }
        val parseTime = System.currentTimeMillis() - t
        "totalParseTime:$parseTime".logE()
        return """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />
                    <title>$title</title>
                    $dom2imageJs
                    $customJs
                    <script type="text/javascript">
                        $ajax
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