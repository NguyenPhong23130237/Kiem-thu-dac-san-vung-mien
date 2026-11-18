<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="css/forgot-password.css">
    <link rel="stylesheet" href="css/global.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div id="title">Quên mật khẩu</div>

    <form action="forgot-password" method="post">
        <div class="table-forget_password">
            <div class="comfirm">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="Nhập email" required>
                <c:if test="${not empty error}">
                    <p style="color:red;">${error}</p>
                </c:if>
                <button type="submit" name="action" value="sendOtp">
                    Xác nhận
                </button>
            </div>
        </div>
    </form>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>
