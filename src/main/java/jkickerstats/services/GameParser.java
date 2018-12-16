package jkickerstats.services;

import jkickerstats.domain.Game;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

public class GameParser {
    static Stream<Game> findGames(Document doc) {
        Elements gameSnippets = filterGameSnippets(doc);
        return isValidGameList(gameSnippets) ? extractGames(gameSnippets) : Stream.empty();
    }

    static Game createGame(final boolean withImages, Element snippet) {
        Elements rawPlayerNames = snippet.select("td a");
        Boolean doubleMatch = isDoubleMatch(snippet);
        return Game.createGame()
                .withDoubleMatch(doubleMatch)
                .withPosition(parseGamePosition(snippet))
                .withHomeScore(parseHomeScore(snippet, withImages))
                .withGuestScore(parseGuestScore(snippet, withImages))
                .withHomePlayer1(parsePlayerName(rawPlayerNames, 0))
                .withHomePlayer2(parseDoublePlayerName(rawPlayerNames, 1, doubleMatch))
                .withGuestPlayer1(parseGuestPlayerName(rawPlayerNames, doubleMatch))
                .withGuestPlayer2(parseDoublePlayerName(rawPlayerNames, 3, doubleMatch));
    }

    static boolean isValidGameList(Elements elements) {
        if (elements.isEmpty()) {
            return false;
        } else {
            int columnCount = elements.first().children().size();
            return columnCount == 6 || columnCount == 4;
        }
    }

    static Elements filterGameSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(5) > tbody");
        return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
    }

    static int parseHomeScore(Element doc, boolean imagesAvailable) {
        return parseScore(doc, imagesAvailable, 0);
    }

    static int parseGuestScore(Element doc, boolean imagesAvailable) {
        return parseScore(doc, imagesAvailable, 1);
    }

    private static int parseScore(Element doc, boolean imagesAvailable, int scorePosition) {
        int index = imagesAvailable ? 3 : 2;
        String scoreString = doc.children().eq(index).text().trim();
        return StringUtils.isEmpty(scoreString) ?
                0 :
                Optional.of(scoreString)
                        .map(scores -> scores.split(":"))
                        .filter(scores -> scores.length == 2)
                        .map(scores -> Integer.parseInt(scores[scorePosition]))
                        .orElse(0);
    }

    static Boolean isDoubleMatch(Element gameDoc) {
        return gameDoc.select("td a").size() == 4;
    }

    static Integer parseGamePosition(Element gameDoc) {
        return Integer.parseInt(gameDoc.children().first().text());
    }

    private static boolean hasImages(Elements elements) {
        return elements.first().children().size() == 6;
    }

    private static String parseDoublePlayerName(Elements rawPlayerNames, int position, boolean doubleMatch) {
        if (doubleMatch) {
            return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
        } else
            return null;
    }

    private static String parsePlayerName(Elements rawPlayerNames, int position) {
        return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
    }

    private static String parseGuestPlayerName(Elements rawPlayerNames, boolean doubleMatch) {
        int position = doubleMatch ? 2 : 1;
        return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
    }

    private static Stream<Game> extractGames(Elements gameSnippets) {
        final boolean withImages = hasImages(gameSnippets);
        return gameSnippets.stream()
                .map(snippet -> createGame(withImages, snippet));
    }
}
