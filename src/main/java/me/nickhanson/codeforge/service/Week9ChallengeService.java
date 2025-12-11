package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;

import java.util.List;
import java.util.Optional;

// TODO: Remove this class ASAP as it is not needed anymore.

/**
 * A temporary service class for Week 9 challenges.
 * This class is intended to be removed as soon as possible.
 * It provides methods to find all challenges and find a challenge by its ID.
 * @author Nick Hanson
 */
public class Week9ChallengeService {
    private final ChallengeService svc;

    public Week9ChallengeService() {
        this.svc = new ChallengeService();
    }

    public Week9ChallengeService(ChallengeService svc) {
        this.svc = svc;
    }

    public List<Challenge> findAll() {
        return svc.listChallenges(null);
    }

    public Optional<Challenge> findById(Long id) {
        return svc.getById(id);
    }
}