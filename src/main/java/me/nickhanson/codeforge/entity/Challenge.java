package me.nickhanson.codeforge.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.lang.SuppressWarnings;

@Entity
@Table(name = "challenges")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Setter
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Setter
    private Difficulty difficulty;

    @Column(nullable = false)
    @Setter
    private String blurb;

    @Lob
    @Column(nullable = false)
    @Setter
    private String promptMd;

    @CreationTimestamp
    @Column(updatable = false)
    @SuppressWarnings("unused")
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
                ", promptMd='" + (promptMd.length() > 20 ? promptMd.substring(0, 20) + "..." : promptMd) + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
