package io.github.iamyours.wandroid.util

object Script {
    fun getDom2Image(ww: Int): String {
        return """
            var lastTime = new Date().getTime();
                var imgs = document.getElementsByTagName("img");
                for(var i=0;i<imgs.length;i++){
                    var dataset = imgs[i].dataset;
                    if(dataset && dataset.src && dataset.src!=imgs[i].src){
                        imgs[i].src = dataset.src;
                    }
                    imgs[i].onclick = function(e){
                        var t = new Date().getTime();
                        if(t-lastTime<500)return;
                        lastTime = t;
                        var target = e.target;
                        var rect = target.getBoundingClientRect();
                        android.showImage(target.src,rect.x,rect.y,rect.width,rect.height,outerWidth);
                        e.stopPropagation();
                    };
                }
                var ww = ${ww}.0;
                var scale = ww/outerWidth;
                var pres = document.getElementsByTagName("pre");
                if(pres.length==0)pres = document.getElementsByTagName("pre");
                for(var i=0;i<pres.length;i++){
                    pres[i].onclick = function(e){
                        var t = new Date().getTime();
                        if(t-lastTime<500)return;
                        lastTime = t;
                        var imgWidth = this.scrollWidth;
                        var node = this;
                        for(var n=0;n<this.childElementCount;n++){
                            var child = this.children[n];
                            if(child.tagName=="CODE"||child.tagName=='SECTION'){
                            var cw = child.scrollWidth;
                                if(cw>imgWidth){
                                    imgWidth = cw;
                                    node = child;
                                }
                            }
                        }
                        
                        imgWidth = imgWidth+3;
                        var imgHeight = node.offsetHeight;
                        var rect = node.getBoundingClientRect();
                        console.log(node.tagName);
                        android.showImage("",rect.x,rect.y,imgWidth,rect.height,outerWidth);
                        domtoimage.toPng(node,{width:imgWidth*scale,height:rect.height*scale,
                                style: {
                                    transform: "scale(" + scale + ")",
                                    transformOrigin: "top left",
                                    width: imgWidth + "px",
                                    height: rect.height + "px"
                                }
                            })
                            .then(function(data){
                                console.log(data);
                                android.showImage(data,rect.x,rect.y,imgWidth,rect.height,outerWidth);
                            });
                    };
                }
        """.trimIndent()
    }
}