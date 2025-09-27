package me.nickhanson.codeforge.utilities;

import me.nickhanson.codeforge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByTitle(String title);
}
