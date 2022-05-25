package ch.uzh.ifi.group26.scrumblebee.service;

import ch.uzh.ifi.group26.scrumblebee.entity.RefreshToken;
import ch.uzh.ifi.group26.scrumblebee.entity.User;
import ch.uzh.ifi.group26.scrumblebee.repository.RefreshTokenRepository;
import ch.uzh.ifi.group26.scrumblebee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            RefreshToken refreshToken = new RefreshToken();
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                refreshToken.setUser(optionalUser.get());
                long refreshTokenDurationsMS = 3600000L;
                refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationsMS));
                refreshToken.setToken(UUID.randomUUID().toString());
                refreshToken = refreshTokenRepository.save(refreshToken);
                refreshTokenRepository.flush();
            }
            return refreshToken;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "createRefreshToken: User does not exist");
        }

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired, please sign in again.");
        }
        return token;
    }
}
