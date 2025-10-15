/*
package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChallengeRepo extends JpaRepository<Challenge, Long> {

    Optional<Challenge> findByTitle(String title);

    Page<Challenge> findByDifficulty(Difficulty difficulty, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);
}
*/