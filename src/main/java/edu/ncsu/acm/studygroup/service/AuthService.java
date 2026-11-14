package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.dto.LoginDto;
import edu.ncsu.acm.studygroup.dto.SignupDto;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Handles account registration and login.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates the authentication service.
     *
     * @param userRepository repository used for user persistence
     */
    public AuthService(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Creates a new user account.
     *
     * @param signupDto account information
     * @return newly created user
     */
    public User signup(final SignupDto signupDto) {

        final String email = signupDto.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with this email already exists.");
        }

        final String ncsuId =
                signupDto.getNcsuId() == null
                        ? null
                        : signupDto.getNcsuId().trim();

        if (ncsuId != null
                && !ncsuId.isBlank()
                && userRepository.findByNcsuId(ncsuId).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with this NC State ID already exists.");
        }

        final User user = new User();

        user.setName(signupDto.getName().trim());
        user.setEmail(email);

        if (ncsuId != null && !ncsuId.isBlank()) {
            user.setNcsuId(ncsuId);
        }

        user.setPassword(
                passwordEncoder.encode(signupDto.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Validates a user's login credentials.
     *
     * @param loginDto login information
     * @return authenticated user, if credentials are valid
     */
    public Optional<User> login(final LoginDto loginDto) {

        final String email =
                loginDto.getEmail().trim().toLowerCase();

        final Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        final String storedPassword = user.get().getPassword();

        if (storedPassword == null
                || !passwordEncoder.matches(
                loginDto.getPassword(),
                storedPassword)) {
            return Optional.empty();
        }

        return user;
    }
}