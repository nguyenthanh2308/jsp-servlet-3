package main.java.org.example.DAO;

import main.java.org.example.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User getUserById(long userId) throws SQLException;

    boolean isEmailExists(String email, long userId) throws SQLException;

    void updateUserProfile(User user) throws SQLException;

    User findByUsername(String username) throws SQLException;

    User findByUsernameAndPassword(String username, String password) throws SQLException;

    void registerUser(User user) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    List<User> searchUsers(int minFollowing, int minFollowers) throws SQLException;
}