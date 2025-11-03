package me.nickhanson.codeforge.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing a coding challenge.
 * This class is mapped to the CHALLENGES table in the database.
 * @author Nick Hanson
 * TODO: needs better Lombok implementation
 */
@Entity
@Table(name = "CHALLENGES")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA default ctor
@ToString(onlyExplicitlyIncluded = true)            // we’ll include selectively (see below)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)   // avoid using all fields by default
public class Challenge {

    /**
     * The unique identifier for the Challenge.
     * This ID is auto-generated.
     * This ID serves as the primary key for the Challenge entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Includes the ID field in equals and hashCode
    @ToString.Include // Includes the ID field in toString
    private Long id;

    /**
     * The title of the Challenge.
     */
    @ToString.Include // Includes the title field in toString
    @Column(nullable = false, unique = true, length = 100) // Maps to a non-null, unique column with max length 100
    private String title;

    /**
     * The difficulty level of the Challenge.
     */
    @Enumerated(EnumType.STRING) // Maps the enum to a database column as a string
    @ToString.Include // Includes the difficulty field in toString
    @Column(nullable = false, length = 20) // Maps to a non-null column with max length 20
    private Difficulty difficulty;

    /**
     * A short description or blurb about the Challenge.
     */
    @Lob // Maps to a large object column for storing long text
    @Column(nullable = false) // Maps to a non-null column
    private String blurb;

    /**
     * The full prompt of the Challenge in Markdown format.
     */
    @Lob // Maps to a large object column for storing long text
    @Column(name = "PROMPT_MD", nullable = false) // Maps to a non-null column
    private String promptMd;

    /**
     * The timestamp when the Challenge was created.
     * This field is automatically populated with the current timestamp when the entity is created.
     * It is not updatable after creation.
     */
    @ToString.Include // Includes the createdAt field in toString
    @Column(updatable = false) // Maps to a column that cannot be updated after creation
    private Instant createdAt;

    // Constructor to create a Challenge with all required fields
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