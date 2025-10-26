package me.nickhanson.codeforge.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "CHALLENGES")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA default ctor
@ToString(onlyExplicitlyIncluded = true)            // we’ll include selectively (see below)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)   // avoid using all fields by default
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    @ToString.Include
    @Column(nullable = false, unique = true, length = 100)
    private String title;
    @Enumerated(EnumType.STRING)
    @ToString.Include
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;
    @Lob
    @Column(nullable = false)
    private String blurb;
    @Lob
    @Column(nullable = false)
    private String promptMd;
    @ToString.Include
    @Column(updatable = false)
    private Instant createdAt;

    public Challenge(String title, Difficulty difficulty, String blurb, String promptMd) {
        this.title = title;
        this.difficulty = difficulty;
        this.blurb = blurb;
        this.promptMd = promptMd;
    }

    @ToString.Include(name = "promptMd")
    private String promptPreview() {
        if (promptMd == null) return "null";
        return promptMd.length() > 20 ? promptMd.substring(0, 20) + "..." : promptMd;
    }
}