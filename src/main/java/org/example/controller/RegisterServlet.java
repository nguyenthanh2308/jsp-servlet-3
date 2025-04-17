package main.java.org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.org.example.model.User;
import main.java.org.example.DAO.UserDAO;
import main.java.org.example.DAO.UserDAOImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAOImpl();

        try {
            User existingUser = userDAO.findByUsername(username);

            if (existingUser != null) {
                request.setAttribute("error", "Username đã tồn tại!");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole("USER");
            newUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now())); // Chuyển LocalDateTime thành Timestamp

            userDAO.registerUser(newUser); // Thay save bằng registerUser

            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLException e) {
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}