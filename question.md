# 遇到的一些问题
这里的问题是我写这个小网站时遇到的一些问题, 如果关于这些问题你有自己的见解, 请抽出点时间告诉我, 我的邮箱是 [i.am.t.lbk@gmail.com](mailto:i.am.t.lbk@gmail.com)

## 关于软件分层
- 软件分层怎样分才合适? 向这个小测试网站这样, 由于spring**貌似**只能有一个加载资源文件的bean, 导致所有需要测试persist模块的地方都得配置加载的文件.这既是好事也是坏事:
    - 好事就是配置性更强: 向questionnaire-sms中的`git.lbk.questionnaire.phone.SmsImplParallelTest`测试类中我想使用h2database, 直接创建一个测试的配置文件, 里面写上h2database数据库的信息就可以
    - 坏事就是依赖太强了吧? 这里所有需要使用这个模块的地方都需要去加载那个资源文件.而且需要准确的配置上加密的属性名...
