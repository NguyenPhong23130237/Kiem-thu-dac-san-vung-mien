package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.util.Collections;

@WebServlet(name = "GoogleCallback", value = "/google-callback")
public class GoogleCallback extends HttpServlet {
    private static final String CLIENT_ID = "32963302137-fcnm4s7oif6o8bco634lc24v813lfgks.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    private static final String REDIRECT_URI = "http://localhost:8080/WebDacSanVungMienVN/google-callback";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect("login?error=google_no_code");
            return;
        }

        try {
            // Đổi authorization code → token
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            GsonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            CLIENT_ID,
                            CLIENT_SECRET,
                            code,
                            REDIRECT_URI
                    ).execute();

            // Lấy ID Token (chứa thông tin user)
            String idTokenString = tokenResponse.getIdToken();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                response.sendRedirect("login?error=google_invalid_token");
                return;
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String googleId = payload.getSubject();     // ID duy nhất của Google
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Kiểm tra DB
            UserDAO dao = new UserDAO();
            User user = dao.findByGoogleId(googleId);

            if (user == null) {
                User emailUser = dao.findByEmail(email);

                if (emailUser == null) {
                    dao.insertGoogleUser(email, name, googleId);
                } else {
                    // Đã có email trong hệ thống
                    if (emailUser.getGoogleId() == null) {
                        // Chưa liên kết Google → cho link
                        dao.linkGoogleAccount(emailUser.getId(), googleId);
                    }
                    // Nếu đã có google_id rồi thì không làm gì (đã link trước đó)
                }
                // QUAN TRỌNG: load lại user sau khi DB thay đổi
                user = dao.findByGoogleId(googleId);
                if (user == null) {
                    response.sendRedirect("login?error=google_db_error");
                    return;
                }
            }

            // Tạo session đăng nhập
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect("home");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login?error=google_exception");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}