package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import com.google.api.client.googleapis.auth.oauth2.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "GoogleLogin", value = "/google-login")
public class GoogleLogin extends HttpServlet {
    private static final String CLIENT_ID = "32963302137-fcnm4s7oif6o8bco634lc24v813lfgks.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/WebDacSanVungMienVN/google-callback";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String googleAuthUrl =
                "https://accounts.google.com/o/oauth2/v2/auth?" +
                        "client_id=" + CLIENT_ID +
                        "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                        "&response_type=code" +
                        "&scope=openid%20email%20profile" +
                        "&access_type=offline" +
                        "&prompt=consent";

        response.sendRedirect(googleAuthUrl);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}