package jkickerstats.persistence;

import jkickerstats.MatchTestdata;
import jkickerstats.domain.Match;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static jkickerstats.MatchTestdata.createTestMatch;
import static org.assertj.core.api.Assertions.assertThat;

public class NitriteRepositoryTest {
    private MatchLister lister;
    private MatchPersister persister;
    private NitriteMatchRepository nitriteMatchRepository;

    @Before
    public void before() {
        nitriteMatchRepository = new NitriteMatchRepository("nitrite-unittest.db");
        lister = nitriteMatchRepository;
        persister = nitriteMatchRepository;
    }

    @After
    public void tearDown() throws Exception {
        nitriteMatchRepository.deleteAllMatches();
        nitriteMatchRepository.closeDb();
    }

    @Test
    public void dbHasNoMatches() {
        assertThat(lister.noMatchesAvailable()).isTrue();
    }

    @Test
    public void countesTheNumberOfMatches() {
        assertThat(lister.countMatches()).isEqualTo(0);
        persister.save(createTestMatch());
        assertThat(lister.countMatches()).isEqualTo(1);
        persister.save(MatchTestdata.createMatchWithSingleGame());
        assertThat(lister.countMatches()).isEqualTo(2);
    }

    @Test
    public void detectsAlreadyPersistedMatches() {
        persister.save(createTestMatch());
        assertThat(lister.isNewMatch(createTestMatch())).isFalse();
        assertThat(lister.isNewMatch(MatchTestdata.createMatchWithSingleGame())).isTrue();
    }

    @Test
    public void dbHasMatches() {
        persister.save(createTestMatch());

        assertThat(lister.noMatchesAvailable()).isFalse();
        Match persistedMatch = nitriteMatchRepository.getAllMatches().get(0);
        assertThat(persistedMatch.getGames()).hasSize(2);
    }

    @Test
    public void savesACompleteMatch() {
        persister.save(createTestMatch());
        Match persistedMatch = nitriteMatchRepository.getAllMatches().get(0);

        assertThat(persistedMatch).isEqualTo(createTestMatch());
    }

    @Test
    public void findesAllMatches() {
        //given
        Match expectedMatch = createTestMatch();
        persister.save(expectedMatch);

        //when
        List<Match> matchFromDb = lister.getAllMatches();

        //then
        assertThat(matchFromDb.get(0)).isEqualTo(expectedMatch);
    }

    @Test
    public void convertsBothWays() {
        NitriteMatch nitiriteMatch = NitriteMatch.fromMatch(createTestMatch());
        Match match = nitiriteMatch.toMatch();
        assertThat(match).isEqualTo(createTestMatch());
    }
}
