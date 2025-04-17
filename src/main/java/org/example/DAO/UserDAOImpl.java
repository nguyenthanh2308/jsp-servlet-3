package main.java.org.example.DAO;

import main.java.org.example.model.User;
import main.java.org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public User getUserById(long userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setEmail(resultSet.getString("email"));
                    user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    user.setProvinceId(resultSet.getInt("province_id"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isEmailExists(String email, long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public void updateUserProfile(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, birth_date = ?, province_id = ?, avatar = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setDate(2, Date.valueOf(user.getBirthDate()));
            statement.setInt(3, user.getProvinceId());
            statement.setString(4, user.getAvatar());
            statement.setLong(5, user.getId());
            int rowsUpdated = statement.executeUpdate();
            System.out.println("UserDAOImpl: Updated " + rowsUpdated + " rows for user id " + user.getId());
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setEmail(resultSet.getString("email"));
                    user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    user.setProvinceId(resultSet.getInt("province_id"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setEmail(resultSet.getString("email"));
                    user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    user.setProvinceId(resultSet.getInt("province_id"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, email, birth_date, province_id, avatar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getEmail());
            statement.setDate(5, Date.valueOf(user.getBirthDate()));
            statement.setInt(6, user.getProvinceId());
            statement.setString(7, user.getAvatar());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
            }
            System.out.println("UserDAOImpl: Registered user - id: " + user.getId() + ", username: " + user.getUsername());
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                user.setProvinceId(resultSet.getInt("province_id"));
                user.setAvatar(resultSet.getString("avatar"));
                users.add(user);
                System.out.println("UserDAOImpl: Loaded user - id: " + user.getId() + ", username: " + user.getUsername());
            }
        }
        return users;
    }

    @Override
    public List<User> searchUsers(int minFollowing, int minFollowers) throws SQLException {
        List<User> users = new ArrayList<>();
        // Giả sử bảng users không có cột following và followers, phương thức này sẽ trả về danh sách rỗng
        // Nếu có bảng riêng để lưu following/followers, cần truy vấn thêm
        System.out.println("UserDAOImpl: searchUsers - minFollowing: " + minFollowing + ", minFollowers: " + minFollowers + " (not implemented)");
        return users;
    }
}