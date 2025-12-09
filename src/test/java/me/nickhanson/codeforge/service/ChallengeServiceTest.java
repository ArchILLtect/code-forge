package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.web.ChallengeForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import me.nickhanson.codeforge.testutil.DbReset;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest extends DbReset {

    @Mock ChallengeDao dao;
    @InjectMocks ChallengeService svc;

    @Test
    void findAll() {
        List<Challenge> all = List.of(
                new Challenge("Two Sum", Difficulty.EASY, "b","p"),
                new Challenge("LRU Cache", Difficulty.HARD, "b","p")
        );
        when(dao.getAll()).thenReturn(all);

        List<Challenge> result = svc.listChallenges(null);

        assertEquals(2, result.size());
        verify(dao).getAll();
        verifyNoMoreInteractions(dao);
    }

    @Test
    void findById_returnsOne_whenPresent() {
        Challenge c = new Challenge("Two Sum", Difficulty.EASY, "b","p");
        when(dao.getById(42L)).thenReturn(c);

        assertTrue(svc.getById(42L).isPresent());
        assertEquals("Two Sum", svc.getById(42L).get().getTitle());
        verify(dao, times(2)).getById(42L); // or call once then store the Optional
    }

    @Test
    void create_setsFields_and_saves() {
        ChallengeForm form = new ChallengeForm();
        form.setTitle("Two Sum");
        form.setDifficulty(Difficulty.EASY);
        form.setBlurb("b");
        form.setPromptMd("p");

        svc.create(form);

        ArgumentCaptor<Challenge> cap = ArgumentCaptor.forClass(Challenge.class);
        verify(dao).saveOrUpdate(cap.capture());
        Challenge saved = cap.getValue();
        assertEquals("Two Sum", saved.getTitle());
        assertEquals(Difficulty.EASY, saved.getDifficulty());
        assertEquals("b", saved.getBlurb());
        assertEquals("p", saved.getPromptMd());
    }

    @Test
    void update_changesFields_and_saves() {
        Long id = 10L;
        Challenge existing = new Challenge("Old", Difficulty.EASY, "b", "p");
        // emulate persisted id
        try { var f = Challenge.class.getDeclaredField("id"); f.setAccessible(true); f.set(existing, id); } catch (Exception ignored) {}
        when(dao.getById(id)).thenReturn(existing);

        ChallengeForm form = new ChallengeForm();
        form.setTitle("New");
        form.setDifficulty(Difficulty.MEDIUM);
        form.setBlurb("b2");
        form.setPromptMd("p2");

        Optional<Challenge> res = svc.update(id, form);
        assertTrue(res.isPresent());
        verify(dao).saveOrUpdate(existing);
        assertEquals("New", existing.getTitle());
        assertEquals(Difficulty.MEDIUM, existing.getDifficulty());
    }

    @Test
    void titleExists_true_when_caseInsensitiveMatch() {
        when(dao.existsTitleIgnoreCase("two sum")).thenReturn(true);
        assertTrue(svc.titleExists("two sum"));
    }

    @Test
    void getById_returnsEmpty_whenMissing() {
        when(dao.getById(999L)).thenReturn(null);
        assertTrue(svc.getById(999L).isEmpty());
    }
}
