package main.java.org.example.DAO;

import main.java.org.example.model.Posts;
import main.java.org.example.model.User;

import java.util.List;

public interface PostsDAO {
    List<Posts> findAll(int offset, int limit, User currentUser);
    void save(Posts post);
    Posts findById(Long id);
    void delete(Long id);
    List<Posts> findAll();
    void update(Posts post);
}