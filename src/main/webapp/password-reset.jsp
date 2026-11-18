<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="css/change-password.css">

</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div id="title">Đặt lại mật khẩu</div>
    <c:if test="${not empty error}">
        <p style="color:red; text-align:center;">${error}</p>
    </c:if>

    <form action="password-reset" method="post">
        <input type="hidden" name="token" value="${param.token}">
        <div class="change-pass">
            <label>Mật khẩu mới:</label>
            <input type="password" name="password" required minlength="8" title="Mật khẩu phải có ít nhất 8 ký tự">

            <label>Nhập lại mật khẩu:</label>
            <input type="password" name="confirmPassword" required minlength="8"
                   title="Mật khẩu phải có ít nhất 8 ký tự">
        </div>

        <button class="comfirm-button" type="submit">Xác nhận</button>
    </form>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>