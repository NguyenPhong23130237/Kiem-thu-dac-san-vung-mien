package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.VerificationDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.EmailUtil;

import java.sql.Timestamp;
import java.util.UUID;

public class VerificationService {
    private final VerificationDAO vDAO = new VerificationDAO();

    public static String buildBaseUrl(HttpServletRequest request) {
        return EmailUtil.buildBaseUrl(request);
    }

    public void sendVerificationEmail(int userId, String email) throws Exception {
        sendVerificationEmail(userId, email, "http://localhost:8080");
    }

    public void sendVerificationEmail(int userId, String email, String baseUrl) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId for verification email: " + userId);
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        // Xóa token cũ
        vDAO.deleteTokenByUserId(userId);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        Timestamp expiry = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);

        vDAO.saveToken(userId, token, expiry);

        String normalizedBaseUrl = (baseUrl != null && baseUrl.endsWith("/"))
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
        String link = normalizedBaseUrl + "/verify-email?token=" + token;

        String content =
                "<div style='font-family:Arial,sans-serif;line-height:1.6'>"
                        + "<h2 style='color:#2ecc71'>📩 Xác minh địa chỉ email</h2>"

                        + "<p>Cảm ơn bạn đã đăng ký tài khoản.</p>"
                        + "<p>Vui lòng xác minh email để kích hoạt tài khoản của bạn:</p>"

                        + "<p style='margin:25px 0;'>"
                        + "<a href='" + link + "' "
                        + "style='display:inline-block;padding:12px 22px;background:#2ecc71;color:#fff;"
                        + "text-decoration:none;border-radius:6px;font-weight:bold;'>"
                        + "XÁC MINH TÀI KHOẢN</a></p>"

                        + "<p>⏳ Liên kết xác minh sẽ hết hạn sau <b>15 phút</b>.</p>"

                        + "<p style='color:#555'>Nếu bạn không tạo tài khoản này, "
                        + "bạn có thể bỏ qua email.</p>"

                        + "<hr style='margin:25px 0;border:none;border-top:1px solid #ddd'>"
                        + "<p style='font-size:13px;color:#888'>Đây là email tự động, vui lòng không trả lời.</p>"
                        + "</div>";

        EmailUtil.sendEmail(email, "Xác minh tài khoản", content);
    }
}