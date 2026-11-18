<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    if (request.getAttribute("users") == null && !request.getRequestURI().endsWith("/admin/users")) {
        String qs = request.getQueryString();
        response.sendRedirect(request.getContextPath() + "/admin/users" + (qs != null ? ("?" + qs) : ""));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Người dùng - Admin</title>
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
                <li><a href="<%=request.getContextPath()%>/admin/users" class="active"><i class="fas fa-users"></i> <span>Người dùng</span></a></li>
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
            <h1>Quản lý Người dùng</h1>
            <div class="user-info">
                <span>Xin chào, Admin</span>
            </div>
        </header>

        <div class="admin-content">
            <div class="data-table-container">
                <div class="table-header">
                    <h2><i class="fas fa-users"></i> Danh sách người dùng</h2>
                    <div class="table-actions">
                        <form action="${pageContext.request.contextPath}/admin/users" method="get" style="display:flex; gap:8px;">
                            <input type="text"
                                   name="search"
                                   class="search-box"
                                   placeholder="Tìm kiếm danh mục..."
                                   value="${param.search}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                        <button type="button" class="btn btn-primary" onclick="openModal('addModal')">
                            <i class="fas fa-plus"></i> Thêm mới
                        </button>
                    </div>
                </div>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>ID</th>
                            <th>Họ tên</th>
                            <th>Email</th>
                            <th>SĐT</th>
                            <th>Giới tính</th>
                            <th>Ngày sinh</th>
                            <th>Vai trò</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="cp" value="${currentPage != null ? currentPage : 1}" />
                        <c:set var="ps" value="${pageSize != null ? pageSize : 10}" />
                        <c:choose>
                            <c:when test="${not empty users}">
                                <c:forEach var="u" items="${users}" varStatus="loop">
                                    <tr>
                                        <td><c:out value="${(cp - 1) * ps + loop.index + 1}"/></td>
                                        <td><c:out value="${u.id}"/></td>
                                        <td><c:out value="${u.fullName}"/></td>
                                        <td><c:out value="${u.email}"/></td>
                                        <td><c:out value="${u.phoneNumber}"/></td>
                                        <td><c:out value="${u.gender}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty u.dateOfBirth}">
                                                    <fmt:formatDate value="${u.dateOfBirth}" pattern="yyyy-MM-dd"/>
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${u.role == 'admin'}">
                                                    <span class="badge badge-danger">Admin</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-info"><c:out value="${u.role}"/></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="actions">
                                            <button type="button" class="btn btn-info btn-sm btn-view-user"
                                                    title="Xem"
                                                    onclick="showUserViewButton(this)"
                                                    data-user-id="${u.id}"
                                                    data-full-name="${fn:escapeXml(u.fullName)}"
                                                    data-email="${fn:escapeXml(u.email)}"
                                                    data-phone-number="${fn:escapeXml(u.phoneNumber)}"
                                                    data-gender="${fn:escapeXml(u.gender)}"
                                                    data-date-of-birth="<fmt:formatDate value='${u.dateOfBirth}' pattern='yyyy-MM-dd'/>"
                                                    data-role="${u.role}">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <button type="button" class="btn btn-warning btn-sm btn-edit-user" title="Sửa"
                                                    onclick="showUserEditButton(this)"
                                                    data-user-id="${u.id}"
                                                    data-full-name="${fn:escapeXml(u.fullName)}"
                                                    data-email="${fn:escapeXml(u.email)}"
                                                    data-phone-number="${fn:escapeXml(u.phoneNumber)}"
                                                    data-gender="${fn:escapeXml(u.gender)}"
                                                    data-date-of-birth="<fmt:formatDate value='${u.dateOfBirth}' pattern='yyyy-MM-dd'/>"
                                                    data-role="${u.role}">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <form action="${pageContext.request.contextPath}/DeleteUserServlet" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">
                                                <input type="hidden" name="id" value="${u.id}" />
                                                <button type="submit" class="btn btn-danger btn-sm" title="Xóa"><i class="fas fa-trash"></i></button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" style="text-align:center;">Không có người dùng nào.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <div class="pagination">
                    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
                    <c:set var="cp" value="${currentPage}"/>
                    <c:set var="tp" value="${totalPages}"/>
                    <c:set var="searchParam" value="${search != null && search != '' ? '&search=' + search : ''}" />

                    <c:if test="${cp > 1}">
                        <a class="btn" href="${ctx}/admin/users?page=${cp-1}${searchParam}"><i class="fas fa-chevron-left"></i></a>
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
                                <a class="btn" href="${ctx}/admin/users?page=${i}${searchParam}"><c:out value="${i}"/></a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${cp < tp}">
                        <a class="btn" href="${ctx}/admin/users?page=${cp+1}${searchParam}"><i class="fas fa-chevron-right"></i></a>
                    </c:if>
                    <c:if test="${cp >= tp}">
                        <button disabled><i class="fas fa-chevron-right"></i></button>
                    </c:if>
                </div>
            </div>
        </div>
    </main>

    <!-- View Modal -->
    <div class="modal" id="userViewModal">
        <div class="modal-content" style="max-width: 600px;">
            <div class="modal-header">
                <h3><i class="fas fa-eye"></i> Chi tiết người dùng</h3>
                <button class="modal-close" onclick="closeModal('userViewModal')">&times;</button>
            </div>
            <div class="modal-body">
                <table style="width: 100%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee; width: 40%;"><strong>ID:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Họ tên:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Email:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Số điện thoại:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Giới tính:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Ngày sinh:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><strong>Vai trò:</strong></td>
                        <td style="padding: 10px; border-bottom: 1px solid #eee;"><span class="badge badge-info">User</span></td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn" onclick="closeModal('userViewModal')">Đóng</button>
                <button class="btn btn-warning" onclick="closeModal('userViewModal'); openModal('userEditModal');">
                    <i class="fas fa-edit"></i> Chỉnh sửa
                </button>
            </div>
        </div>
    </div>
    <!-- Add Modal -->
    <div class="modal" id="addModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3><i class="fas fa-plus-circle"></i> Thêm người dùng mới</h3>
                <button class="modal-close" onclick="closeModal('addModal')">&times;</button>
            </div>
            <div class="modal-body">
                <c:if test="${not empty addError}">
                    <div style="color: red; margin-bottom: 10px; font-weight: bold;">${fn:escapeXml(addError)}</div>
                </c:if>
                <form id="addUserForm" action="${pageContext.request.contextPath}/AddUserServlet" method="post">
                    <div class="form-group">
                        <label>Họ tên *</label>
                        <input type="text" name="full_name" required placeholder="Nhập họ tên" value="${fn:escapeXml(addFullName)}">
                    </div>
                    <div class="form-group">
                        <label>Email *</label>
                        <input type="email" name="email" required placeholder="Nhập email" value="${fn:escapeXml(addEmail)}">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu *</label>
                        <input type="password" name="password" required placeholder="Nhập mật khẩu">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="tel" name="phone_number" placeholder="Nhập SĐT" value="${fn:escapeXml(addPhoneNumber)}">
                        </div>
                        <div class="form-group">
                            <label>Ngày sinh</label>
                            <input type="date" name="date_of_birth" value="${addDateOfBirth}">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Giới tính</label>
                            <select name="gender">
                                <option value="Không xác định" ${addGender=='Không xác định' ? 'selected' : ''}>Không xác định</option>
                                <option value="Nam" ${addGender=='Nam' ? 'selected' : ''}>Nam</option>
                                <option value="Nữ" ${addGender=='Nữ' ? 'selected' : ''}>Nữ</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Vai trò *</label>
                            <select name="role" required>
                                <option value="user" ${addRole=='user' ? 'selected' : ''}>User</option>
                                <option value="admin" ${addRole=='admin' ? 'selected' : ''}>Admin</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn" onclick="closeModal('addModal')">Hủy</button>
                <button type="submit" form="addUserForm" class="btn btn-primary">
                    <i class="fas fa-save"></i> Lưu
                </button>
            </div>
        </div>
    </div>

    <!-- Edit Modal -->
    <div class="modal" id="userEditModal">
        <div class="modal-content">
            <div class="modal-header">
                <h3><i class="fas fa-edit"></i> Sửa thông tin người dùng</h3>
                <button class="modal-close" onclick="closeModal('userEditModal')">&times;</button>
            </div>
            <div class="modal-body">
                <c:if test="${errorDuplicateProduct}">
                    <div style="color: red; margin-bottom: 10px; font-weight: bold;" data-error-duplicate-product>Email này đã được tạo. Vui lòng sử dụng email khác!</div>
                </c:if>
                <form id="editUserForm" action="${pageContext.request.contextPath}/UpdateUserServlet" method="post">
                    <input type="hidden" name="id" id="edit_id" value="<c:out value='${editUser.id}'/>">
                    <div class="form-group">
                        <label>Họ tên *</label>
                        <input type="text" name="full_name" id="edit_full_name" required value="<c:out value='${editUser.fullName}'/>">
                    </div>
                    <div class="form-group">
                        <label>Email *</label>
                        <input type="email" name="email" id="edit_email" required value="<c:out value='${editUser.email}'/>">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu mới (để trống nếu không đổi)</label>
                        <input type="password" name="password" id="edit_password" placeholder="Nhập mật khẩu mới">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="tel" name="phone_number" id="edit_phone_number" value="<c:out value='${editUser.phoneNumber}'/>">
                        </div>
                        <div class="form-group">
                            <label>Ngày sinh</label>
                            <input type="date" name="date_of_birth" id="edit_date_of_birth" value="<fmt:formatDate value='${editUser.dateOfBirth}' pattern='yyyy-MM-dd'/>">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Giới tính</label>
                            <select name="gender" id="edit_gender">
                                <option value="Không xác định" <c:if test="${editUser.gender=='Không xác định'}">selected</c:if>>Không xác định</option>
                                <option value="Nam" <c:if test="${editUser.gender=='Nam'}">selected</c:if>>Nam</option>
                                <option value="Nữ" <c:if test="${editUser.gender=='Nữ'}">selected</c:if>>Nữ</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Vai trò *</label>
                            <select name="role" id="edit_role" required>
                                <option value="user" <c:if test="${editUser.role=='user'}">selected</c:if>>User</option>
                                <option value="admin" <c:if test="${editUser.role=='admin'}">selected</c:if>>Admin</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn" onclick="closeModal('userEditModal')">Hủy</button>
                <button type="submit" form="editUserForm" class="btn btn-primary">
                    <i class="fas fa-save"></i> Cập nhật
                </button>
            </div>
        </div>
    </div>
    <script>
        window.APP_CTX = '<%= request.getContextPath() %>';
    </script>
    <script src="<%=request.getContextPath()%>/js/admin-core.js"></script>
    <script src="<%=request.getContextPath()%>/js/admin-users.js"></script>
</body>
</html>

