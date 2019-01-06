package jkickerstats.services;

import jkickerstats.domain.Game;
import jkickerstats.domain.Match;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jkickerstats.domain.Match.createMatch;
import static jkickerstats.services.PageDownloader.downloadPage;
import static jkickerstats.services.PageDownloader.downloadSeason;
import static jkickerstats.services.PageParser.*;

@Component
public class ParsedMatchesRetrieverImpl implements ParsedMatchesRetriever {
    private static final Logger LOG = Logger.getLogger(ParsedMatchesRetrieverImpl.class.getName());
    private static final String SEASONS_URL = "http://kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";

    public Stream<Integer> getSeasonIDs() {
        Document seasonsDoc = downloadPage(SEASONS_URL);
        return findSeasonIDs(seasonsDoc);
    }

    @Override
    public Stream<Match> getViaDocs(Integer seasonId) {
        return getLigaDocuments(seasonId)
                .map(ParsedMatchesRetrieverImpl::parseMatches)//
                .flatMap(Function.identity());
    }
    public Match downloadGamesFromMatch(Match match) {
        return createMatch(match)
                .withGames(getGames(match.getMatchLink()).collect(toList()));
    }

    static Stream<Game> getGames(String matchLink) {
        Document matchDoc = downloadPage(matchLink);
        try {
            return GameParser.findGames(matchDoc);
        } catch (Exception e) {
            LOG.severe("processing createMatch: " + matchLink);
            throw e;
        }
    }

    static Stream<String> getMatchLinks(String ligaLink) {
        Document ligaDoc = downloadPage(ligaLink);
        return MatchParser.findMatchLinks(ligaDoc);
    }

    static Stream<Match> getMatches(String ligaLink) {
        Document ligaDoc = downloadPage(ligaLink);
        try {
            return MatchParser.findMatches(ligaDoc);
        } catch (Exception e) {
            LOG.severe("processing liga: " + ligaLink);
            throw e;
        }
    }

    private static Stream<Match> parseMatches(Document ligaDoc) {
        try {
            return MatchParser.findMatches(ligaDoc);
        } catch (Exception e) {
            LOG.severe("processing liga: " + ligaDoc.location());
            throw e;
        }
    }

    static Stream<String> getLigaLinks(Integer seasonId) {
        Document seasonDoc = downloadSeason(seasonId);
        return findLigaLinks(seasonDoc);
    }

    static Stream<Document> getLigaDocuments(Integer seasonId) {
        Document seasonDoc = downloadSeason(seasonId);
        if (isZwischenSaison2018(seasonDoc)) {
            return Stream.of(seasonDoc);
        }
        return findLigaLinks(seasonDoc)
                .map(PageDownloader::downloadPage);
    }
}
