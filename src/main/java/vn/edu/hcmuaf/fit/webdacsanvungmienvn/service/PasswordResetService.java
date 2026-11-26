package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.PasswordResetDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.EmailUtil;

import java.sql.Timestamp;
import java.util.UUID;

public class PasswordResetService {
    private final PasswordResetDAO passwordResetDAO = new PasswordResetDAO();

    public void sendPasswordResetEmail(int userId, String email) throws Exception {
        sendPasswordResetEmail(userId, email, "http://localhost:8080");
    }

    public void sendPasswordResetEmail(int userId, String email, String baseUrl) throws Exception {
        if (userId <= 0) {
            // Không làm gì nếu email không tồn tại trong hệ thống
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        // Xóa token cũ
        passwordResetDAO.deleteTokenByUserId(userId);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);

        passwordResetDAO.saveToken(userId, token, expiry);

        String normalizedBaseUrl = (baseUrl != null && baseUrl.endsWith("/"))
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
        String link = normalizedBaseUrl + "/password-reset?token=" + token;

        String content =
                "<div style='font-family:Arial,sans-serif;line-height:1.6'>"
                        + "<h2 style='color:#e74c3c'>🔐 Yêu cầu đặt lại mật khẩu</h2>"

                        + "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                        + "<p>Nhấn nút bên dưới để tạo mật khẩu mới:</p>"

                        + "<p style='margin:25px 0;'>"
                        + "<a href='" + link + "' "
                        + "style='display:inline-block;padding:12px 22px;background:#e74c3c;color:#fff;"
                        + "text-decoration:none;border-radius:6px;font-weight:bold;'>"
                        + "ĐẶT LẠI MẬT KHẨU</a></p>"

                        + "<p>⏳ Liên kết này sẽ hết hạn sau <b>15 phút</b> vì lý do bảo mật.</p>"

                        + "<p style='color:#555'>Nếu bạn không yêu cầu thay đổi mật khẩu, "
                        + "hãy bỏ qua email này. Mật khẩu hiện tại của bạn vẫn an toàn.</p>"

                        + "<hr style='margin:25px 0;border:none;border-top:1px solid #ddd'>"
                        + "<p style='font-size:13px;color:#888'>Đây là email tự động, vui lòng không trả lời.</p>"
                        + "</div>";

        EmailUtil.sendEmail(email, "Đặt lại mật khẩu", content);
    }
}
