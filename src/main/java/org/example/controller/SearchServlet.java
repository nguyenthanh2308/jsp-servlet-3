package main.java.org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.org.example.DAO.FollowDAO;
import main.java.org.example.DAO.FollowDAOImpl;
import main.java.org.example.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private final FollowDAO followDAO = new FollowDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số từ request
        String minFollowingStr = request.getParameter("minFollowing");
        String minFollowersStr = request.getParameter("minFollowers");

        // Đặt giá trị mặc định nếu không có tham số
        int minFollowing = minFollowingStr != null && !minFollowingStr.isEmpty() ? Integer.parseInt(minFollowingStr) : 0;
        int minFollowers = minFollowersStr != null && !minFollowersStr.isEmpty() ? Integer.parseInt(minFollowersStr) : 0;

        // Gọi DAO để tìm kiếm người dùng
        List<User> searchResults = followDAO.findUsersByFollowCriteria(minFollowing, minFollowers);
        request.setAttribute("searchResults", searchResults);
        request.setAttribute("minFollowing", minFollowing);
        request.setAttribute("minFollowers", minFollowers);

        // Chuyển tiếp về home.jsp
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}