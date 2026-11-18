<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order-detail.css?v=1">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div class="back-nav-wrapper">
        <div class="back-nav">
            <a href="order-history" class="back-link">
                <i class="fa fa-arrow-left"></i>
                <span>Quay lại</span>
            </a>
        </div>
    </div>
    <section>
        <div class="container">
            <div class="title-panel">
                <h2>Đơn hàng #${order.id}</h2>
            </div>
            <div class="order-details">
                <div class="info-panel">
                    <h3 class="info-title">Thông tin đơn hàng</h3>
                    <div class="info-row">
                        <p><strong>Mã đơn hàng:</strong></p>
                        <p>${order.id}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Ngày đặt:</strong></p>
                        <p>${order.date}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Trạng thái đơn hàng:</strong></p>
                        <p>${order.status}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Xác thực chữ ký:</strong></p>
                        <c:choose>
                            <c:when test="${signatureStatus == 'VALID'}">
                                <p class="sig-valid">Đã xác thực</p>
                            </c:when>
                            <c:when test="${signatureStatus == 'INVALID'}">
                                <p class="sig-invalid">Không hợp lệ</p>
                            </c:when>
                            <c:otherwise>
                                <p class="sig-unsigned">Chưa xác thực</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="product-list">
                    <c:forEach items="${order.orderItems}" var="oi">
                        <a class="product" href="product-detail?productId=${oi.productId}">
                            <div class="product-image-name">
                                <img class="product-image" src="${oi.productImage}"/>
                                <p class="product-name">${oi.productName}</p>
                            </div>
                            <div class="product-price-quantity">
                                <p class="product-price"><strong>${oi.format(oi.productPrice)}đ</strong></p>
                                <p class="product-quantity">SL: ${oi.productQuantity}</p>
                            </div>
                        </a>
                    </c:forEach>
                </div>
                <div class="info-panel">
                    <h3 class="info-title">Thông tin thanh toán</h3>
                    <div class="info-row">
                        <p><strong>Tồng thanh toán:</strong></p>
                        <p>${order.format(order.total)}đ</p>
                    </div>
                </div>
                <div class="info-panel">
                    <h3 class="info-title">Thông tin người nhận hàng</h3>
                    <div class="info-row">
                        <p><strong>Họ và tên:</strong></p>
                        <p>${order.fullName}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Số điện thoại:</strong></p>
                        <p>${order.phoneNumber}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Địa chỉ nhận hàng:</strong></p>
                        <p>${order.address}</p>
                    </div>
                    <div class="info-row">
                        <p><strong>Lưu ý cho người bán:</strong></p>
                        <p>${order.note}</p>
                    </div>
                </div>
                <div class="button-panel">
                    <a class="reorder-button" href="repurchase?orderId=${order.id}">Mua lại</a>
                </div>
                <c:if test="${signatureStatus == 'UNSIGNED'}">
                    <div class="button-panel">
                        <a class="chu-ky" href="order-sign?orderId=${order.id}">Xác thực</a>
                    </div>
                </c:if>
            </div>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>