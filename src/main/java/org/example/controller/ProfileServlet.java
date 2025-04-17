package main.java.org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import main.java.org.example.DAO.ProvinceDAO;
import main.java.org.example.DAO.ProvinceDAOImpl;
import main.java.org.example.DAO.UserDAO;
import main.java.org.example.DAO.UserDAOImpl;
import main.java.org.example.model.Province;
import main.java.org.example.model.User;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 5 * 5) // 25MB
public class ProfileServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();
    private ProvinceDAO provinceDAO = new ProvinceDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vô hiệu hóa cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        System.out.println("ProfileServlet: doGet called");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("ProfileServlet: User not logged in, redirecting to /login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User currentUser = (User) session.getAttribute("user");
            System.out.println("ProfileServlet: Current user - " + currentUser.getUsername());
            request.setAttribute("user", currentUser);

            List<Province> provinces = provinceDAO.getAllProvinces();
            System.out.println("ProfileServlet: Loaded " + provinces.size() + " provinces");
            for (Province province : provinces) {
                System.out.println("ProfileServlet: Province in list - id: " + province.getIdProvince() + ", name: " + province.getNameProvince());
            }
            request.setAttribute("provinces", provinces);

            System.out.println("ProfileServlet: Forwarding to /profile.jsp");
            // Kiểm tra xem file profile.jsp có tồn tại không
            if (getServletContext().getResource("/profile.jsp") == null) {
                System.err.println("ProfileServlet: Error - /profile.jsp not found");
                request.setAttribute("error", "Không tìm thấy trang profile.jsp. Vui lòng kiểm tra cấu trúc thư mục.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            System.err.println("ProfileServlet: Error loading provinces - " + e.getMessage());
            request.setAttribute("error", "Không thể tải danh sách tỉnh/thành. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vô hiệu hóa cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        System.out.println("ProfileServlet: doPost called");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        String email = request.getParameter("email");
        String birthDateStr = request.getParameter("birthDate");
        String provinceIdStr = request.getParameter("provinceId");
        Part avatarPart = request.getPart("avatar");

        System.out.println("ProfileServlet: doPost - email: " + email + ", birthDate: " + birthDateStr + ", provinceId: " + provinceIdStr);

        if (!isValidEmail(email)) {
            request.setAttribute("emailError", "Email không hợp lệ.");
            forwardToProfilePage(request, response);
            return;
        }
        try {
            if (isEmailExists(email, currentUser.getId())) {
                request.setAttribute("emailError", "Email đã tồn tại trong hệ thống.");
                forwardToProfilePage(request, response);
                return;
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi kiểm tra email. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateStr);
        } catch (Exception e) {
            request.setAttribute("birthDateError", "Ngày sinh không hợp lệ.");
            forwardToProfilePage(request, response);
            return;
        }
        LocalDate currentDate = LocalDate.of(2025, 4, 16);
        int age = Period.between(birthDate, currentDate).getYears();
        if (age < 15) {
            request.setAttribute("birthDateError", "Bạn phải trên 15 tuổi để đăng ký.");
            forwardToProfilePage(request, response);
            return;
        }

        String avatarFileName = currentUser.getAvatar();
        if (avatarPart != null && avatarPart.getSize() > 0) {
            String contentType = avatarPart.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                request.setAttribute("avatarError", "Chỉ chấp nhận file JPG hoặc PNG.");
                forwardToProfilePage(request, response);
                return;
            }

            if (avatarPart.getSize() > 200 * 1024) {
                request.setAttribute("avatarError", "Dung lượng file không được vượt quá 200KB.");
                forwardToProfilePage(request, response);
                return;
            }

            String uploadPath = getServletContext().getRealPath("") + File.separator + "img";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            avatarFileName = System.currentTimeMillis() + "_" + avatarPart.getSubmittedFileName();
            avatarPart.write(uploadPath + File.separator + avatarFileName);
        }

        currentUser.setEmail(email);
        currentUser.setBirthDate(birthDate);
        currentUser.setProvinceId(Integer.parseInt(provinceIdStr));
        if (avatarFileName != null) {
            currentUser.setAvatar(avatarFileName);
        }

        try {
            userDAO.updateUserProfile(currentUser);
            session.setAttribute("user", currentUser);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi cập nhật hồ sơ. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isEmailExists(String email, long userId) throws SQLException {
        return userDAO.isEmailExists(email, userId);
    }

    private void forwardToProfilePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Province> provinces = provinceDAO.getAllProvinces();
            request.setAttribute("provinces", provinces);
            // Kiểm tra xem file profile.jsp có tồn tại không
            if (getServletContext().getResource("/profile.jsp") == null) {
                System.err.println("ProfileServlet: Error - /profile.jsp not found in forwardToProfilePage");
                request.setAttribute("error", "Không tìm thấy trang profile.jsp. Vui lòng kiểm tra cấu trúc thư mục.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi tải danh sách tỉnh/thành. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}