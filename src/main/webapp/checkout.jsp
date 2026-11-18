<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thông tin thanh toán</title>
    <link rel="stylesheet" href="css/checkout.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="css/global.css">
    <script src="js/ThanhToan.js"></script>

</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div class="back-nav-wrapper">
        <div class="back-nav">
            <a href="my-cart" class="back-link">
                <i class="fa fa-arrow-left"></i>
                <span>Quay lại</span>
            </a>
        </div>
    </div>
    <h1 class="title">Thông tin thanh toán</h1>
    <section class="section-thanh-toan">
        <div class="information">
            <h2>Thông tin giao hàng</h2>
            <form action="confirm-checkout" method="post">
                <div class="table-information">
                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty sessionScope.cart.shippingInfo}">
                                <div class="address-container">
                                    <div class="address-info">
                                        <p><strong>${sessionScope.cart.shippingInfo.receiverName}</strong>
                                            | ${sessionScope.cart.shippingInfo.receiverPhoneNumber}</p>
                                        <p>${sessionScope.cart.shippingInfo.ward}, ${sessionScope.cart.shippingInfo.district}, ${sessionScope.cart.shippingInfo.province}</p>
                                        <p>${sessionScope.cart.shippingInfo.detailedAddress}</p>
                                    </div>
                                    <a href="shipping-info?mode=select">
                                        <div class="change">
                                            <p class="change-btn"><i class="fa-solid fa-arrows-rotate"></i>Thay đổi
                                            </p>
                                        </div>
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="address-container">
                                    <div class="address-info">
                                        <p><strong>Chưa chọn thông tin giao hàng</strong></p>
                                    </div>
                                    <a href="shipping-info?mode=select">
                                        <div class="change">
                                            <p class="change-btn">Chọn thông tin giao hàng
                                            </p>
                                        </div>
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>


                    <div class="row">
                        <textarea id="message" name="note" rows="4" placeholder="Lưu ý cho người bán"></textarea>
                    </div>

                    <div class="choosepay">
                        <label class="payment-option">
                            <input type="radio" name="payment-method" value="COD" checked
                                   required>
                            <span class="custom-radio"></span>
                            <span class="text">Thanh toán sau khi nhận hàng</span>
                        </label>

                        <label class="payment-option">
                            <input type="radio" name="payment-method" value="Online">
                            <span class="custom-radio"></span>
                            <span class="text">Chuyển khoản ngân hàng</span>
                        </label>
                    </div>
                </div>
                <c:if test="${not empty error}">
                    <p style="color: red">${error}</p>
                </c:if>
                <button type="submit">Tiếp tục</button>
            </form>
        </div>
        <div class="listItem">
            <h>Danh sách đơn hàng</h>
            <div class="listbuy">
                <c:forEach items="${sessionScope.cart.items}" var="item">
                    <div class="listbuyItem">
                        <img src="${item.product.image}"/>
                        <span>${item.name} x ${item.quantity}</span>
                        <span class="price">${item.format(item.price)}đ</span>
                    </div>
                </c:forEach>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>Tổng số tiền</th>
                    <th>${sessionScope.cart.format(sessionScope.cart.total)}đ</th>
                </tr>
                </tfoot>
            </table>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>