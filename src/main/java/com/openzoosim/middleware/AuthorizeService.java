package com.openzoosim.middleware;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.entities.UserSessionEntity;
import com.openzoosim.models.interfaces.IAuthorizeService;
import com.openzoosim.models.interfaces.ITokenService;
import com.openzoosim.repositories.UserRepository;
import com.openzoosim.repositories.UserSessionRepository;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
@Blocking
public class AuthorizeService implements IAuthorizeService {

    @Inject
    Instance<ITokenService> _tokenService;

    @Inject
    UserRepository _userRepository;

    @Inject
    UserSessionRepository _userSessionRepository;

    public int authorizedUserID(String token, String ip, String deviceThumprint) {

        UserSessionEntity session = _userSessionRepository.findByTokenID(token);

        long expiresAt = session.ExpiresAt.toEpochSecond(ZoneOffset.UTC);
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        boolean isExpired = now > expiresAt;

        if (session.IsArchived || !session.IP.equals(ip) || !session.DeviceThumbprint.equals(deviceThumprint) || isExpired) {
            return 0;
        }

        UserEntity user = _userRepository.findById(Integer.toUnsignedLong(session.User.id));

        if (user == null || user.IsArchived || user.IsBanned || !user.IsVerified) {
            return 0;
        }

        return user.id;
    }

}
