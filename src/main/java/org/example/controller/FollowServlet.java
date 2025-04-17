package main.java.org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.org.example.DAO.FollowDAO;
import main.java.org.example.DAO.FollowDAOImpl;
import main.java.org.example.DAO.UserDAO;
import main.java.org.example.DAO.UserDAOImpl;
import main.java.org.example.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/follow/*")
public class FollowServlet extends HttpServlet {
    private FollowDAO followDAO = new FollowDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
            return;
        }

        try {
            Long followingId = Long.parseLong(pathInfo.substring(1));
            User followingUser = userDAO.getUserById(followingId);

            if (followingUser == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User to follow not found");
                return;
            }

            followDAO.follow(currentUser, followingUser);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (SQLException e) {
            throw new ServletException("Error following user", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
            return;
        }

        try {
            Long followingId = Long.parseLong(pathInfo.substring(1));
            User followingUser = userDAO.getUserById(followingId);

            if (followingUser == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User to unfollow not found");
                return;
            }

            followDAO.unfollow(currentUser, followingUser);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (SQLException e) {
            throw new ServletException("Error unfollowing user", e);
        }
    }
}