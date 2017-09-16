package jkickerstats.interfaces;

import jkickerstats.Application;
import jkickerstats.types.Match;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jkickerstats.interfaces.ParsedMatchesRetrieverImpl.getGames;
import static jkickerstats.interfaces.StatsUpdater.getCurrentSeasonId;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class StatsUpdaterTest {
    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

    @Autowired
    private ParsedMatchesRetriever retriever;

    @Ignore
    @Test
    public void createCSVFileWithAllGames() {
        List<Match> matches = statsUpdater.downloadAllMatches();
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
        Stream<Integer> seasonIDs = retriever.getSeasonIDs();
        assertThat(getCurrentSeasonId(seasonIDs)).isEqualTo((12));
    }

    @Ignore
    @Test
    public void updatesData() {
        statsUpdater.updateData();
    }

    @Ignore
    @Test
    public void getsLinks() {
        assertThat(ParsedMatchesRetrieverImpl.getLigaLinks(11)).isNotEmpty();
    }

    @Ignore
    @Test
    public void findsMatches() {
        ParsedMatchesRetrieverImpl.getLigaLinks(11).forEach(ligaLink -> {
            System.out.println("processing liga link: " + ligaLink);
            ParsedMatchesRetrieverImpl.getMatchLinks(ligaLink)//
                    .forEach(matchLink -> {
                        System.out.println("processing match link: " + matchLink);
                        getGames(matchLink).forEach(System.out::println);
                    });
        });

    }

    @Ignore
    @Test
    public void findsAllMatches() {
        ParsedMatchesRetrieverImpl.getLigaLinks(11)//
                .forEach(ligaLink -> ParsedMatchesRetrieverImpl.getMatches(ligaLink)//
                        .forEach(match -> {
                            Match fullMatch = new Match.MatchBuilder(match)//
                                    .withGames(getGames(match.getMatchLink()).collect(toList()))//
                                    .build();
                            System.out.println(String.format("Datum:%s home:%s guest:%s spiele:%s",
                                    fullMatch.getMatchDate(), fullMatch.getHomeTeam(), fullMatch.getGuestTeam(),
                                    fullMatch.getGames().size()));
                        }));
    }
}
