package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found")); // Throwing exception directly here
    }
}
