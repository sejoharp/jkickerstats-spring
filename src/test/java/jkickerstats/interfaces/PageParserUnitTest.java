package jkickerstats.interfaces;

import jkickerstats.GameTestdata;
import jkickerstats.MatchTestdata;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import org.assertj.core.api.exception.RuntimeIOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static jkickerstats.interfaces.PageParser.filterMatchLinkSnippets;
import static jkickerstats.types.Game.game;
import static org.assertj.core.api.Assertions.assertThat;

public class PageParserUnitTest {

    private static final Document begegnungDoc = loadFile("begegnung.html");
    private static final Document begegnungNoDateDoc = loadFile("begegnung_no_date.html");
    private static final Document begegnungenDoc = loadFile("begegnungen.html");
    private static final Document begegnungenNoDateDoc = loadFile("begegnungen_no_date.html");
    private static final Document begegnungBildDoc = loadFile("begegnung_bild.html");
    private static final Document begegnungenLiveDoc = loadFile("begegnungen_live.html");
    private static final Document uebersichtDoc = loadFile("uebersicht.html");
    private static final Document uebersichtRelegationDoc = loadFile("uebersicht_relegation.html");
    private static final Document begegnungNoNamesDoc = loadFile("begegnung_no_names.html");
    private static final Document begegnungenNumberFormatExceptionDoc = loadFile("begegnungen_NFE.html");
    private static final Document begegnungenUnconfirmedDoc = loadFile("begegnungen_unconfirmed.html");

    @Test
    public void anUnconfirmedMatchIsNotAValidMatch() {
        Elements linkSnippets = filterMatchLinkSnippets(begegnungenUnconfirmedDoc);

        boolean isValid = PageParser.isValidMatchLink(linkSnippets.get(0));

        assertThat(isValid).isFalse();
    }

    @Test
    public void aRunningMatchIsNotAValidMatch() {
        Elements linkSnippets = filterMatchLinkSnippets(begegnungenLiveDoc);

        boolean isValid = PageParser.isValidMatchLink(linkSnippets.get(0));

        assertThat(isValid).isFalse();
    }

    @Test
    public void aConfirmedMatchIsAValidMatch() {
        Elements linkSnippets = filterMatchLinkSnippets(begegnungenLiveDoc);

        boolean isValid = PageParser.isValidMatchLink(linkSnippets.get(1));

        assertThat(isValid).isTrue();
    }

    @Test
    public void returnsAllSeasons() throws IOException {
        List<Integer> expectedSeasonsIDs = Arrays.asList(12, 11, 9, 8, 7, 4, 3, 2, 1);

        List<Integer> seasonsIDs = PageParser.findSeasonIDs(uebersichtDoc).collect(toList());

        assertThat(seasonsIDs).isEqualTo(expectedSeasonsIDs);
        assertThat(seasonsIDs.get(0)).isEqualTo(expectedSeasonsIDs.get(0));
    }

    @Test
    public void returnsAllMatchLinks() throws IOException {
        List<String> matchLinks = PageParser.findMatchLinks(begegnungenDoc).collect(toList());

        assertThat(matchLinks).hasSize(14);
        String expectedMatchLink = "http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=118&id=8675";
        assertThat(matchLinks.get(0)).isEqualTo(expectedMatchLink);
    }

    @Test
    public void returnsAllConfirmedMatchLinks() throws IOException {
        List<String> matchLinks = PageParser.findMatchLinks(begegnungenLiveDoc).collect(toList());

        assertThat(matchLinks).hasSize(13);
    }

    @Test
    public void returnsAllLigaLinks() throws IOException {
        List<String> ligaLinksIDs = PageParser.findLigaLinks(uebersichtDoc).collect(toList());

        assertThat(ligaLinksIDs).hasSize(11);
        assertThat(ligaLinksIDs.get(0)).isEqualTo(
                "http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=118");
    }

    @Test
    public void filtersAllMatchLinkSnippets() throws IOException {
        Elements elements = filterMatchLinkSnippets(begegnungenDoc);

        assertThat(elements).hasSize(90);
    }

    @Test
    public void filtersAllGames() throws IOException {
        Elements gameCount = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(gameCount).hasSize(16);
    }

    @Test
    public void detectsGamelistAsValid() throws IOException {
        Elements gameSnippets = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.isValidGameList(gameSnippets)).isTrue();
    }

    @Test
    public void detectsGameIsValidWithImagesAsValid() throws IOException {
        Elements gameSnippets = PageParser.filterGameSnippets(begegnungBildDoc);

        assertThat(PageParser.isValidGameList(gameSnippets)).isTrue();
    }

    @Test
    public void removesDescriptionFromTeamname() {
        String teamname = "Team Hauff (A)";
        assertThat(PageParser.removeTeamDescriptions(teamname)).isEqualTo("Team Hauff");
    }

    @Test
    public void returnsCompleteTeamnameIfItDoesNotContainDescriptions() {
        String teamname = "Team Hauff";
        assertThat(PageParser.removeTeamDescriptions(teamname)).isEqualTo("Team Hauff");
    }

    @Test
    public void returnsHomeTeamname() throws IOException {
        assertThat(PageParser.parseHomeTeam(begegnungDoc)).isEqualTo("Tingeltangel FC St. Pauli");
    }

    @Test
    public void returnsGuestTeamname() throws IOException {
        assertThat(PageParser.parseGuestTeam(begegnungDoc)).isEqualTo("Hamburg Privateers 08");
    }

    @Test
    public void detectsThatMatchHasNoMatchdate() throws IOException {
        assertThat(PageParser.hasMatchDate(begegnungNoDateDoc)).isFalse();
    }

    @Test
    public void detectsThatMatchHasAMatchdate() throws IOException {
        assertThat(PageParser.hasMatchDate(begegnungDoc)).isTrue();
    }

    @Test
    public void parsesAStringToDate() throws IOException {
        String rawDate = "27.02.2013 20:00";
        Calendar expectedDate = Calendar.getInstance();
        expectedDate.setTimeInMillis(0);
        expectedDate.set(2013, 1, 27, 20, 0);

        Date resultDate = PageParser.parseDate(rawDate);

        assertThat(resultDate).isEqualTo(expectedDate.getTime());
    }

    @Test
    public void parsesMatchdateFromMatchSnippet() throws IOException {
        Calendar expectedDate = Calendar.getInstance();
        expectedDate.clear();
        expectedDate.set(2013, 1, 27, 20, 0);

        assertThat(PageParser.parseMatchDate(begegnungDoc, true)).isEqualTo(expectedDate.getTime());
    }

    @Test
    public void returnsCleanCalendarIfMatchHasNoMatchDate() throws IOException {
        Calendar expectedDate = Calendar.getInstance();
        expectedDate.setTimeInMillis(0);

        assertThat(PageParser.parseMatchDate(begegnungNoDateDoc, false)).isEqualTo(expectedDate.getTime());
    }

    @Test
    public void parsesMatchday() throws IOException {
        assertThat(PageParser.parseMatchDay(begegnungDoc, true)).isEqualTo(1);
    }

    @Test
    public void parsesMatchdayWithoutDate() throws IOException {
        assertThat(PageParser.parseMatchDay(begegnungNoDateDoc, false)).isEqualTo(5);
    }

    @Test
    public void parsesHomeScore() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.parseHomeScore(rawGames.first(), true)).isEqualTo(4);
    }

    @Test
    public void parsesGuestScore() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.parseGuestScore(rawGames.first(), true)).isEqualTo(7);
    }

    @Test
    public void parsesHomeScoreFromGameSnippetWithImages() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungBildDoc);

        assertThat(PageParser.parseHomeScore(rawGames.first(), true)).isEqualTo(4);
    }

    @Test
    public void parsesGuestScoreFromGameSnippetWithImages() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungBildDoc);

        assertThat(PageParser.parseGuestScore(rawGames.first(), true)).isEqualTo(7);
    }

    @Test
    public void singleMatchIsNotADoubleMatch() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.isDoubleMatch(rawGames.first())).isEqualTo(false);
    }

    @Test
    public void detectsDoubleMatch() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.isDoubleMatch(rawGames.last())).isTrue();
    }

    @Test
    public void detectsDoubleMatchWithImages() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungBildDoc);

        assertThat(PageParser.isDoubleMatch(rawGames.last())).isTrue();
    }

    @Test
    public void parsesPositionOfFirstGame() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.parseGamePosition(rawGames.first())).isEqualTo(1);
    }

    @Test
    public void parsesPositionOfLastGame() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        assertThat(PageParser.parseGamePosition(rawGames.last())).isEqualTo(16);
    }

    @Test
    public void parsesPlayerNamesOfLastGame() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        Game game = PageParser.createGame(true, rawGames.last());

        assertThat(game.getHomePlayer1()).isEqualTo("Sommer, Sebastian");
        assertThat(game.getHomePlayer2()).isEqualTo("Hölzer, Heinz");
        assertThat(game.getGuestPlayer1()).isEqualTo("Nestvogel, Markus");
        assertThat(game.getGuestPlayer2()).isEqualTo("Matheuszik, Sven");
    }

    @Test
    public void parsesPlayerNamesOfFirstGame() throws IOException {
        Elements rawGames = PageParser.filterGameSnippets(begegnungDoc);

        Game game = PageParser.createGame(true, rawGames.first());

        assertThat(game.getHomePlayer1()).isEqualTo("Technau, Jerome");
        assertThat(game.getGuestPlayer1()).isEqualTo("Hojas, René");
    }

    @Test
    public void returnsAllGamesFromAMatch() {
        List<Game> games = PageParser.findGames(begegnungDoc).collect(toList());

        assertThat(games).hasSize(16);
    }

    @Test
    public void returnsAllGamesWithImagesFromAMatch() {
        List<Game> games = PageParser.findGames(begegnungBildDoc).collect(toList());

        assertThat(games).hasSize(16);
    }

    @Test
    public void returnsAFullFilledSingleGame() {
        Game game = game()
                .withDoubleMatch(false)
                .withGuestPlayer1("Matheuszik, Sven")
                .withGuestScore(7)
                .withHomePlayer1("Kränz, Ludwig")
                .withHomeScore(5)
                .withPosition(2);

        List<Game> games = PageParser.findGames(begegnungDoc).collect(toList());

        assertThat(games.get(1)).isEqualTo(game);
    }

    @Test
    public void returnsAFullFilledDoubleGame() {
        Game game = game()
                .withDoubleMatch(true)
                .withGuestPlayer1("Zierott, Ulli")
                .withGuestPlayer2("Hojas, René")
                .withGuestScore(5)
                .withHomePlayer1("Fischer, Harro")
                .withHomePlayer2("Kränz, Ludwig")
                .withHomeScore(4)
                .withPosition(3);

        List<Game> games = PageParser.findGames(begegnungDoc).collect(toList());

        assertThat(games.get(2)).isEqualTo(game);
    }

    @Test
    public void returnsAFullFilledSingleGameWithImages() {
        Game game = GameTestdata.createSecondSingleGame();

        List<Game> games = PageParser.findGames(begegnungBildDoc).collect(toList());

        assertThat(games.get(0)).isEqualTo(game);
    }

    @Test
    public void returnsAFullFilledDoubleGameWithImages() {
        Game game = GameTestdata.createDoubleGame();

        List<Game> games = PageParser.findGames(begegnungBildDoc).collect(toList());

        assertThat(games.get(15)).isEqualTo(game);
    }

    @Test
    public void doesNotReturnGamesFromRelagation() {
        List<Game> games = PageParser.findGames(uebersichtRelegationDoc).collect(toList());

        assertThat(games).isEmpty();
    }

    @Test
    public void parsesAllGamesInCorrectOrder() {
        List<Game> games = PageParser.findGames(begegnungNoNamesDoc).collect(toList());
        assertThat(games.get(0).getPosition()).isEqualTo(1);
        assertThat(games.get(1).getPosition()).isEqualTo(2);
        assertThat(games.get(2).getPosition()).isEqualTo(3);
        assertThat(games.get(3).getPosition()).isEqualTo(4);
        assertThat(games.get(4).getPosition()).isEqualTo(5);
        assertThat(games.get(5).getPosition()).isEqualTo(6);
        assertThat(games.get(6).getPosition()).isEqualTo(7);
        assertThat(games.get(7).getPosition()).isEqualTo(8);
        assertThat(games.get(8).getPosition()).isEqualTo(9);
        assertThat(games.get(9).getPosition()).isEqualTo(10);
        assertThat(games.get(10).getPosition()).isEqualTo(11);
        assertThat(games.get(11).getPosition()).isEqualTo(12);
        assertThat(games.get(12).getPosition()).isEqualTo(13);
        assertThat(games.get(13).getPosition()).isEqualTo(14);
        assertThat(games.get(14).getPosition()).isEqualTo(15);
        assertThat(games.get(15).getPosition()).isEqualTo(16);
    }

    @Test
    public void parsesGamesWithoutPlayernames() {
        List<Game> games = PageParser.findGames(begegnungNoNamesDoc).collect(toList());

        Game gameWithoutPlayernames = games.get(1);
        assertThat(gameWithoutPlayernames.getGuestPlayer1()).isEmpty();
        assertThat(gameWithoutPlayernames.getHomePlayer1()).isEmpty();
        assertThat(gameWithoutPlayernames.getHomeScore()).isEqualTo(5);
        assertThat(gameWithoutPlayernames.getGuestScore()).isEqualTo(7);
        assertThat(gameWithoutPlayernames.getPosition()).isEqualTo(2);
        assertThat(gameWithoutPlayernames.isDoubleMatch()).isFalse();
    }

    @Test
    public void returnsAFilledMatch() {
        List<Match> matches = PageParser.findMatches(begegnungenDoc).collect(toList());
        Match match = matches.get(0);

        assertThat(match).isEqualTo(MatchTestdata.createMatchWithLink2());
    }

    @Test
    public void returnsAllMatchesWithoutNumberFormatException() {
        List<Match> matches = PageParser.findMatches(begegnungenNumberFormatExceptionDoc).collect(toList());
        Match match = matches.get(0);

        assertThat(match.getHomeGoals()).isEqualTo(0);
        assertThat(match.getGuestGoals()).isEqualTo(0);
        assertThat(match.getHomeScore()).isEqualTo(13);
        assertThat(match.getGuestScore()).isEqualTo(19);
    }

    @Test
    public void returnsTeamNamesWithoutDescriptions() {
        List<Match> matches = PageParser.findMatches(begegnungenDoc).collect(toList());
        Match match = matches.get(0);

        assertThat(match.getHomeTeam()).isEqualTo("Krabbeltüte");
    }

    @Test
    public void returnsAFilledMatchWithoutDate() {
        List<Match> matches = PageParser.findMatches(begegnungenNoDateDoc).collect(toList());
        Match match = matches.get(0);

        assertThat(match).isEqualTo(MatchTestdata.createMatchLinkWithoutDate());
    }

    @Test
    public void returnsAllMatches() {
        List<Match> matches = PageParser.findMatches(begegnungenDoc).collect(toList());
        assertThat(matches.size()).hasSameClassAs(14);
    }

    private static Document loadFile(String fileName) {
        File testFile = new File(PageParserTest.RECOURCES_DIRECTORY + fileName);
        try {
            return Jsoup.parse(testFile, "UTF-8", "");
        } catch (IOException e) {
            throw new RuntimeIOException(e.getMessage());
        }
    }

}
