### 暗黑系WanAndroid客户端
- 首款全暗黑系玩Android APP
- 适配wanandroid 每日一问，手机看答案，体验升级
- 适配简书文章，去广告，无需手动折叠打开
- 适配掘金文章，去header/footer，关注内容本身
- 适配CSDN文章
- 适配公众号文章

### 项目架构
- 基于mvvm模式开发
- 网络层使用[LiveData+Retrofit](https://www.jianshu.com/p/34fb6ffaa684)，剔除RxJava
- 使用DataBinding展示数据，自定义属性，事件[CommonBinding](https://github.com/iamyours/Wandroid/blob/master/app/src/main/java/io/github/iamyours/wandroid/binds/CommonBinding.kt)，简化数据交互
- 自定义WebViewClient，适配简书，掘金，wanandroid手机端，注入css,js适配文章暗黑模式

### 效果
#### 玩android 每日问答
![问答](screen/wenda.gif)
#### 玩android 项目文章
![项目](screen/project.gif)
#### 掘金
![掘金](screen/juejin.gif)
#### 简书
![简书](screen/jianshu.gif)
#### CSDN
![CSDN](screen/csdn.gif)
#### 公众号
![鸿洋](screen/wx-hongyang.gif)
![郭霖](screen/wx-guolin.gif)
![玉刚](screen/wx-yugang.gif)
![code小生](screen/wx-code.gif)
![Android群英传](screen/wx-qunyingzhuan.gif)

