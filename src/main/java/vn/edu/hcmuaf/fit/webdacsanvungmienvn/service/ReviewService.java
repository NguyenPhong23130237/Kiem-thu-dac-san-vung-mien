package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.OrderDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ReviewDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.RatingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Review;

import java.util.List;

public class ReviewService {
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public List<Review> getReviewsByProductId(int productId, int limit, int page) {
        return reviewDAO.getReviewsByProductId(productId, limit, page);
    }

    public int countReviewsByProductId(int productId) {
        return reviewDAO.countReviewsByProductId(productId);
    }

    public void addReview(Review review) {
        int productId = review.getProductId();
        int userId = review.getUserId();
        String content = review.getContent();
        int rating = review.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Đánh giá phải từ 1 đến 5 sao");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Đánh giá không được để trống");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Đánh giá không được vượt quá 1000 ký tự");
        }
        if (!orderDAO.hasUserOrderedProduct(userId, productId)) {
            throw new IllegalArgumentException("Chỉ có thể đánh giá sản phẩm đã mua");
        }
        if (reviewDAO.hasUserReviewedProduct(userId, productId)) {
            throw new IllegalArgumentException("Người dùng đã đánh giá sản phẩm này");
        }
        reviewDAO.addReview(review);
    }

    public RatingInfo getRatingInfoByProductId(int productId) {
        double averageRating = reviewDAO.getAverageRatingByProductId(productId);
        return new RatingInfo(averageRating);
    }
}
