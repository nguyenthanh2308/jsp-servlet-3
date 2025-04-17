package main.java.org.example.DAO;

import main.java.org.example.model.User;

import java.sql.SQLException;
import java.util.List;

public interface FollowDAO {
    void follow(User follower, User following);

    void unfollow(User follower, User following);

    List<User> findFollowing(User user);

    List<User> findFollowers(User user);

    List<User> findUsersByFollowCriteria(int minFollowing, int minFollowers);

    int getFollowingCount(Long userId) throws SQLException;

    int getFollowerCount(Long userId) throws SQLException;
}