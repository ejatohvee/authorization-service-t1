package org.ejatohvee.authorizationservicet1.repository;

import org.ejatohvee.authorizationservicet1.entity.RefreshToken;
import org.ejatohvee.authorizationservicet1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
