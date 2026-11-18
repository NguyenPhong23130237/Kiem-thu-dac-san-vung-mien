package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.productDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.RatingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Review;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ReviewService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.PaginationUtil;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/product-detail")
public class ProductDetailServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        Product product = productService.getProductById(productId);
        if (product == null) {
            response.sendRedirect("home");
            return;
        }
        request.setAttribute("product", product);

        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        request.setAttribute("page", page);

        List<Review> reviews = reviewService.getReviewsByProductId(productId, 5, page);
        RatingInfo ratingInfo = reviewService.getRatingInfoByProductId(productId);
        int totalReviews = reviewService.countReviewsByProductId(productId);
        int totalPages = PaginationUtil.calculateTotalPages(totalReviews, 5);
        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("error", error);
        }
        request.setAttribute("reviews", reviews);
        request.setAttribute("ratingInfo", ratingInfo);
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("product-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}