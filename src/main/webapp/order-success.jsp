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
        <h1>THÀNH CÔNG</h1>
        <p class="info">
            <c:choose>
                <c:when test="${fromSign}">
                    Đơn hàng <strong>#<c:out value="${order.id}"/></strong> đã được
                    <strong>xác thực bằng chữ ký số</strong> và đặt thành công.
                    Tính toàn vẹn của đơn hàng đã được đảm bảo.
                </c:when>
                <c:otherwise>
                    Đơn hàng mã số <strong><c:out value="${order.id}"/></strong> của bạn đã được đặt thành công.
                </c:otherwise>
            </c:choose>
        </p>
        <div class="action-group">
            <a class="back-btn" href="home">Quay về trang chủ</a>
            <a class="back-btn secondary" href="order-history">Lịch sử đơn hàng</a>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>