package jkickerstats.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import jkickerstats.domain.MatchRepoInterface;
import jkickerstats.types.Game;
import jkickerstats.types.Match;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatsUpdaterUnitTest {

	@Mock
	private PageParser pageParserMock;
	@Mock
	private PageDownloader pageDownloaderMock;
	@Mock
	private MatchRepoInterface matchRepoMock;
	@InjectMocks
	private StatsUpdater statsUpdater;
	@Captor
	private ArgumentCaptor<ArrayList<Game>> gameListCaptor;
	@Captor
	private ArgumentCaptor<Match> matchCaptor;

	@Before
	public void setup() {
		when(pageParserMock.findGames(any(Document.class))).thenReturn(
				Arrays.asList(new Game.GameBuilder().build()));
		when(pageParserMock.findMatches(any(Document.class))).thenReturn(
				Arrays.asList(new Match.MatchBuilder().build()));
		when(pageParserMock.findSeasonIDs(any(Document.class))).thenReturn(
				Arrays.asList(7));
		when(pageParserMock.findLigaLinks(any(Document.class))).thenReturn(
				Arrays.asList(""));
		when(pageParserMock.findMatchLinks(any(Document.class))).thenReturn(
				Arrays.asList(""));
	}

	@Test
	public void returnsTheCurrentSeasonId() {
		assertThat(
				statsUpdater.getCurrentSeasonId(Arrays.asList(7, 5, 2, 4, 1)),
				is(7));

		assertThat(
				statsUpdater.getCurrentSeasonId(Arrays.asList(4, 5, 2, 7, 1)),
				is(7));
	}

	@Test
	public void downloadsAllGamesAndMatchesWhenDBisEmpty() {
		when(matchRepoMock.noMatchesAvailable()).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchRepoMock, times(0)).isNewMatch(any(Match.class));
	}

	@Test
	public void downloadsNewGamesAndMatchesWhenDBisFilled() {
		when(matchRepoMock.noMatchesAvailable()).thenReturn(false);
		when(matchRepoMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchRepoMock, times(1)).isNewMatch(any(Match.class));
	}

	@Test
	public void savesOneMatchWhenDBisFilled() {
		when(matchRepoMock.noMatchesAvailable()).thenReturn(false);
		when(matchRepoMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchRepoMock).save(matchCaptor.capture());
		assertThat(matchCaptor.getValue(), is(not(nullValue())));
	}

	@Test
	public void savesOneMatchWhenDBisEmpty() {
		when(matchRepoMock.noMatchesAvailable()).thenReturn(true);
		when(matchRepoMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchRepoMock).save(matchCaptor.capture());
		assertThat(matchCaptor.getValue(), is(not(nullValue())));
	}

	@Test
	public void doesNotSaveOldMatches() {
		when(matchRepoMock.noMatchesAvailable()).thenReturn(false);
		when(matchRepoMock.isNewMatch(any(Match.class))).thenReturn(false);

		statsUpdater.updateStats();

		verify(matchRepoMock, times(0)).save(matchCaptor.capture());
	}
}
