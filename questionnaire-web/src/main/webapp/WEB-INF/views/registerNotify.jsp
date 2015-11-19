<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>激活XX账号</title>
    <script src="<c:url value="/js/jquery.js" />"></script>
    <script src="<c:url value="/js/layer/layer.js"/>"></script>
    <script>
        $(function(){
            $('#resendEmail').click(function(){
                var data ={ email: '${param.email}'};
                $.get('/user/registerEmailSend', data, function(data){
                    if(data == 'success'){
                        layer.msg('发送成功');
                    }
                    else if(data == 'invalid email'){
                        layer.msg('邮箱无效, 请重新注册');
                    }
                    else{
                        // account error
                        layer.msg('发送失败');
                    }
                });
                return false;
            });
        });
    </script>
</head>
<body>
    <p>我们已经向您的邮箱${param.email}发送了一封激活邮件，请点击邮件中的链接完成注册！</p>
    <a href="${emailUrl }" target="_blank">立即进入邮箱</a>
    没有收到邮件? 点击<a id="resendEmail" href="#">重新发送</a>
</body>
</html>
