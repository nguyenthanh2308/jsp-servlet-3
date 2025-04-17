package main.java.org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.org.example.DAO.PostsDAO;
import main.java.org.example.DAO.PostsDAOImpl;
import main.java.org.example.model.Posts;
import main.java.org.example.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/post/*")
public class PostServlet extends HttpServlet {
    private PostsDAO postsDAO = new PostsDAOImpl();

    private boolean isAuthorizedToModifyPost(HttpServletRequest request, Posts post) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return false;

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("role");

        return currentUser != null &&
                (currentUser.getId() == post.getUser().getId() || "ADMIN".equals(userRole));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/edit".equals(pathInfo)) {
            Long postId = Long.parseLong(request.getParameter("postId"));
            Posts post = postsDAO.findById(postId);
            if (post == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("post", post);
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            createPost(request, response);
        } else if ("/edit".equals(pathInfo)) {
            editPost(request, response);
        }
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        try {
            Posts newPost = new Posts();
            newPost.setTitle(title);
            newPost.setBody(body);
            newPost.setUser(currentUser);
            newPost.setStatus("ACTIVE");
            newPost.setCreatedAt(LocalDateTime.now());
            newPost.setUpdatedAt(LocalDateTime.now());

            postsDAO.save(newPost);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            throw new ServletException("Error creating post", e);
        }
    }

    private void editPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long postId = Long.parseLong(request.getParameter("postId"));
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        Posts existingPost = postsDAO.findById(postId);
        if (existingPost == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy bài viết");
            return;
        }

        if (!isAuthorizedToModifyPost(request, existingPost)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sửa bài viết này");
            return;
        }

        try {
            existingPost.setTitle(title);
            existingPost.setBody(body);
            existingPost.setUpdatedAt(LocalDateTime.now());

            postsDAO.update(existingPost);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật bài viết: " + e.getMessage());
            request.setAttribute("post", existingPost);
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Long postId = Long.parseLong(pathInfo.substring(1));
            deletePost(postId, request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void deletePost(Long postId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Posts post = postsDAO.findById(postId);
        if (post == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!isAuthorizedToModifyPost(request, post)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        postsDAO.delete(postId);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}