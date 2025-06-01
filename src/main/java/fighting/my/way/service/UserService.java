package fighting.my.way.service;

import fighting.my.way.entity.User;
import fighting.my.way.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            return false; // 이미 존재하는 사용자
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setEmail(email);
        userRepository.save(newUser); 
        return true;
    }


    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.filter(u -> u.getPassword().equals(password));
    }
}
