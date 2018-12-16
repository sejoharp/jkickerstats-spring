package jkickerstats.services;

import jkickerstats.MatchTestdata;
import jkickerstats.domain.Match;
import jkickerstats.persistence.MatchLister;
import jkickerstats.persistence.MatchPersister;
import org.assertj.core.api.Fail;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static jkickerstats.services.StatsUpdater.getCurrentSeasonId;
import static org.assertj.core.api.Assertions.assertThat;

public class StatsUpdaterUnitTest {

    @Test
    public void returnsTheCurrentSeasonId() {
        assertThat(getCurrentSeasonId(Stream.of(7, 5, 2, 4, 1))).isEqualTo(7);

        assertThat(getCurrentSeasonId(Stream.of(4, 5, 2, 7, 1))).isEqualTo(7);
    }

    @Test
    public void savesOneMatch() {
        // given
        MatchLister lister = Collections::emptyList;
        MatchPersister persister = match -> assertThat(match).isEqualTo(MatchTestdata.createMatchLinkWithDoubleGame());
        StatsUpdater statsUpdater = new StatsUpdater(lister, persister, createRetriever());

        // then
        statsUpdater.updateStats();
    }

    @Test
    public void doesNotSaveOldMatches() {
        // given
        MatchLister lister = () -> singletonList(MatchTestdata.createMatchLinkWithDoubleGame());
        MatchPersister persister = match -> Fail.fail("should not save old matches");
        StatsUpdater statsUpdater = new StatsUpdater(lister, persister, createRetriever());

        // then
        statsUpdater.updateStats();
    }

    // fixtures

    private ParsedMatchesRetriever createRetriever() {
        return new ParsedMatchesRetriever() {
            @Override
            public Stream<Match> get(Integer seasonId) {
                return Stream.of(MatchTestdata.createMatchWithLink());
            }

            @Override
            public Match downloadGamesFromMatch(Match match) {
                return MatchTestdata.createMatchLinkWithDoubleGame();
            }

            @Override
            public Stream<Integer> getSeasonIDs() {
                return Stream.of(1);
            }
        };
    }
}
