package me.nickhanson.codeforge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Represents a Submission entity, which tracks the outcome of a user's attempt
 * to solve a Challenge. This entity includes fields for the associated Challenge,
 * the result of the submission, and optional solution code.
 */
@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
public class Submission {

    /**
     * The unique identifier for the submission.
     * This ID is auto-generated.
     * This ID serves as the primary key for the Submission entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The Challenge associated with this submission.
     * This is a many-to-one relationship, as multiple submissions can be associated with a single Challenge.
     * The association is mandatory (not optional) and uses lazy loading for performance optimization.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    /**
     * The outcome of the submission.
     * This field uses the Outcome enum to represent the result of the submission.
     * The outcome is stored as a string in the database and is mandatory (not nullable).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Outcome outcome;

    /**
     * Optional solution code associated with the submission.
     * This field is a large object (LOB) to accommodate potentially lengthy code submissions.
     */
    @Lob
    private String code; // optional solution text for future use

    /**
     * The timestamp when the submission was created.
     * This field is automatically populated with the current timestamp when the entity is created.
     * It is not updatable after creation.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /**
     * Constructs a new Submission with the specified challenge, outcome, and optional code.
     *
     * @param challenge The Challenge associated with this submission.
     * @param outcome   The outcome of the submission.
     * @param code      Optional solution code for the submission.
     */
    public Submission(Challenge challenge, Outcome outcome, String code) {
        this.challenge = challenge;
        this.outcome = outcome;
        this.code = code;
    }
}

