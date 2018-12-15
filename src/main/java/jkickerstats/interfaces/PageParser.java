package jkickerstats.interfaces;

import jkickerstats.types.Game;
import jkickerstats.types.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static jkickerstats.types.Match.createMatch;

@Component
class PageParser {
    private static final String DOMAIN = "https://kickern-hamburg.de";
    private static final String CSS_QUERY_MATCH_DATE = "table.contentpaneopen:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1)";

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

    static String parseHomeTeam(Document doc) {
        Elements teams = doc.select(
                "table.contentpaneopen:nth-child(3) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > h2:nth-child(1)");
        return removeTeamDescriptions(teams.first().text());
    }

    static String parseGuestTeam(Document doc) {
        Elements teams = doc.select(
                "table.contentpaneopen:nth-child(3) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > h2:nth-child(1)");
        return removeTeamDescriptions(teams.last().text());
    }

    static String removeTeamDescriptions(String teamString) {
        return teamString.split("\\(")[0].trim();
    }

    static boolean hasMatchDate(Document doc) {
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String[] dateChunks = rawData.split(",");
        return dateChunks.length == 3;
    }

    static Date parseMatchDate(Document doc, boolean matchDateAvailable) {
        if (matchDateAvailable == false) {
            Calendar matchDate = Calendar.getInstance();
            matchDate.setTimeInMillis(0);
            return matchDate.getTime();
        }
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String rawDate = rawData.split(",")[1];
        return parseDate(rawDate);
    }

    static int parseMatchDay(Document doc, boolean matchDateAvailable) {
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String[] dateChunks = rawData.split(",");
        int dateChunk = matchDateAvailable ? 2 : 1;
        String matchDayString = dateChunks[dateChunk].split("\\.")[0].trim();
        return Integer.parseInt(matchDayString);
    }

    static int parseHomeScore(Element doc, boolean imagesAvailable) {
        String[] score = parseScore(doc, imagesAvailable);
        if (score.length < 1 || StringUtils.isEmpty(score[0].trim())){
            return 0;
        }
        return Integer.parseInt(score[0].trim());
    }

    static int parseGuestScore(Element doc, boolean imagesAvailable) {
        String[] score = parseScore(doc, imagesAvailable);
        if (score.length < 2 || StringUtils.isEmpty(score[1].trim())){
            return 0;
        }
        return Integer.parseInt(score[1].trim());
    }

    static Date parseDate(String rawDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return dateFormat.parse(rawDate);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    static Stream<String> findLigaLinks(Document doc) {
        Elements elements = doc.select("table.contentpaneopen > tbody > tr > td > a.readon");
        return elements.stream()
                .map(element -> DOMAIN + element.attr("href"))
                .filter(link -> !link.contains("ticker"));
    }

    static Stream<Integer> findSeasonIDs(Document doc) {
        Elements elements = doc.select("select option");
        return elements.stream()
                .map(element -> Integer.valueOf(element.attr("value")));
    }

    static Stream<String> findMatchLinks(Document doc) {
        Elements elements = filterMatchLinkSnippets(doc);
        return elements.stream()
                .filter(PageParser::isValidMatchLink)
                .map(PageParser::parseMatchLink);
    }

    static Stream<Match> findMatches(Document doc) {
        Elements elements = filterMatchSnippets(doc);
        return recursiveFindMatches(elements, 0, new ArrayList<>()).stream();
    }

    static Elements filterMatchLinkSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(6)");
        return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
    }

    static boolean isValidMatchLink(Element element) {
        boolean alreadyPlayed = element.select("a[href]").size() == 4;
        if (!alreadyPlayed) {
            return false;
        }
        String scoreDescription = element.select("td:nth-child(5) small").text();
        return isMatchConfirmed(scoreDescription) && isMatchCompleted(scoreDescription);
    }

    private static String[] parseScore(Element doc, boolean imagesAvailable) {
        int index = imagesAvailable ? 3 : 2;
        String scoreString = doc.children().eq(index).text();
        return scoreString.split(":");
    }

    private static String parseMatchLink(Element element) {
        return DOMAIN + element.select("a[href]").attr("href");
    }

    private static List<Match> recursiveFindMatches(Elements elements, int matchDay, List<Match> result) {
        if (elements.isEmpty())
            return result;
        if (isMatchDayElement(elements.first())) {
            String matchDayString = elements.first().select("i").text();
            matchDay = Integer.parseInt(matchDayString.split("\\.")[0]);
        } else if (isValidMatchLink(elements.first()))
            result.add(parseMatch(elements.first(), matchDay));

        elements.remove(elements.first());
        return recursiveFindMatches(elements, matchDay, result);
    }

    private static Match parseMatch(Element element, int matchDay) {
        return createMatch()
                .withMatchDate(parseMatchDate(element))
                .withMatchDay(matchDay)
                .withHomeScore(parseMatchHomeScore(element))
                .withGuestScore(parseMatchGuestScore(element))
                .withHomeTeam(removeTeamDescriptions(element.children().eq(1).text()))
                .withGuestTeam(removeTeamDescriptions(element.children().eq(2).text()))
                .withGuestGoals(parseMatchGuestGoals(element))
                .withHomeGoals(parseMatchHomeGoals(element))
                .withMatchLink(parseMatchLink(element));
    }

    private static boolean isMatchDayElement(Element element) {
        return element.hasClass("sectiontableheader")
                && element.select("i").text().isEmpty() == false;
    }

    private static boolean isNewMatchFormat(Element element) {
        return element.children().size() == 5;
    }

    private static int parseMatchHomeGoals(Element element) {
        return parseMatchGoals(element, 0);
    }

    private static int parseMatchGuestGoals(Element element) {
        return parseMatchGoals(element, 1);
    }

    private static int parseMatchGoals(Element element, int chunk) {
        if (isNewMatchFormat(element)) {
            String goals = element.children().eq(3).text();
            return Integer.parseInt(goals.split(":")[chunk]);
        } else {
            return 0;
        }
    }

    private static int parseMatchHomeScore(Element element) {
        return parseMatchScore(element, 0);
    }

    private static int parseMatchGuestScore(Element element) {
        return parseMatchScore(element, 1);
    }

    private static Elements filterMatchSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(6)");
        return rawDoc.select("tr.sectiontableheader,tr.sectiontableentry1, tr.sectiontableentry2");
    }

    static Boolean isDoubleMatch(Element gameDoc) {
        return gameDoc.select("td a").size() == 4;
    }

    static Integer parseGamePosition(Element gameDoc) {
        return Integer.parseInt(gameDoc.children().first().text());
    }

    private static int parseMatchScore(Element element, int chunk) {
        String score = element.children().last().text();
        return Integer.parseInt(score.split(":")[chunk]);
    }

    private static Date parseMatchDate(Element element) {
        String rawData = element.select("a").text();
        String[] dateString = rawData.split(",");
        if (dateString.length == 2) {
            return parseDate(dateString[1]);
        } else {
            Calendar matchDate = Calendar.getInstance();
            matchDate.setTimeInMillis(0);
            return matchDate.getTime();
        }
    }

    private static boolean hasImages(Elements elements) {
        return elements.first().children().size() == 6;
    }

    private static boolean isMatchCompleted(String scoreDescription) {
        return !"live".equals(scoreDescription);
    }

    private static boolean isMatchConfirmed(String scoreDescription) {
        return !"unbestätigt".equals(scoreDescription);
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
