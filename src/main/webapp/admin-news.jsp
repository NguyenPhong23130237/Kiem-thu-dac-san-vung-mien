<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%
    // Redirect to controller when opened directly (keep consistent with other admin pages)
    if (request.getAttribute("newsList") == null && !request.getRequestURI().contains("/admin/news")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/news" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Tin tức - Admin</title>
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
                <li><a href="<%=request.getContextPath()%>/admin"><i class="fas fa-home"></i> <span>Dashboard</span></a></li>

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
                <li><a href="<%=request.getContextPath()%>/admin-news.jsp" class="active"><i class="fas fa-newspaper"></i> <span>Tin tức</span></a></li>

                <li class="menu-section">Hệ thống</li>
                <li><a href="<%=request.getContextPath()%>/home"><i class="fas fa-external-link-alt"></i> <span>Xem Website</span></a></li>
                <li><a href="<%=request.getContextPath()%>/logout"><i class="fas fa-sign-out-alt"></i><span>Đăng xuất</span></a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <main class="admin-main">
        <header class="admin-header">
            <h1>Quản lý Tin tức / Blog</h1>
            <div class="user-info">
                <span>Xin chào, Admin</span>
                <img src="<%=request.getContextPath()%>/images/user.png" alt="Admin">
            </div>
        </header>

        <div class="admin-content">
            <div class="data-table-container">
                <div class="table-header">
                    <h2><i class="fas fa-newspaper"></i> Danh sách bài viết</h2>
                    <div class="table-actions">
                        <form action="${pageContext.request.contextPath}/admin/news" method="get" style="display:flex; gap:8px;">
                            <input type="text"
                                   name="search"
                                   class="search-box"
                                   placeholder="Tìm kiếm bài viết..."
                                   value="${param.search}">
                            <button type="submit" class="btn btn-primary" title="Tìm kiếm">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                        <button class="btn btn-primary" onclick="openModal('addModal')">
                            <i class="fas fa-plus"></i> Thêm mới
                        </button>
                    </div>
                </div>

                <c:if test="${not empty message}">
                    <div class="alert alert-success">
                        ${message}
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <div style="margin: 8px 0; font-size: 12px; color: #666;">
                    Tổng bài viết: <c:out value="${totalItems}" default="${newsList != null ? newsList.size() : 0}"/>
                </div>

                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Hình ảnh</th>
                            <th>Tiêu đề</th>
                            <th>Tác giả</th>
                            <th>Ngày đăng</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty newsList}">
                                <c:forEach var="news" items="${newsList}">
                                    <tr>
                                        <td><c:out value="${news.id}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty news.image}">
                                                    <img src="${pageContext.request.contextPath}/images/${news.image}"
                                                         alt="<c:out value='${news.title}'/>"
                                                         style="width: 80px; height: 60px; object-fit: cover;">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/default-news.jpg"
                                                         alt="No image" style="width: 80px; height: 60px; object-fit: cover;">
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><c:out value="${news.title}"/></td>
                                        <td><c:out value="${news.author}"/></td>
                                        <td>
                                            <fmt:formatDate value="${news.publishedDate}" pattern="dd/MM/yyyy" />
                                        </td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/admin/news?action=view&id=${news.id}"
                                               class="btn btn-info btn-sm" title="Xem">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/news?action=edit&id=${news.id}"
                                               class="btn btn-warning btn-sm" title="Sửa">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/admin/news" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete" />
                                                <input type="hidden" name="id" value="${news.id}" />
                                                <button type="submit"
                                                        class="btn btn-danger btn-sm js-delete-news"
                                                        data-title="<c:out value='${news.title}'/>"
                                                        title="Xóa"
                                                        onclick="return confirm('Bạn có chắc chắn muốn xóa bài viết này?')">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" style="text-align:center; padding: 16px; color: #666;">
                                        Không có bài viết nào.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>

            </div>
        </div>
    </main>

    <script>
        window.APP_CTX = '<%= request.getContextPath() %>';
    </script>
    <script src="${pageContext.request.contextPath}/js/admin-core.js"></script>
    <script src="${pageContext.request.contextPath}/js/admin-news.js"></script>

    <!-- Add Modal (match convention like admin-products/admin-categories) -->
    <div class="modal" id="addModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3><i class="fas fa-plus-circle"></i> Thêm bài viết mới</h3>
                <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
            </div>
            <div class="modal-body">
                <form id="addNewsForm" action="${pageContext.request.contextPath}/admin/news" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add" />

                    <div class="form-group">
                        <label>Tiêu đề *</label>
                        <input type="text" name="title" required placeholder="Nhập tiêu đề bài viết" value="<c:out value='${param.title}'/>" />
                    </div>

                    <div class="form-group">
                        <label>Tác giả *</label>
                        <input type="text" name="author" required placeholder="Nhập tên tác giả" value="<c:out value='${param.author}'/>" />
                    </div>

                    <div class="form-group">
                        <label>Ngày đăng *</label>
                        <input type="date" name="published_date" required value="<c:out value='${param.published_date}'/>" />
                    </div>

                    <div class="form-group">
                        <label>Hình ảnh</label>
                        <input type="file" name="image" accept="image/*" />
                    </div>

                    <div class="form-group">
                        <label>Nội dung *</label>
                        <textarea name="content" rows="8" required placeholder="Nhập nội dung...">${param.content}</textarea>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn" onclick="closeModal('addModal')">Hủy</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Lưu
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>

