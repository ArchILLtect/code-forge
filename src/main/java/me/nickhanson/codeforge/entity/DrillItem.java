package me.nickhanson.codeforge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Represents a DrillItem entity, which is associated with a Challenge and tracks
 * the user's progress in a drill. This entity includes fields for tracking the
 * number of times the item has been seen, the current streak of correct answers,
 * and the next scheduled appearance of the item.
 * @author Nick Hanson
 */
@Entity
@Table(name = "drill_items")
@Getter
@Setter
@NoArgsConstructor
public class DrillItem {

    /**
     * The unique identifier for the DrillItem.
     * This ID is auto-generated.
     * This ID serves as the primary key for the DrillItem entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optimistic locking version field.
     * Managed by JPA/Hibernate; incremented on each update to prevent lost updates under concurrency.
     */
    @Version
    private Long version;

    /**
     * The Challenge associated with this DrillItem.
     * This is a many-to-one relationship, as multiple DrillItems can be associated with a single Challenge.
     * The association is mandatory (not optional) and uses lazy loading for performance optimization.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    /**
     * The number of times this DrillItem has been seen by the user.
     * This field is initialized to zero and cannot be null.
     */
    @Column(name = "TIMES_SEEN", nullable = false)
    private int timesSeen = 0;

    /**
     * The current streak of consecutive correct or acceptable answers for this DrillItem.
     * This field is initialized to zero and cannot be null.
     */
    @Column(name = "STREAK", nullable = false)
    private int streak = 0; // consecutive correct/acceptable answers

    /**
     * The timestamp for when this DrillItem is next due to appear in a drill session.
     * This field is optional and can be null if the item is not scheduled for future appearance.
     */
    @Column(name = "NEXT_DUE_AT")
    private Instant nextDueAt; // optional scheduling for next appearance

    /**
     * The timestamp for when this DrillItem was created.
     * This field is automatically populated with the current timestamp when the entity is created
     * and is not updatable thereafter.
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    /**
     * Constructs a new DrillItem associated with the specified Challenge.
     *
     * @param challenge The Challenge to associate with this DrillItem.
     */
    public DrillItem(Challenge challenge) {
        this.challenge = challenge;
    }
}
