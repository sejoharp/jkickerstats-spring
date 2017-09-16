package jkickerstats.interfaces;

import jkickerstats.domain.MatchPersister;
import jkickerstats.domain.MatchLister;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import jkickerstats.types.Match.MatchBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class StatsUpdater {
    private static final Logger LOG = Logger.getLogger(StatsUpdater.class.getName());
    private static String SEASONS_URL = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";

    private final PageParser pageParser;
    private final PageDownloader pageDownloader;
    private final MatchLister matchLister;
    private final MatchPersister persister;

    @Autowired
    public StatsUpdater(PageParser pageParser, PageDownloader pageDownloader, MatchLister matchLister, MatchPersister persister) {
        this.pageParser = pageParser;
        this.pageDownloader = pageDownloader;
        this.matchLister = matchLister;
        this.persister = persister;
    }

    public void updateStats() {
        LOG.info("updater batch started.");
        long matchesCountBefore = matchLister.countMatches();
        if (matchLister.noMatchesAvailable()) {
            getAllData();
        } else {
            updateData();
        }
        long matchesCountAfter = matchLister.countMatches();
        LOG.info(String.format("updater batch finished and found %s new matches.",
                matchesCountAfter - matchesCountBefore));
    }

    protected List<Game> downloadAllGames() {
        List<Game> games = new ArrayList<>();
        getSeasonIDs().forEach(seasonId -> getLigaLinks(seasonId)//
                .forEach(ligaLink -> getMatchLinks(ligaLink)//
                        .forEach(matchLink -> games.addAll(getGames(matchLink)))));
        return games;
    }

    protected List<Match> downloadAllMatches() {
        List<Match> allMatches = new ArrayList<>();
        for (Integer seasonId : getSeasonIDs()) {
            System.out.println("processing seasonId:" + seasonId);
            for (String ligaLink : getLigaLinks(seasonId)) {
                System.out.println("processing ligalink:" + ligaLink);
                for (Match match : getMatches(ligaLink)) {
                    System.out.println("processing match:" + match.getMatchDate());
                    allMatches.add(new MatchBuilder(match)//
                            .withGames(getGames(match.getMatchLink()))//
                            .build());
                }
            }
        }
        return allMatches;
    }

    protected void getAllData() {
        getSeasonIDs().stream()//
                .map(this::getLigaLinks)//
                .flatMap(links -> links.stream())//
                .map(this::getMatches)//
                .flatMap(matchLists -> matchLists.stream())//
                .map(this::createMatchWithGames)//
                .forEach(persister::save);
    }

    protected void updateData() {
        Integer seasonId = getCurrentSeasonId(getSeasonIDs());
        getLigaLinks(seasonId)//
                .stream()//
                .map(this::getMatches)//
                .flatMap(lists -> lists.stream())//
                .filter(matchLister::isNewMatch)//
                .map(this::createMatchWithGames)//
                .forEach(persister::save);
    }

    private Match createMatchWithGames(Match match) {
        return new Match.MatchBuilder(match)//
                .withGames(getGames(match.getMatchLink()))//
                .build();
    }

    static int getCurrentSeasonId(List<Integer> seasons) {
        return seasons.stream()
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("no seasons found."));
    }

    protected List<Game> getGames(String matchLink) {
        Document matchDoc = pageDownloader.downloadPage(matchLink);
        try {
            return pageParser.findGames(matchDoc);
        } catch (Exception e) {
            LOG.severe("processing match: " + matchLink);
            throw e;
        }
    }

    protected List<String> getMatchLinks(String ligaLink) {
        Document ligaDoc = pageDownloader.downloadPage(ligaLink);
        return pageParser.findMatchLinks(ligaDoc);
    }

    protected List<Match> getMatches(String ligaLink) {
        Document ligaDoc = pageDownloader.downloadPage(ligaLink);
        try {
            return pageParser.findMatches(ligaDoc);
        } catch (Exception e) {
            LOG.severe("processing liga: " + ligaLink);
            throw e;
        }
    }

    protected List<String> getLigaLinks(Integer seasonId) {
        Document seasonDoc = pageDownloader.downloadSeason(seasonId);
        return pageParser.findLigaLinks(seasonDoc);
    }

    protected List<Integer> getSeasonIDs() {
        Document seasonsDoc = pageDownloader.downloadPage(SEASONS_URL);
        return pageParser.findSeasonIDs(seasonsDoc);
    }
}
