package me.nickhanson.codeforge.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity class representing a Challenge in the system.
 * This class is mapped to the "challenges" table in the database.
 */
@Entity
@Table(name = "challenges")
@Getter
@Setter
public class Challenge {

    /**
     * The unique identifier for the challenge.
     * Auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the challenge.
     * Must be unique, not null, and have a maximum length of 100 characters.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String title;

    /**
     * The difficulty level of the challenge.
     * Stored as a string and cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;

    /**
     * A short description or summary of the challenge.
     * Stored as a large object (LOB) and cannot be null.
     */
    @Lob
    @Column(nullable = false)
    private String blurb;

    /**
     * The full prompt for the challenge in Markdown format.
     * Stored as a large object (LOB) and cannot be null.
     */
    @Lob
    @Column(nullable = false)
    private String promptMd;

    /**
     * The timestamp indicating when the challenge was created.
     * Automatically set when the entity is persisted and cannot be updated.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /**
     * Default constructor required by JPA.
     * Protected to prevent direct instantiation.
     */
    protected Challenge() {} // JPA

    /**
     * Constructs a new Challenge with the specified title, difficulty, blurb, and prompt.
     *
     * @param title     The title of the challenge.
     * @param difficulty The difficulty level of the challenge.
     * @param blurb     A short description of the challenge.
     * @param promptMd  The full prompt in Markdown format.
     */
    public Challenge(String title, Difficulty difficulty, String blurb, String promptMd) {
        this.title = title;
        this.difficulty = difficulty;
        this.blurb = blurb;
        this.promptMd = promptMd;
    }

    /**
     * Returns a string representation of the Challenge object.
     * Includes the id, title, difficulty, blurb, a truncated version of promptMd, and createdAt.
     *
     * @return A string representation of the Challenge.
     */
    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", difficulty=" + difficulty +
                ", blurb='" + blurb + '\'' +
                ", promptMd='" + formatPromptMd() + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Formats the prompt Markdown for display.
     * If the prompt is longer than 20 characters, it truncates and appends "...".
     *
     * @return A formatted string representation of the prompt.
     */
    private String formatPromptMd() {
        if (promptMd == null) {
            return "null";
        }
        return promptMd.length() > 20 ? promptMd.substring(0, 20) + "..." : promptMd;
    }
}
