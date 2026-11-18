package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.News;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    public List<News> getAllNews() {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM news ORDER BY published_date DESC";

        System.out.println("=== DEBUG getAllNews ===");
        System.out.println("SQL: " + sql);

        try (Connection conn = DBConnect.getConnection()) {
            System.out.println("Connection obtained successfully");

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("Query executed, processing results...");

                while (rs.next()) {
                    News news = new News();
                    news.setId(rs.getInt("id"));
                    news.setTitle(rs.getString("title"));
                    news.setAuthor(rs.getString("author"));
                    news.setPublishedDate(rs.getDate("published_date"));
                    news.setContent(rs.getString("content"));
                    news.setImage(rs.getString("image"));
                    list.add(news);

                    System.out.println("Added news: " + news.getId() + " - " + news.getTitle());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in getAllNews: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total news retrieved: " + list.size());
        return list;
    }
    public List<News> getLatestNews(int limit) {
        List<News> list = new ArrayList<>();
        String sql = "SELECT * FROM news ORDER BY published_date DESC LIMIT ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setAuthor(rs.getString("author"));
                news.setPublishedDate(rs.getDate("published_date"));
                news.setContent(rs.getString("content"));
                news.setImage(rs.getString("image"));
                list.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public News getNewsById(int id) {
        News news = null;
        String sql = "SELECT * FROM news WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setAuthor(rs.getString("author"));
                news.setPublishedDate(rs.getDate("published_date"));
                news.setContent(rs.getString("content"));
                news.setImage(rs.getString("image"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public boolean addNews(News news) {
        String sql = "INSERT INTO news (title, author, published_date, content, image) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, news.getTitle());
            ps.setString(2, news.getAuthor());
            ps.setDate(3, new java.sql.Date(news.getPublishedDate().getTime()));
            ps.setString(4, news.getContent());
            ps.setString(5, news.getImage());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNews(News news) {
        String sql = "UPDATE news SET title=?, author=?, published_date=?, content=?, image=? WHERE id=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, news.getTitle());
            ps.setString(2, news.getAuthor());
            ps.setDate(3, new java.sql.Date(news.getPublishedDate().getTime()));
            ps.setString(4, news.getContent());
            ps.setString(5, news.getImage());
            ps.setInt(6, news.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNews(int id) {
        String sql = "DELETE FROM news WHERE id=?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean testConnection() {
        try (Connection conn = DBConnect.getConnection()) {
            System.out.println("Database connection successful!");
            return true;
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void printAllNews() {
        System.out.println("=== DEBUG: Printing all news from database ===");
        List<News> list = getAllNews();
        System.out.println("Total news in DB: " + list.size());
        for (News n : list) {
            System.out.println("ID: " + n.getId() +
                    ", Title: " + n.getTitle() +
                    ", Date: " + n.getPublishedDate() +
                    ", Image: " + n.getImage());
        }
    }


}
