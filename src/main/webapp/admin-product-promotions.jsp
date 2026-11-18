<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (request.getAttribute("discounts") == null
            && !request.getRequestURI().endsWith("/admin/product-discounts")) {
        response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Khuyến mãi Sản phẩm - Admin</title>
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
                <li><a href="<%=request.getContextPath()%>/admin/product-discounts" class="active"><i class="fas fa-percent"></i> <span>KM Sản phẩm</span></a></li>
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
            <h1>Quản lý Khuyến mãi Sản phẩm</h1>
            <div class="user-info">
                <span>Xin chào, Admin</span>
                <img src="<%=request.getContextPath()%>/images/user.png" alt="Admin">
            </div>
        </header>

        <div class="admin-content">
            <div class="data-table-container">
                <div class="table-header">
                    <h2><i class="fas fa-percent"></i> Danh sách khuyến mãi sản phẩm</h2>
                    <div class="table-actions">
                        <form action="${pageContext.request.contextPath}/admin/product-discounts" method="get" style="display:flex; gap:8px;">
                            <input type="text"
                                   name="search"
                                   class="search-box"
                                   placeholder="Tìm kiếm khuyến mãi sản phẩm..."
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
                        <th>Sản phẩm</th>
                        <th>Giảm (%)</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
                        <c:set var="ps" value="${pageSize != null ? pageSize : 10}" />
                        <c:if test="${not empty discounts}">
                            <c:forEach var="discount" items="${discounts}" varStatus="loop">
                                <tr>
                                    <td>${(cp - 1) * ps + loop.index + 1}</td>
                                    <td>${discount.id}</td>
                                    <td>${discount.productName}</td>
                                    <td><span class="badge badge-success">${discount.discountPercentage}%</span></td>
                                    <td><fmt:formatDate value="${discount.startDate}" pattern="dd/MM/yyyy"/></td>
                                    <td><fmt:formatDate value="${discount.endDate}" pattern="dd/MM/yyyy"/></td>
                                    <td class="discount-status-cell"
                                        data-discount-start="${discount.startDate}"
                                        data-discount-end="${discount.endDate}">
                                    </td>
                                    <td class="actions">
                                        <a href="#" class="btn btn-warning btn-sm js-edit-discount"
                                           data-id="${discount.id}"
                                           data-product="${discount.productId}"
                                           data-discount="${discount.discountPercentage}"
                                           data-start="<fmt:formatDate value='${discount.startDate}' pattern='yyyy-MM-dd'/>"
                                           data-end="<fmt:formatDate value='${discount.endDate}' pattern='yyyy-MM-dd'/>"
                                           title="Sửa"><i class="fas fa-edit"></i></a>
                                        <a href="${pageContext.request.contextPath}/admin/product-discounts?action=delete&id=${discount.id}" class="btn btn-danger btn-sm" title="Xóa" onclick="return confirm('Bạn có chắc chắn muốn xóa khuyến mãi này?');"><i class="fas fa-trash"></i></a>
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
                    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
                    <c:set var="cp" value="${currentPage}"/>
                    <c:set var="tp" value="${totalPages}"/>
                    <c:if test="${cp > 1}">
                        <a class="btn" href="${ctx}/admin/product-discounts?page=${cp-1}"><i class="fas fa-chevron-left"></i></a>
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
                                <a class="btn" href="${ctx}/admin/product-discounts?page=${i}"><c:out value="${i}"/></a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${cp < tp}">
                        <a class="btn" href="${ctx}/admin/product-discounts?page=${cp+1}"><i class="fas fa-chevron-right"></i></a>
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
                <h3><i class="fas fa-plus-circle"></i> Thêm khuyến mãi sản phẩm</h3>
                <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
            </div>
            <div class="modal-body">
                <c:if test="${errorDuplicateProduct}">
                    <div style="color: red; margin-bottom: 10px; font-weight: bold;" data-error-duplicate-product>Sản phẩm này đã có khuyến mãi. Vui lòng chọn sản phẩm khác!</div>
                </c:if>
                <form id="addDiscountForm" action="${pageContext.request.contextPath}/admin/product-discounts" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="form-group">
                        <label>Sản phẩm *</label>
                        <select name="product" required>
                            <option value="">-- Chọn sản phẩm --</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}" <c:if test="${formProduct == product.id}">selected</c:if>>${product.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Phần trăm giảm giá (%) *</label>
                        <input type="number" name="discount_percentage" required min="1" max="100" placeholder="Nhập phần trăm giảm giá" value="${formDiscount != null ? formDiscount : ''}">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Ngày bắt đầu *</label>
                            <input type="date" name="start_date" required value="${formStart != null ? formStart : ''}">
                        </div>
                        <div class="form-group">
                            <label>Ngày kết thúc *</label>
                            <input type="date" name="end_date" required value="${formEnd != null ? formEnd : ''}">
                        </div>
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
                <h3><i class="fas fa-edit"></i> Sửa khuyến mãi sản phẩm</h3>
                <button class="modal-close" onclick="closeModal('editModal')">&times;</button>
            </div>
            <div class="modal-body">
                <form id="editDiscountForm" action="${pageContext.request.contextPath}/admin/product-discounts" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${editDiscount.id}">
                    <div class="form-group">
                        <label>Sản phẩm *</label>
                        <select name="product" required>
                            <option value="">-- Chọn sản phẩm --</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}" <c:if test="${product.id == editDiscount.productId}">selected</c:if>>${product.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Phần trăm giảm giá (%) *</label>
                        <input type="number" name="discount_percentage" required min="1" max="100" value="${editDiscount.discountPercentage}">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Ngày bắt đầu *</label>
                            <input type="date" name="start_date" required value="${editDiscount.startDate}">
                        </div>
                        <div class="form-group">
                            <label>Ngày kết thúc *</label>
                            <input type="date" name="end_date" required value="${editDiscount.endDate}">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/admin/product-discounts" class="btn">Hủy</a>
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
    <script src="<%=request.getContextPath()%>/js/admin-product-promotions.js"></script>
</body>
</html>

