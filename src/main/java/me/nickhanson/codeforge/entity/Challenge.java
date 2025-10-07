package me.nickhanson.codeforge.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "challenges")
@Getter
@Setter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;

    @Column(nullable = false)
    private String blurb;

    @Lob
    @Column(nullable = false)
    private String promptMd;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    // --- constructors ---
    protected Challenge() {} // JPA
    public Challenge(String title, Difficulty difficulty, String blurb, String promptMd) {
        this.title = title;
        this.difficulty = difficulty;
        this.blurb = blurb;
        this.promptMd = promptMd;
    }

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

    private String formatPromptMd() {
        if (promptMd == null) {
            return "null";
        }
        return promptMd.length() > 20 ? promptMd.substring(0, 20) + "..." : promptMd;
    }
}
