package jkickerstats.interfaces;

import jkickerstats.domain.MatchLister;
import jkickerstats.domain.MatchPersister;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static jkickerstats.MatchTestdata.createMatchLinkWithDoubleGame;
import static jkickerstats.MatchTestdata.createMatchWithLink;
import static jkickerstats.interfaces.StatsUpdater.getCurrentSeasonId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class StatsUpdaterUnitTest {

    @Test
    public void returnsTheCurrentSeasonId() {
        assertThat(getCurrentSeasonId(Arrays.asList(7, 5, 2, 4, 1))).isEqualTo(7);

        assertThat(getCurrentSeasonId(Arrays.asList(4, 5, 2, 7, 1))).isEqualTo(7);
    }

    @Test
    public void savesOneMatch() {
        // given
        ParsedMatchesRetriever downloader = new ParsedMatchesRetriever() {
            @Override
            public Stream<Match> get(Integer seasonId) {
                return Stream.of(createMatchWithLink());
            }

            @Override
            public Match downloadGamesFromMatch(Match match) {
                return createMatchLinkWithDoubleGame();
            }

            @Override
            public List<Integer> getSeasonIDs() {
                return singletonList(1);
            }
        };
        MatchLister lister = Collections::emptyList;
        MatchPersister persister = match -> {
            assertThat(match).isEqualTo(createMatchLinkWithDoubleGame());
        };
        StatsUpdater statsUpdater = new StatsUpdater(lister, persister, downloader);

        // then
        statsUpdater.updateStats();
    }

    @Test
    public void doesNotSaveOldMatches() {
        // given
        MatchLister lister = () -> singletonList(createMatchLinkWithDoubleGame());
        ParsedMatchesRetriever downloader = new ParsedMatchesRetriever() {
            @Override
            public Stream<Match> get(Integer seasonId) {
                return Stream.of(createMatchWithLink());
            }

            @Override
            public Match downloadGamesFromMatch(Match match) {
                return createMatchLinkWithDoubleGame();
            }

            @Override
            public List<Integer> getSeasonIDs() {
                return singletonList(1);
            }
        };
        MatchPersister persister = match -> {
            fail("should not save old matches");
        };

        StatsUpdater statsUpdater = new StatsUpdater(lister, persister, downloader);

        // then
        statsUpdater.updateStats();
    }
}
