<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thành công</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="css/success.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <section class="success-container">
        <div class="success-icon">
            <i class="fa-solid fa-circle-check"></i>
        </div>
        <h1>Thành công!</h1>
        <p class="info">
            Tài khoản <strong>${email}</strong> của bạn đã được đặt lại mật khẩu thành công.
        </p>
        <a class="back-btn" href="login">Đăng nhập ngay</a>
    </section>
</main>


<%@ include file="footer.jsp" %>
</body>
</html>