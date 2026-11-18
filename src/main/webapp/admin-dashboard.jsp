<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (request.getAttribute("admin") == null
            && !request.getRequestURI().endsWith("/admin")) {
        response.sendRedirect(request.getContextPath() + "/admin");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Đặc Sản Vùng Miền</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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
                <li><a href="<%=request.getContextPath()%>/admin" class="active"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>

                <li class="menu-section">Quản lý sản phẩm</li>
                <li><a href="<%=request.getContextPath()%>/admin/products"><i class="fas fa-box"></i> <span>Sản phẩm</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/categories"><i class="fas fa-tags"></i> <span>Danh mục</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/brands"><i class="fas fa-trademark"></i> <span>Thương hiệu</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/banners"><i class="fas fa-image"></i> <span>Banner</span></a></li>

                <li class="menu-section">Khuyến mãi</li>
                <li><a href="<%=request.getContextPath()%>/admin/product-discounts"><i class="fas fa-percent"></i> <span>KM Sản phẩm</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/category-discounts"><i class="fas fa-tag"></i> <span>KM Danh mục</span></a></li>

                <li class="menu-section">Đơn hàng</li>
                <li><a href="<%=request.getContextPath()%>/admin/orders"><i class="fas fa-shopping-cart"></i> <span>Đơn hàng</span></a></li>

                <li class="menu-section">Người dùng</li>
                <li><a href="<%=request.getContextPath()%>/admin/users"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/shipping-info"><i class="fas fa-map-marker-alt"></i> <span>Địa chỉ giao</span></a></li>
                <li><a href="<%=request.getContextPath()%>/admin/key"> <i class="fas fa-key"></i> <span>Khóa người dùng</span></a></li>

                <li class="menu-section">Nội dung</li>
                <li><a href="<%=request.getContextPath()%>/admin-news.jsp"><i class="fas fa-newspaper"></i> <span>Tin tức</span></a></li>

                <li class="menu-section">Hệ thống</li>
                <li><a href="<%=request.getContextPath()%>/home"><i class="fas fa-external-link-alt"></i> <span>Xem Website</span></a></li>
                <li><a href="<%=request.getContextPath()%>/logout"><i class="fas fa-sign-out-alt"></i><span>Đăng xuất</span></a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <main class="admin-main">
        <header class="admin-header">
            <h1>Dashboard</h1>
            <div class="user-info">
                <span>Xin chào, Admin</span>
                <img src="images/user.png" alt="Admin">
            </div>
        </header>

        <div class="admin-content">
            <!-- Stats Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="icon blue"><i class="fas fa-box"></i></div>
                    <div class="info">
                        <h3><c:out value="${totalProducts != null ? totalProducts : 0}"/></h3>
                        <p>Tổng sản phẩm</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="icon green"><i class="fas fa-shopping-cart"></i></div>
                    <div class="info">
                        <h3><c:out value="${ordersToday != null ? ordersToday : 0}"/></h3>
                        <p>Đơn hàng hôm nay</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="icon orange"><i class="fas fa-users"></i></div>
                    <div class="info">
                        <h3><c:out value="${totalUsers != null ? totalUsers : 0}"/></h3>
                        <p>Tổng người dùng</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="icon purple"><i class="fas fa-dollar-sign"></i></div>
                    <div class="info">
                        <h3>
                            <fmt:formatNumber value="${monthlyRevenue != null ? monthlyRevenue : 0}" type="currency" currencyCode="VND"/>
                        </h3>
                        <p>Doanh thu tháng</p>
                    </div>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="quick-actions">
                <a href="<%=request.getContextPath()%>/admin-products.jsp" class="quick-action-card">
                    <i class="fas fa-plus-circle"></i>
                    <p>Thêm sản phẩm</p>
                </a>
                <a href="<%=request.getContextPath()%>/admin-orders.jsp" class="quick-action-card">
                    <i class="fas fa-clipboard-list"></i>
                    <p>Xem đơn hàng</p>
                </a>
                <a href="<%=request.getContextPath()%>/admin-news.jsp" class="quick-action-card">
                    <i class="fas fa-edit"></i>
                    <p>Đăng tin tức</p>
                </a>
            </div>
        </div>
    </main>
    <script>
        window.APP_CTX = '<%= request.getContextPath() %>';
    </script>
    <script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
    <script src="<%=request.getContextPath()%>/js/admin-dashboard.js"></script>
</body>
</html>

