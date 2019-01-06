package jkickerstats.services;

import jkickerstats.domain.Game;
import jkickerstats.domain.Match;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jkickerstats.domain.Match.createMatch;
import static jkickerstats.services.ParsedMatchesRetrieverImpl.*;
import static jkickerstats.services.StatsUpdater.getCurrentSeasonId;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatsUpdaterTest {
    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

    @Autowired
    private ParsedMatchesRetriever parsedMatchesRetriever;

    @Ignore
    @Test
    public void createCSVFileWithAllGames() {
        List<Match> matches = downloadAllMatchesForTesting();
        List<String> gameStrings = csvCreator.createCsvRowList(matches);
        csvCreator.createCsvFile(gameStrings);
    }

    @Test
    @Ignore
    public void savesAllMatchesWithGames() {
        statsUpdater.getAllData();
    }

    @Ignore
    @Test
    public void getsSeasonIds() {
        Stream<Integer> seasonIDs = parsedMatchesRetriever.getSeasonIDs();
        assertThat(getCurrentSeasonId(seasonIDs)).isEqualTo((12));
    }

    @Ignore
    @Test
    public void updatesCurrentSeason() {
        statsUpdater.updateCurrentSeason();
    }

    @Ignore
    @Test
    public void getsLinks() {
        assertThat(ParsedMatchesRetrieverImpl.getLigaLinks(11)).isNotEmpty();
    }

    @Test
    public void getsSaison20182019() {
        assertThat(ParsedMatchesRetrieverImpl.getLigaDocuments(16)).hasSize(13);
    }

    @Test
    public void getsZwischenSaison2018() {
        assertThat(ParsedMatchesRetrieverImpl.getLigaDocuments(13)).hasSize(1);
    }

    @Ignore
    @Test
    public void findsMatches() {
        ParsedMatchesRetrieverImpl.getLigaLinks(13)
                .forEach(ligaLink -> {
                    System.out.println("processing liga link: " + ligaLink);
                    ParsedMatchesRetrieverImpl.getMatchLinks(ligaLink)
                            .forEach(matchLink -> {
                                System.out.println("processing createMatch link: " + matchLink);
                                getGames(matchLink).forEach(System.out::println);
                            });
                });

    }

    @Ignore
    @Test
    public void findsAllMatches() {
        ParsedMatchesRetrieverImpl.getLigaLinks(16)
                .forEach(ligaLink -> ParsedMatchesRetrieverImpl.getMatches(ligaLink)
                        .forEach(match -> {
                            Match fullMatch = createMatch(match)
                                    .withGames(getGames(match.getMatchLink()).collect(toList()));
                            System.out.println(String.format("Datum:%s home:%s guest:%s spiele:%s",
                                    fullMatch.getMatchDate(),
                                    fullMatch.getHomeTeam(),
                                    fullMatch.getGuestTeam(),
                                    fullMatch.getGames().size()));
                        }));
    }

    private List<Match> downloadAllMatchesForTesting() {
        List<Match> allMatches = new ArrayList<>();
        parsedMatchesRetriever.getSeasonIDs().forEach(seasonId -> {
            System.out.println("processing seasonId:" + seasonId);
            getLigaLinks(seasonId).forEach(ligaLink -> {
                System.out.println("processing ligalink:" + ligaLink);
                getMatches(ligaLink).forEach(match -> {
                    System.out.println("processing createMatch:" + match.getMatchDate());
                    allMatches.add(createMatch(match).withGames(getGames(match.getMatchLink()).collect(toList())));
                });
            });
        });
        return allMatches;
    }

    private List<Game> downloadAllGamesForTesting() {
        List<Game> games = new ArrayList<>();
        parsedMatchesRetriever.getSeasonIDs()
                .forEach(seasonId -> getLigaLinks(seasonId)//
                        .forEach(ligaLink -> getMatchLinks(ligaLink)//
                                .forEach(matchLink -> games.addAll(getGames(matchLink).collect(toList())))));
        return games;
    }
}
