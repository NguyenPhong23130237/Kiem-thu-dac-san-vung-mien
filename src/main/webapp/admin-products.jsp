<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    // Redirect to controller when opened directly.
    if (request.getAttribute("products") == null && !request.getRequestURI().endsWith("/admin/products")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/products" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản phẩm - Admin</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="<%=request.getContextPath()%>/assets/tinymce/tinymce.min.js"></script>
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
            <li><a href="<%=request.getContextPath()%>/admin/products" class="active"><i class="fas fa-box"></i> <span>Sản phẩm</span></a></li>
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
        <h1>Quản lý Sản phẩm</h1>
        <div class="user-info">
            <span>Xin chào, Admin</span>
            <img src="<%=request.getContextPath()%>/images/user.png" alt="Admin">
        </div>
    </header>

    <div class="admin-content">
        <div class="data-table-container">
            <div class="table-header">
                <h2><i class="fas fa-box"></i> Danh sách sản phẩm</h2>
                <div class="table-actions">
                    <form action="${pageContext.request.contextPath}/admin/products" method="get"
                          style="display:flex; gap:8px;">
                        <input type="text"
                               name="search"
                               class="search-box"
                               placeholder="Tìm kiếm sản phẩm..."
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

            <c:if test="${param.error == 'duplicate'}">
                <div class="alert alert-danger" style="margin-bottom: 16px;">
                    Sản phẩm đã bị trùng!
                </div>
            </c:if>

            <c:if test="${param.error == 'empty_name'}">
                <div class="alert alert-danger">Tên sản phẩm không được để trống.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_input'}">
                <div class="alert alert-danger">Dữ liệu nhập không hợp lệ.</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id'}">
                <div class="alert alert-danger">ID không hợp lệ.</div>
            </c:if>
            <c:if test="${param.error == 'db'}">
                <div class="alert alert-danger">Lỗi lưu dữ liệu. Vui lòng kiểm tra database.</div>
            </c:if>
            <c:if test="${param.error == 'update_failed'}">
                <div class="alert alert-danger">Cập nhật sản phẩm thất bại.</div>
            </c:if>
            <c:if test="${param.error == 'delete_failed'}">
                <div class="alert alert-danger">Xóa sản phẩm thất bại.</div>
            </c:if>
            <c:if test="${param.error == 'empty_specification'}">
                <div class="alert alert-danger">Quy cách không được để trống.</div>
            </c:if>

            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success">Thêm sản phẩm thành công.</div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success">Cập nhật sản phẩm thành công.</div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success">Xóa sản phẩm thành công.</div>
            </c:if>

            <div style="margin: 8px 0; font-size: 12px; color: #666;">
                Tổng sản phẩm: <c:out value="${totalItems}" default="0"/>
            </div>
            <table class="data-table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>ID</th>
                    <th>Hình ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá</th>
                    <th>Thương hiệu</th>
                    <th>Danh mục</th>
                    <th>Vùng miền</th>
                    <th>Tồn kho</th>
                    <th>Đã bán</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty products}">
                        <c:set var="_pageSize" value="${empty pageSize ? fn:length(products) : pageSize}"/>
                        <c:set var="_pageIndex" value="${empty currentPage ? 1 : currentPage}"/>
                        <c:set var="_startIndex" value="${(_pageIndex - 1) * _pageSize}"/>

                        <c:forEach var="p" items="${products}" varStatus="st">
                            <tr>
                                <td><c:out value="${_startIndex + st.index + 1}"/></td>
                                <td><c:out value="${p.id}"/></td>
                                <td>
                                    <img src="${p.image}"
                                         alt="<c:out value='${p.name}'/>">
                                </td>
                                <td><c:out value="${p.name}"/></td>
                                <td><c:out value="${p.format(p.price)}"/>đ</td>
                                <td><c:out value="${p.brandName}"/></td>
                                <td><c:out value="${empty p.categoryName ? p.categoryId : p.categoryName}"/></td>
                                <td><c:out value="${p.region}"/></td>
                                <td><c:out value="${p.quantity}"/></td>
                                <td><c:out value="${p.soldQuantity}"/></td>
                                <td class="actions">
                                    <button type="button" class="btn btn-info btn-sm js-view-product" title="Xem"
                                            data-id="<c:out value='${p.id}'/>">
                                        <i class="fas fa-eye"></i>
                                    </button>

                                    <button type="button" class="btn btn-warning btn-sm js-edit-product" title="Sửa"
                                            data-id="<c:out value='${p.id}'/>"
                                            data-name="<c:out value='${p.name}'/>"
                                            data-price="<c:out value='${p.price}'/>"
                                            data-quantity="<c:out value='${p.quantity}'/>"
                                            data-sold-quantity="<c:out value='${p.soldQuantity}'/>"
                                            data-brand-id="<c:out value='${p.brandId}'/>"
                                            data-category-id="<c:out value='${p.categoryId}'/>"
                                            data-region="<c:out value='${p.region}'/>"
                                            data-specification="<c:out value='${p.specification}'/>"
                                            data-description="<c:out value='${p.description}'/>"
                                            data-image="<c:out value='${p.image}'/>">
                                        <i class="fas fa-edit"></i>
                                    </button>

                                    <form action="<%=request.getContextPath()%>/admin/product/delete" method="post"
                                          style="display:inline;">
                                        <input type="hidden" name="id" value="<c:out value='${p.id}'/>"/>
                                        <button type="submit" class="btn btn-danger btn-sm" title="Xóa"
                                                onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="11" style="text-align: center">Chưa có sản phẩm nào trong database.</td>
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
                    <a class="btn" href="${ctx}/admin/products?page=${cp-1}"><i class="fas fa-chevron-left"></i></a>
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
                            <a class="btn" href="${ctx}/admin/products?page=${i}"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${cp < tp}">
                    <a class="btn" href="${ctx}/admin/products?page=${cp+1}"><i class="fas fa-chevron-right"></i></a>
                </c:if>
                <c:if test="${cp >= tp}">
                    <button disabled><i class="fas fa-chevron-right"></i></button>
                </c:if>
            </div>
        </div>
    </div>
</main>

<!-- View Modal -->
<div class="modal" id="viewModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-eye"></i> Chi tiết sản phẩm</h3>
            <button class="modal-close" onclick="closeModal('viewModal')">&times;</button>
        </div>
        <div class="modal-body">
            <div style="display: flex; gap: 20px;">
                <div style="flex: 0 0 200px;">
                    <img id="viewProductImage" src="" alt="" style="width: 100%; border-radius: 10px;">
                </div>
                <div style="flex: 1;">
                    <h2 id="viewProductName" style="color: #2c3e50; margin-bottom: 15px;"></h2>
                    <p style="margin-bottom: 10px;"><strong>ID:</strong> <span id="viewProductId"></span></p>
                    <p style="margin-bottom: 10px;"><strong>Giá:</strong> <span id="viewProductPrice"
                                                                                style="color: #e74c3c; font-size: 18px; font-weight: bold;"></span>
                    </p>
                    <p style="margin-bottom: 10px;"><strong>Thương hiệu:</strong> <span id="viewProductBrand"></span>
                    </p>
                    <p style="margin-bottom: 10px;"><strong>Danh mục:</strong> <span id="viewProductCategory"></span>
                    </p>
                    <p style="margin-bottom: 10px;"><strong>Vùng miền:</strong> <span id="viewProductRegion"></span></p>
                    <p style="margin-bottom: 10px;"><strong>Quy cách:</strong> <span id="viewProductSpec"></span></p>
                    <p style="margin-bottom: 10px;"><strong>Tồn kho:</strong> <span id="viewProductQuantity"
                                                                                    class="badge badge-success"></span>
                    </p>
                    <p style="margin-bottom: 10px;"><strong>Đã bán:</strong> <span id="viewProductSold"></span></p>
                    <p style="margin-bottom: 10px;"><strong>Ngày tạo:</strong> <span id="viewProductCreated"></span></p>
                </div>
            </div>
            <div style="margin-top: 20px;">
                <h4 style="color: #2c3e50; margin-bottom: 10px;">Mô tả sản phẩm</h4>
                <p id="viewProductDescription" style="color: #666; line-height: 1.6;"></p>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn" onclick="closeModal('viewModal')">Đóng</button>
            <button class="btn btn-warning" onclick="closeModal('viewModal'); openModal('editModal');">
                <i class="fas fa-edit"></i> Chỉnh sửa
            </button>
        </div>
    </div>
</div>

<!-- Add Modal -->
<div class="modal" id="addModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-plus-circle"></i> Thêm sản phẩm mới</h3>
            <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="addProductForm" action="<%=request.getContextPath()%>/admin/product/add" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label>Hình ảnh sản phẩm
                        <span id="bannerImageError" style="display:none;margin-left:8px;color:#dc3545;font-size:13px;">Cần thêm ảnh mới có thể đăng lên</span>
                    </label>
                    <div class="file-upload" onclick="document.getElementById('addProductImage').click()">
                        <i class="fas fa-cloud-upload-alt"></i>
                        <p>Click để tải ảnh lên</p>
                        <input type="file" id="addProductImage" name="thumbnailImage" accept="image/*" required>
                        <img class="image-preview" src="" style="display:block; max-width:100%; margin-top:10px;"
                             alt="Preview"/>
                    </div>
                </div>
                <div class="form-group">
                    <label>Tên sản phẩm *</label>
                    <input type="text" name="name" required placeholder="Nhập tên sản phẩm">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Giá (VNĐ) *</label>
                        <input type="number" name="price" required placeholder="Nhập giá">
                    </div>
                    <div class="form-group">
                        <label>Số lượng *</label>
                        <input type="number" name="quantity" required placeholder="Nhập số lượng">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Thương hiệu</label>
                        <select name="brandId" required>
                            <option value="">-- Chọn thương hiệu --</option>
                            <c:forEach var="b" items="${brands}">
                                <option value="<c:out value='${b.id}'/>"><c:out value="${b.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Danh mục *</label>
                        <select name="categoryId" required>
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="<c:out value='${cat.id}'/>"><c:out value="${cat.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Vùng miền *</label>
                        <select name="region" required>
                            <option value="">-- Chọn vùng miền --</option>
                            <option value="Miền Bắc">Miền Bắc</option>
                            <option value="Miền Trung">Miền Trung</option>
                            <option value="Miền Nam">Miền Nam</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Quy cách *</label>
                        <input type="text" name="specification" placeholder="VD: 500g, 1kg, Hộp 10 cái..." required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Mô tả sản phẩm</label>
                    <textarea name="description" placeholder="Nhập mô tả chi tiết sản phẩm..."></textarea>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn" onclick="closeModal('addModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Lưu sản phẩm
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
            <h3><i class="fas fa-edit"></i> Sửa sản phẩm</h3>
            <button class="modal-close" onclick="closeModal('editModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="editProductForm" action="<%=request.getContextPath()%>/admin/product/update" method="post"
                  enctype="multipart/form-data">
                <input type="hidden" name="id" id="editProductId"/>
                <input type="hidden" name="currentPage" value="${param.page != null ? param.page : 1}">
                <div class="form-group">
                    <label>Hình ảnh sản phẩm
                        <span id="bannerImageError" style="display:none;margin-left:8px;color:#dc3545;font-size:13px;">Cần thêm ảnh mới có thể đăng lên</span>
                    </label>
                    <div class="file-upload" onclick="document.getElementById('editProductImageFile').click()">
                        <i class="fas fa-cloud-upload-alt"></i>
                        <p>Click để tải ảnh lên</p>
                        <input type="file" id="editProductImageFile" name="thumbnailImage" accept="image/*">
                        <img class="image-preview" id="editProductImage" src=""
                             style="display:block; max-width:100%; margin-top:10px;"
                             alt="Preview"/>
                    </div>
                </div>
                <div class="form-group">
                    <label>Tên sản phẩm *</label>
                    <input type="text" name="name" id="editProductName" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Giá (VNĐ) *</label>
                        <input type="number" name="price" id="editProductPrice" required>
                    </div>
                    <div class="form-group">
                        <label>Số lượng *</label>
                        <input type="number" name="quantity" id="editProductQuantity" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Đã bán</label>
                    <input type="number" name="soldQuantity" id="editProductSoldQuantity" min="0" value="0">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Thương hiệu</label>
                        <select name="brandId" id="editProductBrandId" required>
                            <option value="">-- Chọn thương hiệu --</option>
                            <c:forEach var="b" items="${brands}">
                                <option value="<c:out value='${b.id}'/>"><c:out value="${b.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Danh mục *</label>
                        <select name="categoryId" id="editProductCategoryId" required>
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="<c:out value='${cat.id}'/>"><c:out value="${cat.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Vùng miền *</label>
                        <select name="region" id="editProductRegion" required>
                            <option value="">-- Chọn vùng miền --</option>
                            <option value="Miền Bắc">Miền Bắc</option>
                            <option value="Miền Trung">Miền Trung</option>
                            <option value="Miền Nam">Miền Nam</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Quy cách *</label>
                        <input type="text" name="specification" id="editProductSpecification" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Mô tả sản phẩm</label>
                    <textarea name="description" id="editProductDescription"></textarea>
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
    tinymce.init({
        selector: 'textarea',
        license_key: 'gpl',
        base_url: '<%=request.getContextPath()%>/assets/tinymce',
        plugins: 'image link lists table code',
        toolbar: 'undo redo | blocks fontfamily fontsize | bold italic | alignleft aligncenter alignright | bullist numlist | image link | code',

        images_upload_url: '<c:url value="/rich-text-upload-image?type=product"/>',
        automatic_uploads: true,
        file_picker_types: 'image'
    });
</script>
<script>
    window.APP_CTX = '<%= request.getContextPath() %>';
</script>
<script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
<script src="<%=request.getContextPath()%>/js/admin-products.js"></script>
</body>
</html>
