package news_digest_backend.service;

import news_digest_backend.dto.AuthResponse;
import news_digest_backend.dto.LoginRequest;
import news_digest_backend.dto.SignupRequest;
import news_digest_backend.model.User;
import news_digest_backend.repository.UserRepository;
import news_digest_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already Registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token , "Signup Successful");
    }
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User Not Found"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token , "Login Successful");
    }
}
