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

import jkickerstats.types.Game;
import jkickerstats.types.Match;
import jkickerstats.usecases.MatchServiceInterface;

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
	private MatchServiceInterface matchServiceMock;
	@InjectMocks
	private StatsUpdater statsUpdater;
	@Captor
	private ArgumentCaptor<ArrayList<Game>> gameListCaptor;
	@Captor
	private ArgumentCaptor<Match> matchCaptor;

	@Before
	public void setup() {
		when(pageParserMock.findGames(any(Document.class))).thenReturn(
				Arrays.asList(new Game()));
		when(pageParserMock.findMatches(any(Document.class))).thenReturn(
				Arrays.asList(new MatchWithLink()));
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
		when(matchServiceMock.noDataAvailable()).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchServiceMock, times(0)).isNewMatch(any(Match.class));
	}

	@Test
	public void downloadsNewGamesAndMatchesWhenDBisFilled() {
		when(matchServiceMock.noDataAvailable()).thenReturn(false);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchServiceMock, times(1)).isNewMatch(any(Match.class));
	}

	@Test
	public void savesOneMatchWhenDBisFilled() {
		when(matchServiceMock.noDataAvailable()).thenReturn(false);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchServiceMock).saveMatch(matchCaptor.capture());
		assertThat(matchCaptor.getValue(), is(not(nullValue())));
	}

	@Test
	public void savesOneMatchWhenDBisEmpty() {
		when(matchServiceMock.noDataAvailable()).thenReturn(true);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStats();

		verify(matchServiceMock).saveMatch(matchCaptor.capture());
		assertThat(matchCaptor.getValue(), is(not(nullValue())));
	}

	@Test
	public void doesNotSaveOldMatches() {
		when(matchServiceMock.noDataAvailable()).thenReturn(false);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(false);

		statsUpdater.updateStats();

		verify(matchServiceMock, times(0)).saveMatch(matchCaptor.capture());
	}
}
