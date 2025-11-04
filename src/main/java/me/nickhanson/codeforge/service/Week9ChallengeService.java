package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;

import java.util.List;
import java.util.Optional;

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