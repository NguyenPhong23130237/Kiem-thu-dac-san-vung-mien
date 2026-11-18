<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Redirect to controller when opened directly.
    if (request.getAttribute("brands") == null && !request.getRequestURI().endsWith("/admin/brands")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/brands" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Thương hiệu - Admin</title>
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
                <li><a href="<%=request.getContextPath()%>/admin/brands" class="active"><i class="fas fa-trademark"></i> <span>Thương hiệu</span></a></li>
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
        <h1>Quản lý Thương hiệu</h1>
        <div class="user-info">
            <span>Xin chào, Admin</span>
            <img src="<%=request.getContextPath()%>/images/user.png" alt="Admin">
        </div>
    </header>

        <div class="admin-content">
            <div class="data-table-container">
                <div class="table-header">
                    <h2><i class="fas fa-trademark"></i> Danh sách thương hiệu</h2>
                    <div class="table-actions">
                        <form action="${pageContext.request.contextPath}/admin/brands" method="get" style="display:flex; gap:8px;">
                            <input type="text"
                                   name="search"
                                   class="search-box"
                                   placeholder="Tìm kiếm thương hiệu..."
                                   value="${param.search}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                        <button class="btn btn-primary" onclick="openModal('addModal')">
                            <i class="fas fa-plus"></i> Thêm mới
                        </button>
                    </div>
                </div>

            <c:if test="${param.error == 'empty'}">
                <div class="alert alert-danger">Tên thương hiệu không được để trống.</div>
            </c:if>
            <c:if test="${param.error == 'duplicate'}">
                <div class="alert alert-danger">Thương hiệu đã tồn tại.</div>
            </c:if>
            <c:if test="${param.error == 'db'}">
                <div class="alert alert-danger">Lỗi lưu dữ liệu. Vui lòng kiểm tra database.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id'}">
                <div class="alert alert-danger">ID không hợp lệ.</div>
            </c:if>
            <c:if test="${param.error == 'update_failed'}">
                <div class="alert alert-danger">Cập nhật thương hiệu thất bại.</div>
            </c:if>
            <c:if test="${param.error == 'delete_failed'}">
                <div class="alert alert-danger">Xóa thương hiệu thất bại (có thể đang được dùng bởi sản phẩm).</div>
            </c:if>

            <c:if test="${param.success == '1'}">
                <div class="alert alert-success">Thêm thương hiệu thành công.</div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success">Cập nhật thương hiệu thành công.</div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success">Xóa thương hiệu thành công.</div>
            </c:if>

            <div style="margin: 8px 0; font-size: 12px; color: #666;">
                Tổng thương hiệu: <c:out value="${totalItems}" default="0"/>
            </div>

            <table class="data-table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>ID</th>
                    <th>Tên thương hiệu</th>
                    <th>Thông tin</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty brands}">
                        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}"/>
                        <c:forEach var="b" items="${brands}" varStatus="st">
                            <tr>
                                <td><c:out value="${startIndex + st.index + 1}"/></td>
                                <td><c:out value="${b.id}"/></td>
                                <td><c:out value="${b.name}"/></td>
                                <td><c:out value="${b.info}"/></td>
                                <td class="actions">
                                    <button type="button" class="btn btn-warning btn-sm js-edit-brand" title="Sửa"
                                            data-id="<c:out value='${b.id}'/>"
                                            data-name="<c:out value='${b.name}'/>"
                                            data-info="<c:out value='${b.info}'/>">
                                        <i class="fas fa-edit"></i>
                                    </button>

                                    <form action="<%=request.getContextPath()%>/admin/brand/delete" method="post"
                                          style="display:inline;">
                                        <input type="hidden" name="id" value="<c:out value='${b.id}'/>"/>
                                        <button type="submit" class="btn btn-danger btn-sm" title="Xóa"
                                                onclick="return confirm('Bạn có chắc chắn muốn xóa thương hiệu này?')">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" style="text-align: center">Chưa có thương hiệu nào trong database.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <div class="pagination">
                <c:set var="ctx" value="${pageContext.request.contextPath}"/>
                <c:set var="cp" value="${currentPage}"/>
                <c:set var="tp" value="${totalPages}"/>

                <c:if test="${cp > 1}">
                    <a class="btn" href="${ctx}/admin/brands?page=${cp-1}"><i class="fas fa-chevron-left"></i></a>
                </c:if>
                <c:if test="${cp <= 1}">
                    <button disabled><i class="fas fa-chevron-left"></i></button>
                </c:if>

                <c:forEach var="i" begin="1" end="${tp}">
                    <c:choose>
                        <c:when test="${i == cp}">
                            <button class="active" disabled><c:out value="${i}"/></button>
                        </c:when>
                        <c:otherwise>
                            <a class="btn" href="${ctx}/admin/brands?page=${i}"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${cp < tp}">
                    <a class="btn" href="${ctx}/admin/brands?page=${cp+1}"><i class="fas fa-chevron-right"></i></a>
                </c:if>
                <c:if test="${cp >= tp}">
                    <button disabled><i class="fas fa-chevron-right"></i></button>
                </c:if>
            </div>
        </div>
    </div>
</main>

<!-- Add Modal -->
<div class="modal" id="addModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-plus-circle"></i> Thêm thương hiệu mới</h3>
            <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="addBrandForm" action="<%=request.getContextPath()%>/admin/brand/add" method="post">
                <div class="form-group">
                    <label>Tên thương hiệu *</label>
                    <input type="text" name="name" required placeholder="Nhập tên thương hiệu"
                           value="<c:out value='${param.name}'/>">
                </div>
                <div class="form-group">
                    <label>Thông tin</label>
                    <textarea name="info" placeholder="Nhập thông tin về thương hiệu..."><c:out
                            value='${param.info}'/></textarea>
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

<!-- Edit Modal -->
<div class="modal" id="editModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-edit"></i> Sửa thương hiệu</h3>
            <button class="modal-close" onclick="closeModal('editModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="editBrandForm" action="<%=request.getContextPath()%>/admin/brand/update" method="post">
                <input type="hidden" name="id" id="editBrandId"/>
                <div class="form-group">
                    <label>Tên thương hiệu *</label>
                    <input type="text" name="name" id="editBrandName" required>
                </div>
                <div class="form-group">
                    <label>Thông tin</label>
                    <textarea name="info" id="editBrandInfo"></textarea>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn" onclick="closeModal('editModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Cập nhật
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
    <script>
        window.APP_CTX = '<%= request.getContextPath() %>';
    </script>
<script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
<script src="<%=request.getContextPath()%>/js/admin-brands.js"></script>

</body>
</html>

