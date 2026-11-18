<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử mua hàng</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order-history.css?v=1">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <section class="section-lich-su-mua-hang">
        <div class="container">
            <div class="title-panel">
                <h2>Lịch sử mua hàng</h2>
            </div>
            <div class="order-list">
                <c:choose>
                    <c:when test="${not empty orders}">
                        <c:forEach var="o" items="${orders}">
                            <div class="order">
                                <a href="order-detail?orderId=${o.id}">
                                    <div class="order-info">
                                        <div class="order-header">
                                            <h3>Đơn hàng #${o.id}</h3>
                                            <p>Xem chi tiết ></p>
                                        </div>
                                        <div class="order-meta">
                                            <p>Ngày đặt: ${o.date}</p>
                                            <div class="order-status">
                                                <p class="trang-thai">${o.status}</p>
                                                <p>/</p>
                                                <c:set var="sigStatus" value="${signatureStatusMap[o.id]}"/>
                                                <c:choose>
                                                    <c:when test="${sigStatus == 'VALID'}">
                                                        <p class="chu-da-ky">Đã xác thực</p>
                                                    </c:when>
                                                    <c:when test="${sigStatus == 'INVALID'}">
                                                        <p class="chu-khong-hop-le">Không hợp lệ</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="chu-chua-ky">Chưa xác thực</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                        <div class="order-images">
                                            <c:forEach var="oi" items="${o.orderItems}" begin="0" end="3">
                                                <img src="${oi.productImage}" alt="${oi.productName}">
                                            </c:forEach>
                                        </div>
                                    </div>
                                </a>
                                <div class="order-total">
                                    <p>Tổng tiền: ${o.format(o.total)}đ</p>
                                </div>
                                <div class="button-panel">
                                    <c:if test="${signatureStatusMap[o.id] == 'UNSIGNED'}">
                                        <a class="sign-button"
                                           href="order-sign?orderId=${o.id}">
                                            Xác thực
                                        </a>
                                    </c:if>
                                    <a class="reorder-button" href="repurchase?orderId=${o.id}">
                                        Mua lại
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="text-align: center; font-weight: bold">Bạn chưa có đơn hàng nào.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>