package me.nickhanson.codeforge.web;

import me.nickhanson.codeforge.utilities.ChallengeRepo;
import me.nickhanson.codeforge.entity.Challenge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Controller
@RequestMapping("/challenges")
public class ChallengesController {
    private static final Logger log = LoggerFactory.getLogger(ChallengesController.class);
    private final ChallengeRepo repo;

    public ChallengesController(ChallengeRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        List<Challenge> all = repo.findAll();
        log.info("Rendering challenge list, count={}", all.size());
        model.addAttribute("challenges", all);
        return "challenges/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Challenge challenge = repo.findById(id).orElseThrow(() -> {
            log.warn("Challenge not found id={}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found");
        });
        log.info("Rendering challenge detail id={} title={} difficulty={}", id, challenge.getTitle(), challenge.getDifficulty());
        model.addAttribute("challenge", challenge);
        return "challenges/detail";
    }
}

