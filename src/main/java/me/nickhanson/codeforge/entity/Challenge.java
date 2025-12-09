package me.nickhanson.codeforge.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

/**
 * Entity representing a coding challenge.
 * This class is mapped to the CHALLENGES table in the database.
 * @author Nick Hanson
 * TODO: needs better Lombok implementation
 */
@Entity
@Table(name = "challenges")
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
    @Column(name = "title", nullable = false, unique = true, length = 255) // Maps to a non-null, unique column with max length 255
    private String title;

    /**
     * The difficulty level of the Challenge.
     */
    @Enumerated(EnumType.STRING) // Maps the enum to a database column as a string
    @ToString.Include // Includes the difficulty field in toString
    @Column(name = "difficulty", nullable = false, length = 20) // Maps to a non-null column with max length 20
    private Difficulty difficulty;

    /**
     * A short description or blurb about the Challenge.
     */
    @Lob // Maps to a large object column for storing long text
    @Column(name = "blurb", nullable = true) // Maps to a column that can be null
    private String blurb;

    /**
     * The full prompt of the Challenge in Markdown format.
     */
    @Lob // Maps to a large object column for storing long text
    @Column(name = "prompt_md", nullable = true) // Maps to a column that can be null
    private String promptMd;

    /**
     * The timestamp when the Challenge was created.
     * This field is automatically populated with the current timestamp when the entity is created.
     * It is not updatable after creation.
     */
    @ToString.Include // Includes the createdAt field in toString
    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // Maps to a column that cannot be updated after creation
    private Instant createdAt;

    /**
     * The timestamp when the Challenge was last updated.
     * This field is automatically populated with the current timestamp when the entity is updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at") // Maps to a column that can be updated
    private Instant updatedAt;

    /**
     * The expected answer used by the evaluator for MVP.
     */
    @Lob
    @Column(name = "expected_answer")
    private String expectedAnswer;

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