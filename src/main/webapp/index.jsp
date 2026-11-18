<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="css/index.css">
    <script src="js/index.js" defer></script>
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <section class="section-banner">
        <div class="container">
            <div class="slider">
                <div class="slides">
                    <c:forEach items="${banners}" var="b">
                        <div class="slide">
                            <img src="${b.image}"/>
                        </div>
                    </c:forEach>
                </div>
                <button class="arrow prev" type="button" aria-label="Prev">&#10094;</button>
                <button class="arrow next" type="button" aria-label="Next">&#10095;</button>
                <div class="dots" id="dots">
                    <c:forEach items="${banners}" var="b" varStatus="status">
                        <button
                                type="button"
                                class="dot ${status.first ? 'active' : ''}"
                                data-index="${status.index}"
                                aria-label="Slide ${status.index + 1}">
                        </button>
                    </c:forEach>
                </div>
            </div>
        </div>
    </section>
    <c:if test="${not empty topDiscountProducts}">
        <section class="section-product">
            <div class="container">
                <img class="label-image" src="images/sale-tag.png"/>
                <h1 class="title">Khuyến mãi</h1>
                <div class="slider">
                    <div class="product-list slides">
                        <c:forEach items="${topDiscountProducts}" var="p">
                            <div class="product">
                                <div class="sale-tag">
                                    <p class="sale-percent">-${p.getDiscountPercentage()}%</p>
                                </div>
                                <a href="product-detail?productId=${p.getId()}">
                                    <img class="product-image" src="${p.getImage()}"/>
                                </a>
                                <a class="product-name" href="product-detail?productId=${p.getId()}">
                                    <h3>${p.getName()}</h3>
                                </a>
                                <div class="product-price">
                                    <h4 class="new-price">${p.format(p.getDiscountPrice())}đ</h4>
                                    <h4 class="old-price">${p.format(p.getPrice())}đ</h4>
                                </div>
                                <c:choose>
                                    <c:when test="${p.quantity > 0}">
                                        <a class="buy" href="add-cart?productId=${p.id}&quantity=1">Mua hàng</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="buy disabled">Hết hàng</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="arrow prev" type="button" aria-label="Prev">&#10094;</button>
                    <button class="arrow next" type="button" aria-label="Next">&#10095;</button>
                </div>
                <a class="show-all" href="promotions">
                    <h4>Xem tất cả →</h4>
                </a>
            </div>
        </section>
    </c:if>
    <c:if test="${not empty topNewProducts}">
        <section class="section-product">
            <div class="container">
                <img class="label-image" src="images/new.png"/>
                <h1 class="title">Sản phẩm mới</h1>
                <div class="slider">
                    <div class="product-list slides">
                        <c:forEach items="${topNewProducts}" var="p">
                            <div class="product">
                                <div class="sale-tag">
                                    <c:if test="${p.getDiscountPercentage() > 0}">
                                        <p class="sale-percent">-${p.getDiscountPercentage()}%</p>
                                    </c:if>
                                </div>
                                <a href="product-detail?productId=${p.getId()}">
                                    <img class="product-image" src="${p.getImage()}"/>
                                </a>
                                <a class="product-name" href="product-detail?productId=${p.getId()}">
                                    <h3>${p.getName()}</h3>
                                </a>
                                <div class="product-price">
                                    <h4 class="new-price">
                                            ${p.format(p.discountPercentage > 0 ? p.discountPrice : p.price)}đ
                                    </h4>
                                    <c:if test="${p.discountPercentage > 0}">
                                        <h4 class="old-price">
                                                ${p.format(p.price)}đ
                                        </h4>
                                    </c:if>
                                </div>
                                <c:choose>
                                    <c:when test="${p.quantity > 0}">
                                        <a class="buy" href="add-cart?productId=${p.id}&quantity=1">Mua hàng</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="buy disabled">Hết hàng</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="arrow prev" type="button" aria-label="Prev">&#10094;</button>
                    <button class="arrow next" type="button" aria-label="Next">&#10095;</button>
                </div>
                <a class="show-all" href="category-products?filter=new">
                    <h4>Xem tất cả →</h4>
                </a>
            </div>
        </section>
    </c:if>
    <c:if test="${not empty topSoldProducts}">
        <section class="section-product">
            <div class="container">
                <img class="label-image" src="images/best-seller.png"/>
                <h1 class="title">Sản phẩm bán chạy</h1>
                <div class="slider">
                    <div class="product-list slides">
                        <c:forEach items="${topSoldProducts}" var="p">
                            <div class="product">
                                <div class="sale-tag">
                                    <c:if test="${p.getDiscountPercentage() > 0}">
                                        <p class="sale-percent">-${p.getDiscountPercentage()}%</p>
                                    </c:if>
                                </div>
                                <a href="product-detail?productId=${p.getId()}">
                                    <img class="product-image" src="${p.getImage()}"/>
                                </a>
                                <a class="product-name" href="product-detail?productId=${p.getId()}">
                                    <h3>${p.getName()}</h3>
                                </a>
                                <div class="product-price">
                                    <h4 class="new-price">
                                            ${p.format(p.discountPercentage > 0 ? p.discountPrice : p.price)}đ
                                    </h4>
                                    <c:if test="${p.discountPercentage > 0}">
                                        <h4 class="old-price">
                                                ${p.format(p.price)}đ
                                        </h4>
                                    </c:if>
                                </div>
                                <c:choose>
                                    <c:when test="${p.quantity > 0}">
                                        <a class="buy" href="add-cart?productId=${p.id}&quantity=1">Mua hàng</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="buy disabled">Hết hàng</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                    <button class="arrow prev" type="button" aria-label="Prev">&#10094;</button>
                    <button class="arrow next" type="button" aria-label="Next">&#10095;</button>
                </div>
                <a class="show-all" href="category-products?filter=sold">
                    <h4>Xem tất cả →</h4>
                </a>
            </div>
        </section>
    </c:if>
    <section class="section-dac-san-ba-mien">
        <div class="container">
            <h1 class="title">Đặc sản ba miền</h1>
            <div class="dac-san-ba-mien">
                <a class="dac-san-mien" href="category-products?region=north">
                    <div class="overlay"></div>
                    <h2 class="dac-san-mien-text">Đặc sản miền Bắc</h2>
                    <img src="images/ho-hoan-kiem-2.jpg"/>
                </a>
                <a class="dac-san-mien" href="category-products?region=central">
                    <div class="overlay"></div>
                    <h2 class="dac-san-mien-text">Đặc sản miền Trung</h2>
                    <img src="images/mien-trung.jpg">
                </a>
                <a class="dac-san-mien" href="category-products?region=south">
                    <div class="overlay"></div>
                    <h2 class="dac-san-mien-text">Đặc sản miền Nam</h2>
                    <img src="images/Du-lich-mien-Nam-thang-1-2.jpg"/>
                </a>
            </div>
        </div>
    </section>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>