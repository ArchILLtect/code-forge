package me.nickhanson.codeforge.web;

import jakarta.validation.Valid;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.service.ChallengeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for handling web requests related to Challenges.
 * Provides endpoints for listing, viewing, creating, updating, and deleting challenges.
 */
@Controller
@RequestMapping("/challenges")
public class ChallengesController {

    private static final Logger log = LoggerFactory.getLogger(ChallengesController.class);

    // Service for managing challenges
    private final ChallengeService service;

    /**
     * Constructor for ChallengesController.
     *
     * @param service The ChallengeService used for business logic and data access.
     */
    public ChallengesController(ChallengeService service) {
        this.service = service;
    }

    /**
     * Handles GET requests to list challenges with optional pagination, sorting, and filtering.
     *
     * @param page       The page number to retrieve (default is 0).
     * @param size       The number of items per page (default is 10).
     * @param sort       The field to sort by (default is "title").
     * @param dir        The sort direction ("asc" or "desc", default is "asc").
     * @param difficulty Optional filter by difficulty level.
     * @param model      The model to populate with data for the view.
     * @return The name of the view template for the challenge list.
     */
    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "title") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir,
            @RequestParam(name = "difficulty", required = false) Difficulty difficulty,
            Model model
    ) {
        Sort sortSpec = Sort.by(dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50), sortSpec);

        Page<Challenge> pageResult = service.listChallenges(difficulty, pageable);

        log.info("Rendering challenge list page={} size={} sort={} dir={} difficulty={} count={}",
                page, size, sort, dir, difficulty, pageResult.getNumberOfElements());

        model.addAttribute("challenges", pageResult.getContent());
        model.addAttribute("page", pageResult);
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("pageSize", pageResult.getSize());
        model.addAttribute("hasPrev", pageResult.hasPrevious());
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("difficultyValue", difficulty != null ? difficulty.name() : "");
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "challenges/list";
    }

    /**
     * Handles GET requests to view the details of a specific challenge.
     *
     * @param id    The ID of the challenge to retrieve.
     * @param model The model to populate with data for the view.
     * @return The name of the view template for the challenge detail.
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Challenge challenge = service.getById(id).orElseThrow(() -> {
            log.warn("Challenge not found id={}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found");
        });
        log.info("Rendering challenge detail id={} title={} difficulty={}", id, challenge.getTitle(), challenge.getDifficulty());
        model.addAttribute("challenge", challenge);
        return "challenges/detail";
    }

    // --- Admin-facing CRUD (basic) ---

    /**
     * Handles GET requests to display the form for creating a new challenge.
     *
     * @param model The model to populate with data for the view.
     * @return The name of the view template for the new challenge form.
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new ChallengeForm());
        model.addAttribute("difficulties", Difficulty.values());
        return "challenges/new";
    }

    /**
     * Handles POST requests to create a new challenge.
     *
     * @param form   The form data for the new challenge.
     * @param binding The binding result for validation errors.
     * @param ra     Redirect attributes for flash messages.
     * @param model  The model to populate with data for the view in case of errors.
     * @return A redirect to the challenge detail page if successful, or the new challenge form if validation fails.
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("form") ChallengeForm form,
                         BindingResult binding,
                         RedirectAttributes ra,
                         Model model) {
        if (service.titleExists(form.getTitle())) {
            binding.rejectValue("title", "duplicate", "Title must be unique");
        }
        if (binding.hasErrors()) {
            model.addAttribute("difficulties", Difficulty.values());
            return "challenges/new";
        }
        Challenge saved = service.create(form);
        ra.addFlashAttribute("success", "Challenge created successfully.");
        return "redirect:/challenges/" + saved.getId();
    }

    /**
     * Handles GET requests to display the form for editing an existing challenge.
     *
     * @param id    The ID of the challenge to edit.
     * @param model The model to populate with data for the view.
     * @return The name of the view template for the edit challenge form.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Challenge challenge = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
        ChallengeForm form = new ChallengeForm();
        form.setTitle(challenge.getTitle());
        form.setDifficulty(challenge.getDifficulty());
        form.setBlurb(challenge.getBlurb());
        form.setPromptMd(challenge.getPromptMd());
        model.addAttribute("form", form);
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("challengeId", id);
        return "challenges/edit";
    }

    /**
     * Handles POST requests to update an existing challenge.
     *
     * @param id     The ID of the challenge to update.
     * @param form   The form data for the updated challenge.
     * @param binding The binding result for validation errors.
     * @param ra     Redirect attributes for flash messages.
     * @param model  The model to populate with data for the view in case of errors.
     * @return A redirect to the challenge detail page if successful, or the edit challenge form if validation fails.
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ChallengeForm form,
                         BindingResult binding,
                         RedirectAttributes ra,
                         Model model) {
        if (service.titleExistsForOther(form.getTitle(), id)) {
            binding.rejectValue("title", "duplicate", "Title must be unique");
        }
        if (binding.hasErrors()) {
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("challengeId", id);
            return "challenges/edit";
        }
        Challenge updated = service.update(id, form).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
        ra.addFlashAttribute("success", "Challenge updated successfully.");
        return "redirect:/challenges/" + updated.getId();
    }

    /**
     * Handles POST requests to delete an existing challenge.
     *
     * @param id The ID of the challenge to delete.
     * @param ra Redirect attributes for flash messages.
     * @return A redirect to the challenge list page.
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        boolean removed = service.delete(id);
        if (removed) {
            ra.addFlashAttribute("success", "Challenge deleted.");
        } else {
            ra.addFlashAttribute("error", "Challenge not found.");
        }
        return "redirect:/challenges";
    }
}
