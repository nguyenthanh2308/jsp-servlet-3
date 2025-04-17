package main.java.org.example.DAO;

import main.java.org.example.model.User;
import main.java.org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowDAOImpl implements FollowDAO {
    @Override
    public void follow(User follower, User following) {
        String sql = "INSERT INTO follows (follower_id, following_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, follower.getId());
            statement.setLong(2, following.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unfollow(User follower, User following) {
        if (follower == null || following == null || follower.getId() == 0 || following.getId() == 0) {
            throw new IllegalArgumentException("Follower or following user is null or has invalid ID");
        }

        String sql = "DELETE FROM follows WHERE follower_id = ? AND following_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, follower.getId());
            statement.setLong(2, following.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findFollowing(User user) {
        List<User> followingUsers = new ArrayList<>();
        String sql = "SELECT following_id FROM follows WHERE follower_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long followingId = resultSet.getLong("following_id");
                User followingUser = new User();
                followingUser.setId(followingId);
                followingUsers.add(followingUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followingUsers;
    }

    @Override
    public List<User> findFollowers(User user) {
        List<User> followers = new ArrayList<>();
        String sql = "SELECT follower_id FROM follows WHERE following_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long followerId = resultSet.getLong("follower_id");
                User followerUser = new User();
                followerUser.setId(followerId);
                followers.add(followerUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }

    @Override
    public List<User> findUsersByFollowCriteria(int minFollowing, int minFollowers) {
        List<User> matchingUsers = new ArrayList<>();
        Map<Long, Integer> followingCount = new HashMap<>();
        Map<Long, Integer> followerCount = new HashMap<>();

        // Đếm số lượng following
        String followingSql = "SELECT follower_id, COUNT(*) as count FROM follows GROUP BY follower_id HAVING count >= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(followingSql)) {
            statement.setInt(1, minFollowing);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long userId = resultSet.getLong("follower_id");
                followingCount.put(userId, resultSet.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Đếm số lượng followers
        String followerSql = "SELECT following_id, COUNT(*) as count FROM follows GROUP BY following_id HAVING count >= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(followerSql)) {
            statement.setInt(1, minFollowers);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long userId = resultSet.getLong("following_id");
                followerCount.put(userId, resultSet.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Lấy thông tin chi tiết của user thỏa mãn điều kiện
        List<Long> userIds = new ArrayList<>();
        for (Long userId : followingCount.keySet()) {
            if (!userIds.contains(userId)) userIds.add(userId);
        }
        for (Long userId : followerCount.keySet()) {
            if (!userIds.contains(userId)) userIds.add(userId);
        }

        for (Long userId : userIds) {
            String userSql = "SELECT * FROM users WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(userSql)) {
                statement.setLong(1, userId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setRole(resultSet.getString("role"));
                    user.setCreatedAt(resultSet.getTimestamp("created_at"));
                    user.setEmail(resultSet.getString("email"));
                    user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    user.setProvinceId(resultSet.getInt("province_id"));
                    user.setAvatar(resultSet.getString("avatar"));
                    matchingUsers.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return matchingUsers;
    }

    @Override
    public int getFollowingCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getFollowerCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM follows WHERE following_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }
}