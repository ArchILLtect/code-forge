package me.nickhanson.codeforge.persistence;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.DrillItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({DrillItemDao.class, ChallengeDao.class})
class DrillItemDaoTest {

    @Autowired
    private DrillItemDao drillItemDao;

    @Autowired
    private ChallengeDao challengeDao;

    private Challenge sampleChallenge() {
        return new Challenge(
                "Valid Parentheses",
                Difficulty.EASY,
                "Check if parentheses are balanced.",
                "## Prompt..."
        );
    }

    @Test
    void save_and_findById_success() {
        Challenge ch = challengeDao.save(sampleChallenge());
        DrillItem item = new DrillItem(ch);
        DrillItem saved = drillItemDao.save(item);
        assertThat(saved.getId()).isNotNull();

        Optional<DrillItem> found = drillItemDao.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getChallenge().getId()).isEqualTo(ch.getId());
        assertThat(found.get().getTimesSeen()).isZero();
        assertThat(found.get().getStreak()).isZero();
    }

    @Test
    void findByChallengeId_returns_list() {
        Challenge ch = challengeDao.save(sampleChallenge());
        drillItemDao.save(new DrillItem(ch));
        drillItemDao.save(new DrillItem(ch));

        List<DrillItem> list = drillItemDao.findByChallengeId(ch.getId());
        assertThat(list).hasSize(2);
    }

    @Test
    void update_counters_success() {
        Challenge ch = challengeDao.save(sampleChallenge());
        DrillItem item = drillItemDao.save(new DrillItem(ch));
        item.setTimesSeen(item.getTimesSeen() + 1);
        item.setStreak(item.getStreak() + 1);
        DrillItem updated = drillItemDao.save(item);
        assertThat(updated.getTimesSeen()).isEqualTo(1);
        assertThat(updated.getStreak()).isEqualTo(1);
    }

    @Test
    void deleteById_removes_entity() {
        Challenge ch = challengeDao.save(sampleChallenge());
        DrillItem item = drillItemDao.save(new DrillItem(ch));
        Long id = item.getId();
        drillItemDao.deleteById(id);
        assertThat(drillItemDao.findById(id)).isEmpty();
    }
}
