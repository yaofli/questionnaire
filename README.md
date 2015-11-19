# questionnaire
一个小的调查问卷网站, 用来练习java技术

由于下面的文件包含用户名, 密码, 所以没有提交.
**里面的用户名/密码之类的信息都需要进行加密处理, 可以使用 questionnaire-persist 模块中的测试类 git.lbk.questionnaire.security.AESUtilsTest.decrypt 进行加密**

- questionnaire-sms/src/main/resources/ihuyiAccount.properties
    - 结构为:
    - sms.account=账号
    - sms.password=密码
- questionnaire-email/src/main/resources/emailAccount.properties
    - 结构和questionnaire-email/src/test/resources/emailAccountTest.properties一样
- questionnaire-ipAddress/src/main/resources/ipAddress.properties
    - 内容为
    - baidu.apikey=apikey
    - ip.url=http://apis.baidu.com/apistore/iplookupservice/iplookup


做这个网站的时候产生的大部分问题都由 fixme 注释标记出来, 还有部分问题写到了question.md中, 如果您关于这些问题有自己的看法, 请抽点时间, 跟我说说您的想法. 我的邮箱是 [i.am.t.lbk@gmail.com](mailto:i.am.t.lbk@gmail.com).