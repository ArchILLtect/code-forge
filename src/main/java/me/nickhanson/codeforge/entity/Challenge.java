package me.nickhanson.codeforge.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "CHALLENGES")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA default ctor
@AllArgsConstructor                                  // convenience ctor (you can also use @Builder later)
@ToString(onlyExplicitlyIncluded = true)            // we’ll include selectively (see below)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)   // avoid using all fields by default
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NonNull
    @ToString.Include
    private String title;

    @Enumerated(EnumType.STRING)
    @NonNull
    @ToString.Include
    private Difficulty difficulty;
    private String blurb;
    private String promptMd;

    @Column(updatable = false)
    @ToString.Include
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