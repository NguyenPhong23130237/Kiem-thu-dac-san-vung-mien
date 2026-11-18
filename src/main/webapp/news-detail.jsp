<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${news.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <!-- Sidebar -->
    <aside class="admin-sidebar">
        <div class="logo">
            <h2><i class="fas fa-store"></i> DacSanVungMien</h2>
            <p>Quản trị hệ thống</p>
        </div>
        <nav>
            <ul>
                <li><a href="admin-dashboard.jsp"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>

                <p class="menu-section">Quản lý sản phẩm</p>
                <li><a href="admin-products.jsp"><i class="fas fa-box"></i> <span>Sản phẩm</span></a></li>
                <li><a href="admin-categories.jsp"><i class="fas fa-tags"></i> <span>Danh mục</span></a></li>
                <li><a href="admin-brands.jsp"><i class="fas fa-trademark"></i> <span>Thương hiệu</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/banners"><i class="fas fa-image"></i> <span>Banner</span></a></li>

                <p class="menu-section">Khuyến mãi</p>
                <li><a href="admin-product-promotions.jsp"><i class="fas fa-percent"></i> <span>KM Sản phẩm</span></a></li>
                <li><a href="admin-category-promotions.jsp"><i class="fas fa-tag"></i> <span>KM Danh mục</span></a></li>
                <li><a href="admin-discount-vouchers.jsp"><i class="fas fa-ticket-alt"></i> <span>Phiếu giảm giá</span></a></li>

                <p class="menu-section">Đơn hàng</p>
                <li><a href="admin-orders.jsp"><i class="fas fa-shopping-cart"></i> <span>Đơn hàng</span></a></li>

                <p class="menu-section">Người dùng</p>
                <li><a href="admin-users.jsp"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
                <li><a href="admin-shipping-info.jsp"><i class="fas fa-map-marker-alt"></i> <span>Địa chỉ giao</span></a></li>
                <li><a href="admin-reviews.jsp"><i class="fas fa-star"></i> <span>Đánh giá</span></a></li>

                <p class="menu-section">Nội dung</p>
                <li><a href="AdminTinTuc.html" class="active"><i class="fas fa-newspaper"></i> <span>Tin tức</span></a></li>

                <p class="menu-section">Hệ thống</p>
                <li><a href="index.jsp"><i class="fas fa-external-link-alt"></i> <span>Xem Website</span></a></li>
                <li><a href="login.jsp"><i class="fas fa-sign-out-alt"></i> <span>Đăng xuất</span></a></li>
            </ul>
        </nav>
    </aside>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Chi tiết Tin tức</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/admin/news?action=list"
                   class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>
            </div>
        </header>

        <div class="admin-content">
            <c:if test="${not empty news}">
                <div class="news-detail-container">
                    <h2>${news.title}</h2>

                    <div class="news-meta">
                        <p><strong>Tác giả:</strong> ${news.author}</p>
                        <p><strong>Ngày đăng:</strong>
                            <fmt:formatDate value="${news.publishedDate}" pattern="dd/MM/yyyy HH:mm" />
                        </p>
                    </div>

                    <c:if test="${not empty news.image}">
                        <div class="news-image">
                            <img src="${pageContext.request.contextPath}/images/${news.image}"
                                 alt="${news.title}" style="max-width: 100%;">
                        </div>
                    </c:if>

                    <div class="news-content">
                        ${news.content}
                    </div>

                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/news?action=edit&id=${news.id}"
                           class="btn btn-warning">
                            <i class="fas fa-edit"></i> Chỉnh sửa
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/news?action=delete&id=${news.id}"
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa bài viết này?')">
                            <i class="fas fa-trash"></i> Xóa
                        </a>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty news}">
                <div class="alert alert-danger">
                    Bài viết không tồn tại!
                </div>
            </c:if>
        </div>
    </main>
</body>
</html>