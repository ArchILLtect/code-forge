package me.nickhanson.codeforge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class Week9ChallengeServiceTest {

    @Mock ChallengeService svc;
    Week9ChallengeService w9;

    @BeforeEach
    void setUp() { w9 = new Week9ChallengeService(svc); }

    @Test
    void findAll() {
        w9.findAll();
        verify(svc).listChallenges(null);
    }

    @Test
    void findById() {
        Long id = 42L;
        w9.findById(id);
        verify(svc).getById(id);
    }
}
