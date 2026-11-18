package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.productDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Review;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ReviewService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AddReview", value = "/add-review")
public class AddReview extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        int productId = Integer.parseInt(request.getParameter("productId"));
        int userId = user.getId();
        String content = request.getParameter("content");
        int rating = Integer.parseInt(request.getParameter("rating"));
        try {
            Review review = new Review();
            review.setProductId(productId);
            review.setUserId(userId);
            review.setContent(content);
            review.setRating(rating);
            reviewService.addReview(review);
            response.sendRedirect("product-detail?productId=" + productId);
        } catch (IllegalArgumentException e) {
            response.sendRedirect("product-detail?productId=" + productId + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}