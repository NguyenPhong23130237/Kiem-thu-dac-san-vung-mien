<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác thực đơn hàng</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/order-sign.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<main>
    <div class="back-nav-wrapper">
        <div class="back-nav">
            <a href="order-detail?orderId=${orderId}" class="back-link">
                <i class="fa fa-arrow-left"></i>
                <span>Đơn hàng #${orderId}</span>
            </a>
        </div>
    </div>

    <section class="compact-sign">
        <div class="compact-card">
            <label class="card-label">Mã hash đơn hàng (SHA-256)</label>
            <div class="hash-compact">
                <code class="order-hash">${orderHash}</code>
                <div class="payload-actions">
                    <button type="button" class="ghost-button"
                            onclick="navigator.clipboard && navigator.clipboard.writeText(document.querySelector('.order-hash').innerText)">
                        Copy
                    </button>
                </div>
            </div>
        </div>

        <div class="compact-card">
            <label class="card-label">Khóa đang active</label>
            <div class="simple-key">
                <c:choose>
                    <c:when test="${keyRevoked}">
                        <div class="no-key-alert">
                            <p class="no-key-text">
                                <i class="fa-solid fa-triangle-exclamation"></i>
                                Khóa hiện tại đã bị lộ hoặc không còn an toàn.
                                Bạn phải tạo khóa mới trước khi xác thực đơn hàng.
                            </p>

                            <a href="generateKey" class="go-to-genkey-btn">
                                <i class="fa-solid fa-key"></i>
                                Tạo khóa mới
                            </a>
                        </div>
                    </c:when>
                    <c:when test="${not empty activeKey}">
                        <p><strong>Key ID:</strong> ${activeKey.id}</p>
                        <p><strong>Algorithm:</strong> ${activeKey.algorithm}</p>
                        <p><strong>Created at:</strong> ${activeKey.createdAt}</p>
                        <p><strong>Khóa công cộng (Public Key):</strong></p>
                        <p>${fn:length(activeKey.publicKey) > 20 ? fn:substring(activeKey.publicKey, 0, 20).concat('...')
                                : activeKey.publicKey}
                        </p>
                    </c:when>
                    <c:otherwise>
                        <div class="no-key-alert">
                            <p class="no-key-text">
                                <i class="fa-solid fa-triangle-exclamation"></i>
                                Hiện tại bạn chưa có khóa nào active để xác thực đơn hàng.
                            </p>
                            <a href="generateKey" class="go-to-genkey-btn">
                                <i class="fa-solid fa-key"></i> Tạo khóa mới ngay
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <c:if test="${not keyRevoked}">
            <div class="compact-card">
                <form id="signForm" class="sign-inline" action="#" method="post" enctype="multipart/form-data">
                    <label class="card-label">Cung cấp Chữ ký số (Signature)</label>

                    <div id="pasteSection" class="input-section">
                        <textarea id="signatureText" name="signatureText" rows="7" placeholder="Dán nội dung chữ ký số của bạn vào đây..."></textarea>
                    </div>

                    <input type="hidden" name="orderId" value="${empty param.orderId ? '7' : param.orderId}"/>
                    <input type="hidden" name="signatureAlgorithm" value="${empty requestScope.signatureAlgorithm ? 'SHA256withDSA' : requestScope.signatureAlgorithm}">
                </form>
            </div>
        </c:if>
        <c:if test="${not keyRevoked}">
            <div class="compact-card text-center" style="border: none; background: transparent; padding: 0; box-shadow: none;">
                <button id="signBtn" type="button" class="primary-button">Xác thực đơn hàng</button>
            </div>
        </c:if>

        <div id="resultAlert" class="result-alert" role="alert" aria-live="polite"></div>
    </section>

    <script>
        (function () {
            const form = document.getElementById('signForm');
            const btn = document.getElementById('signBtn');
            const alertBox = document.getElementById('resultAlert');

            function showAlert(message, type) {
                alertBox.textContent = message;
                alertBox.classList.remove('success', 'error', 'visible');
                if (type) alertBox.classList.add(type);
                alertBox.classList.add('visible');
            }

            function hideAlert() {
                alertBox.textContent = '';
                alertBox.classList.remove('success', 'error', 'visible');
            }

            btn.addEventListener('click', function () {
                hideAlert();
                const textVal = document.getElementById('signatureText').value.trim();
                if (!textVal) {
                    showAlert('Vui lòng dán nội dung chữ ký số vào ô trống.', 'error');
                    return;
                }

                const fd = new FormData(form);
                btn.disabled = true;
                const prevText = btn.textContent;
                btn.textContent = 'Đang xác thực...';

                fetch('order-sign', {
                    method: 'POST',
                    body: fd,
                    credentials: 'same-origin'
                }).then(async res => {
                    btn.disabled = false;
                    btn.textContent = prevText;
                    if (!res.ok) {
                        const txt = await res.text().catch(() => res.statusText);
                        showAlert('Lỗi mạng: ' + (txt || res.statusText), 'error');
                        return;
                    }
                    const data = await res.json().catch(() => null);
                    if (data && data.success) {
                        if (data.redirect) {
                            window.location.href = data.redirect;
                        } else {
                            const orderId = fd.get('orderId') || '${empty param.orderId ? '7' : param.orderId}';
                            window.location.href = 'order-success?orderId=' + encodeURIComponent(orderId);
                        }
                    } else {
                        const msg = data && data.message ? data.message : 'Xác thực thất bại. Vui lòng kiểm tra private key.';
                        showAlert(msg, 'error');
                    }
                }).catch(err => {
                    btn.disabled = false;
                    btn.textContent = prevText;
                    showAlert('Lỗi khi gửi yêu cầu: ' + (err && err.message ? err.message : String(err)), 'error');
                });
            });
        })();
    </script>
</main>

<%@ include file="footer.jsp" %>
</body>
</html>
