package jkickerstats.interfaces;

import jkickerstats.Application;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class StatsUpdaterTest {
    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

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
        List<Integer> seasonIDs = statsUpdater.getSeasonIDs();
        assertThat(statsUpdater.getCurrentSeasonId(seasonIDs), is(12));
    }

    @Ignore
    @Test
    public void updatesData() {
        statsUpdater.updateData();
    }

    @Ignore
    @Test
    public void getsLinks() {
        assertThat(statsUpdater.getLigaLinks(11), is(not(empty())));
    }

    @Ignore
    @Test
    public void findsMatches() {
        statsUpdater.getLigaLinks(11).forEach(ligaLink -> {
            System.out.println("processing liga link: " + ligaLink);
            statsUpdater.getMatchLinks(ligaLink)//
                    .forEach(matchLink -> {
                        System.out.println("processing match link: " + matchLink);
                        List<Game> games = statsUpdater.getGames(matchLink);
                        System.out.println(games);
                    });
        });

    }

    @Ignore
    @Test
    public void findsAllMatches() {
        statsUpdater.getLigaLinks(11)//
                .forEach(ligaLink -> statsUpdater.getMatches(ligaLink)//
                        .forEach(match -> {
                            Match fullMatch = new Match.MatchBuilder(match)//
                                    .withGames(statsUpdater.getGames(match.getMatchLink()))//
                                    .build();
                            System.out.println(String.format("Datum:%s home:%s guest:%s spiele:%s",
                                    fullMatch.getMatchDate(), fullMatch.getHomeTeam(), fullMatch.getGuestTeam(),
                                    fullMatch.getGames().size()));
                        }));
    }
}
