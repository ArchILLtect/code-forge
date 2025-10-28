package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.DrillItem;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.service.ChallengeRunService;
import me.nickhanson.codeforge.service.ChallengeService;
import me.nickhanson.codeforge.service.DrillService;
import me.nickhanson.codeforge.service.RunResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for managing the Drill feature.
 * Provides endpoints for viewing the drill queue, solving challenges, and submitting solutions.
 */
@Controller
@RequestMapping("/drill")
public class DrillController {

    private static final Logger log = LoggerFactory.getLogger(DrillController.class);

    private final DrillService drillService;
    private final ChallengeService challengeService;
    private final ChallengeRunService runService;

    /**
     * Constructor for DrillController.
     *
     * @param drillService    The DrillService used for managing drill items.
     * @param challengeService The ChallengeService used for accessing challenge data.
     * @param runService      The ChallengeRunService used for evaluating challenge submissions.
     */
    public DrillController(DrillService drillService,
                           ChallengeService challengeService,
                           ChallengeRunService runService) {
        this.drillService = drillService;
        this.challengeService = challengeService;
        this.runService = runService;
    }

    /**
     * Handles GET requests to view the drill queue.
     *
     * @param limit The maximum number of items to retrieve (default is 10).
     * @param model The model to populate with data for the view.
     * @return The name of the view template for the drill queue.
     */
    @Transactional(readOnly = true)
    @GetMapping
    public String queue(@RequestParam(name = "limit", defaultValue = "10") int limit, Model model) {
        log.debug("GET /drill queue limit={}", limit);
        List<DrillItem> items = drillService.getDueQueue(limit);
        List<DrillQueueRow> rows = items.stream().map(di -> {
            Long cid = di.getChallenge().getId();
            Challenge c = challengeService.getById(cid).orElse(null);
            String title = (c != null ? c.getTitle() : ("Challenge " + cid));
            Difficulty difficulty = (c != null ? c.getDifficulty() : null);
            return new DrillQueueRow(cid, title, difficulty, di.getNextDueAt());
        }).collect(Collectors.toList());
        model.addAttribute("rows", rows);
        model.addAttribute("count", rows.size());
        return "drill/queue";
    }

    /**
     * Handles GET requests to retrieve the next due drill item.
     *
     * @param ra Redirect attributes for flash messages.
     * @return A redirect to the next drill item or the drill queue if no items are due.
     */
    @Transactional(readOnly = true)
    @GetMapping("/next")
    public String next(RedirectAttributes ra) {
        log.debug("GET /drill/next");
        return drillService.nextDue()
                .map(di -> "redirect:/drill/" + di.getChallenge().getId())
                .orElseGet(() -> {
                    ra.addFlashAttribute("info", "No items due yet. Add challenges or check back soon.");
                    return "redirect:/drill";
                });
    }

    /**
     * Handles GET requests to display the solve page for a specific challenge.
     *
     * @param id    The ID of the challenge to solve.
     * @param model The model to populate with data for the view.
     * @return The name of the view template for solving the challenge.
     */
    @GetMapping("/{id}")
    public String solve(@PathVariable("id") Long id, Model model) {
        log.debug("GET /drill/{}", id);
        Challenge challenge = challengeService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found"));
        DrillItem di = drillService.ensureDrillItem(id);
        model.addAttribute("challenge", challenge);
        model.addAttribute("drillItem", di);
        return "drill/solve";
    }

    /**
     * Handles POST requests to submit a solution for a specific challenge.
     *
     * @param id       The ID of the challenge being solved.
     * @param language The programming language of the submitted code.
     * @param code     The submitted code (optional).
     * @param ra       Redirect attributes for flash messages.
     * @return A redirect to the next drill item after submission.
     */
    @PostMapping("/{id}/submit")
    public String submit(@PathVariable("id") Long id,
                         @RequestParam("language") String language,
                         @RequestParam(value = "code", required = false) String code,
                         RedirectAttributes ra) {
        log.debug("POST /drill/{}/submit language={} codeSize={}", id, language, (code == null ? 0 : code.length()));
        // "Run" the code with the stub runner
        RunResult result = runService.run(id, language, code);
        Outcome outcome = result.getOutcome();

        // Persist the outcome and update scheduling
        drillService.recordOutcome(id, outcome, code);

        String flash = switch (outcome) {
            case CORRECT -> "Outcome: CORRECT — nice work!";
            case ACCEPTABLE -> "Outcome: ACCEPTABLE — close enough for the stub.";
            case INCORRECT -> "Outcome: INCORRECT — try again soon.";
            case SKIPPED -> "Outcome: SKIPPED — we'll resurface this shortly.";
        };
        ra.addFlashAttribute("success", flash);
        ra.addFlashAttribute("info", result.getMessage());
        return "redirect:/drill/next";
    }

    /**
     * Handles POST requests to add a challenge to the drill queue.
     *
     * @param id The ID of the challenge to add.
     * @param ra Redirect attributes for flash messages.
     * @return A redirect to the challenge detail page.
     */
    @PostMapping("/{id}/add")
    public String addToDrill(@PathVariable("id") Long id, RedirectAttributes ra) {
        log.debug("POST /drill/{}/add", id);
        drillService.ensureDrillItem(id);
        ra.addFlashAttribute("success", "Challenge added to Drill queue.");
        return "redirect:/challenges/" + id;
    }
}
