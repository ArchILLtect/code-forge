package me.nickhanson.codeforge.web;

import lombok.Getter;
import me.nickhanson.codeforge.entity.Difficulty;

import java.time.Instant;

/**
 * Lightweight view model for rendering the drill queue without triggering LAZY loads in JSP.
 * This class encapsulates the essential details of a drill queue item.
 */
@Getter
public class DrillQueueRow {

    // The ID of the challenge associated with this drill queue row.
    private final Long challengeId;
    // The title of the challenge.
    private final String title;
    // The difficulty level of the challenge.
    private final Difficulty difficulty;
    // The next due date and time for this drill item.
    private final Instant nextDueAt;

    /**
     * Constructs a new DrillQueueRow with the specified details.
     * @param challengeId The ID of the challenge.
     * @param title       The title of the challenge.
     * @param difficulty  The difficulty level of the challenge.
     * @param nextDueAt   The next due date and time for this drill item.
     */
    public DrillQueueRow(Long challengeId, String title, Difficulty difficulty, Instant nextDueAt) {
        this.challengeId = challengeId;
        this.title = title;
        this.difficulty = difficulty;
        this.nextDueAt = nextDueAt;
    }
}
