package me.nickhanson.codeforge.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing a coding challenge.
 * This class is mapped to the CHALLENGES table in the database.
 */
@Entity
@Table(name = "CHALLENGES")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA default ctor
@ToString(onlyExplicitlyIncluded = true)            // we’ll include selectively (see below)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)   // avoid using all fields by default
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Includes the ID field in equals and hashCode
    @ToString.Include // Includes the ID field in toString
    private Long id;

    @ToString.Include // Includes the title field in toString
    @Column(nullable = false, unique = true, length = 100) // Maps to a non-null, unique column with max length 100
    private String title;

    @Enumerated(EnumType.STRING) // Maps the enum to a database column as a string
    @ToString.Include // Includes the difficulty field in toString
    @Column(nullable = false, length = 20) // Maps to a non-null column with max length 20
    private Difficulty difficulty;

    @Lob // Maps to a large object column for storing long text
    @Column(nullable = false) // Maps to a non-null column
    private String blurb;

    @Lob // Maps to a large object column for storing long text
    @Column(nullable = false) // Maps to a non-null column
    private String promptMd;

    @ToString.Include // Includes the createdAt field in toString
    @Column(updatable = false) // Maps to a column that cannot be updated after creation
    private Instant createdAt;

    /**
     * Constructor for creating a new Challenge.
     * @param title The title of the challenge.
     * @param difficulty The difficulty level of the challenge.
     * @param blurb A short description of the challenge.
     * @param promptMd The full prompt in Markdown format.
     */
    public Challenge(String title, Difficulty difficulty, String blurb, String promptMd) {
        this.title = title;
        this.difficulty = difficulty;
        this.blurb = blurb;
        this.promptMd = promptMd;
    }

    /**
     * Provides a short preview of the prompt for toString purposes.
     * @return The first 20 characters of promptMd followed by "..." if longer than 20 characters,
     *         or the full promptMd if it is 20 characters or shorter. Returns "null" if promptMd is null.
     */
    @ToString.Include(name = "promptMd")
    private String promptPreview() {
        if (promptMd == null) return "null";
        return promptMd.length() > 20 ? promptMd.substring(0, 20) + "..." : promptMd;
    }
}