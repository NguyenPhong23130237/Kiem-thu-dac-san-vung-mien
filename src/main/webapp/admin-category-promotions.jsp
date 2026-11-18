    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%
        if (request.getAttribute("discounts") == null
                && !request.getRequestURI().endsWith("/admin/category-discounts")) {
            response.sendRedirect(request.getContextPath() + "/admin/category-discounts");
            return;
        }
    %>
    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Khuyến mãi Danh mục - Admin</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <meta name="context-path" content="${pageContext.request.contextPath}" />
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
                    <li><a href="<%=request.getContextPath()%>/admin/category-discounts" class="active"><i class="fas fa-tag"></i> <span>KM Danh mục</span></a></li>

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
                <h1>Quản lý Khuyến mãi Danh mục</h1>
                <div class="user-info">
                    <span>Xin chào, Admin</span>
                </div>
            </header>

            <div class="admin-content">
                <div class="data-table-container">
                    <div class="table-header">
                        <h2><i class="fas fa-tag"></i> Danh sách khuyến mãi danh mục</h2>
                        <div class="table-actions">
                            <form action="${pageContext.request.contextPath}/admin/category-discounts" method="get" style="display:flex; gap:8px;">
                                <input type="text"
                                       name="search"
                                       class="search-box"
                                       placeholder="Tìm kiếm khuyến mãi danh mục..."
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
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>ID</th>
                                <th>Danh mục</th>
                                <th>Giảm giá</th>
                                <th>Ngày bắt đầu</th>
                                <th>Ngày kết thúc</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty discounts}">
                                <c:forEach var="discount" items="${discounts}" varStatus="loop">
                                    <tr>
                                        <td>${(loop.index + 1)}</td>
                                        <td>${discount.id}</td>
                                        <td>${discount.categoryName}</td>
                                        <td><span class="badge badge-success">${discount.discountPercentage}%</span></td>
                                        <td><fmt:formatDate value="${discount.startDate}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatDate value="${discount.endDate}" pattern="dd/MM/yyyy"/></td>
                                        <td class="discount-status-cell"
                                            data-discount-start="<fmt:formatDate value='${discount.startDate}' pattern='yyyy-MM-dd'/>"
                                            data-discount-end="<fmt:formatDate value='${discount.endDate}' pattern='yyyy-MM-dd'/>">
                                        </td>
                                        <td class="actions">
                                            <a href="#" class="btn btn-warning btn-sm js-edit-category-discount"
                                               data-id="${discount.id}"
                                               data-category="${discount.categoryId}"
                                               data-discount="${discount.discountPercentage}"
                                               data-start="<fmt:formatDate value='${discount.startDate}' pattern='yyyy-MM-dd'/>"
                                               data-end="<fmt:formatDate value='${discount.endDate}' pattern='yyyy-MM-dd'/>">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a class="btn btn-danger btn-sm"
                                               title="Xóa"
                                               href="${pageContext.request.contextPath}/admin/category-discounts?action=delete&id=${discount.id}"
                                               onclick="return confirm('Bạn có chắc muốn xóa khuyến mãi danh mục này?');">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty discounts}">
                                <tr>
                                    <td colspan="8" style="text-align: center;">Không có dữ liệu khuyến mãi</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                    <div class="pagination">
                        <button><i class="fas fa-chevron-left"></i></button>
                        <button class="active">1</button>
                        <button><i class="fas fa-chevron-right"></i></button>
                    </div>
                </div>
            </div>
        </main>

        <!-- Add Modal -->
        <div class="modal" id="addModal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3><i class="fas fa-plus-circle"></i> Thêm khuyến mãi danh mục</h3>
                    <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
                </div>

                <div class="modal-body">
                    <c:if test="${errorDuplicateCategory}">
                        <div style="color: red; margin-bottom: 10px; font-weight: bold;" data-error-duplicate-category>Danh mục này đã có khuyến mãi. Vui lòng chọn danh mục khác!</div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/admin/category-discounts"
                          method="post">

                        <input type="hidden" name="action" value="add">

                        <div class="form-group">
                            <label>Danh mục <span class="required">*</span></label>
                            <select name="category" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}" <c:if test="${formCategory == category.id}">selected</c:if>>
                                            ${category.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Phần trăm giảm giá (%) <span class="required">*</span></label>
                            <input type="number"
                                   name="discount_percentage"
                                   min="1"
                                   max="100"
                                   required
                                   placeholder="Ví dụ: 15"
                                   value="${formDiscount != null ? formDiscount : ''}">
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label>Ngày bắt đầu <span class="required">*</span></label>
                                <input type="date" name="start_date" required value="${formStart != null ? formStart : ''}">
                            </div>

                            <div class="form-group">
                                <label>Ngày kết thúc <span class="required">*</span></label>
                                <input type="date" name="end_date" required value="${formEnd != null ? formEnd : ''}">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn" onclick="closeModal('addModal')">
                                Hủy
                            </button>
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
                    <h3><i class="fas fa-edit"></i> Sửa khuyến mãi danh mục</h3>
                    <button type="button" class="modal-close"
                            onclick="closeModal('editModal')">&times;</button>
                </div>

                <div class="modal-body">
                    <form id="editCategoryDiscountForm"
                          action="${pageContext.request.contextPath}/admin/category-discounts"
                          method="post">

                        <!-- ID khuyến mãi -->
                        <input type="hidden" name="id" id="editId">

                        <!-- Phân biệt EDIT -->
                        <input type="hidden" name="action" value="update">

                        <!-- Danh mục -->
                        <div class="form-group">
                            <label>Danh mục <span class="required">*</span></label>
                            <select name="category" id="editCategory" required>
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}">
                                            ${category.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Phần trăm giảm -->
                        <div class="form-group">
                            <label>Phần trăm giảm giá (%) <span class="required">*</span></label>
                            <input type="number"
                                   name="discount_percentage"
                                   id="editDiscount"
                                   min="1" max="100"
                                   required>
                        </div>

                        <!-- Thời gian -->
                        <div class="form-row">
                            <div class="form-group">
                                <label>Ngày bắt đầu <span class="required">*</span></label>
                                <input type="date"
                                       name="start_date"
                                       id="editStart"
                                       required>
                            </div>

                            <div class="form-group">
                                <label>Ngày kết thúc <span class="required">*</span></label>
                                <input type="date"
                                       name="end_date"
                                       id="editEnd"
                                       required>
                            </div>
                        </div>

                        <!-- Footer nằm TRONG form để submit đúng -->
                        <div class="modal-footer">
                            <button type="button" class="btn"
                                    onclick="closeModal('editModal')">
                                Hủy
                            </button>

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
        <script src="${pageContext.request.contextPath}/js/admin-core.js"></script>
        <script src="${pageContext.request.contextPath}/js/admin-category-promotions.js"></script>
    </body>
    </html>


