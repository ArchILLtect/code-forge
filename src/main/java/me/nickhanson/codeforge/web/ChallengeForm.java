package me.nickhanson.codeforge.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.nickhanson.codeforge.entity.Difficulty;

/**
 * DTO (Data Transfer Object) for creating or updating a Challenge entity.
 * This class is used to capture and validate user input from forms.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChallengeForm {

    /**
     * The title of the challenge.
     * Must not be blank and cannot exceed 100 characters.
     */
    @NotBlank
    @Size(max = 100)
    private String title;

    /**
     * The difficulty level of the challenge.
     * Must not be null.
     */
    @NotNull
    private Difficulty difficulty;

    /**
     * A short description or summary of the challenge.
     * Must not be blank.
     */
    @NotBlank
    private String blurb;

    /**
     * The full prompt for the challenge in Markdown format.
     * Must not be blank.
     */
    @NotBlank
    private String promptMd;
}

