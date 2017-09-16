package jkickerstats.interfaces;

import jkickerstats.domain.MatchLister;
import jkickerstats.domain.MatchPersister;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static jkickerstats.GameTestdata.createDoubleGame;
import static jkickerstats.MatchTestdata.createMatchLinkWithDoubleGame;
import static jkickerstats.interfaces.StatsUpdater.getCurrentSeasonId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatsUpdaterUnitTest {

    @Mock
    private PageParser pageParserMock;
    @Mock
    private PageDownloader pageDownloaderMock;
    @Mock
    private MatchLister matchListerMock;

    @Before
    public void setup() {

        when(pageParserMock.findGames(any(Document.class))).thenReturn(
                singletonList(createDoubleGame()));
        when(pageParserMock.findMatches(any(Document.class))).thenReturn(
                singletonList(createMatchLinkWithDoubleGame()));
        when(pageParserMock.findSeasonIDs(any(Document.class))).thenReturn(
                singletonList(7));
        when(pageParserMock.findLigaLinks(any(Document.class))).thenReturn(
                singletonList(""));
        when(pageParserMock.findMatchLinks(any(Document.class))).thenReturn(
                singletonList(""));
    }

    @Test
    public void returnsTheCurrentSeasonId() {
        assertThat(getCurrentSeasonId(Arrays.asList(7, 5, 2, 4, 1))).isEqualTo(7);

        assertThat(getCurrentSeasonId(Arrays.asList(4, 5, 2, 7, 1))).isEqualTo(7);
    }

    @Test
    public void downloadsAllGamesAndMatchesWhenDBisEmpty() {
        // given
        StatsUpdater statsUpdater = new StatsUpdater(
                pageParserMock,
                pageDownloaderMock,
                Collections::emptyList,
                match -> {
                });

        // when
        statsUpdater.updateStats();

        // then
    }

    @Test
    public void savesOneMatch() {
        // given
        MatchLister lister = Collections::emptyList;
        MatchPersister persister = match -> {
            assertThat(match).isEqualTo(createMatchLinkWithDoubleGame());
        };
        StatsUpdater statsUpdater = new StatsUpdater(
                pageParserMock,
                pageDownloaderMock,
                lister,
                persister);

        // then
        statsUpdater.updateStats();
    }

    @Test
    public void doesNotSaveOldMatches() {
        // given
        MatchLister lister = () -> singletonList(createMatchLinkWithDoubleGame());
        MatchPersister persister = match -> {
            fail("should not save old matches");
        };
        StatsUpdater statsUpdater = new StatsUpdater(pageParserMock, pageDownloaderMock,
                lister,
                persister);

        // then
        statsUpdater.updateStats();
    }
}
