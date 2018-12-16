package jkickerstats.interfaces;

import jkickerstats.types.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static jkickerstats.types.Match.createMatch;

public class MatchParser {
    static final String DOMAIN = "https://kickern-hamburg.de";
    private static final String MATCH_DATE_CSS_SELECTOR = "table.contentpaneopen:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1)";

    static String removeTeamDescriptions(String teamString) {
        return teamString.split("\\(")[0].trim();
    }

    static int parseMatchDay(Document doc, boolean matchDateAvailable) {
        String rawData = doc.select(MATCH_DATE_CSS_SELECTOR).text();
        String[] dateChunks = rawData.split(",");
        int dateChunk = matchDateAvailable ? 2 : 1;
        String matchDayString = dateChunks[dateChunk].split("\\.")[0].trim();
        return Integer.parseInt(matchDayString);
    }

    static Date parseDate(String rawDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return dateFormat.parse(rawDate);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    static Stream<String> findMatchLinks(Document doc) {
        Elements elements = filterMatchLinkSnippets(doc);
        return elements.stream()
                .filter(MatchParser::isValidMatchLink)
                .map(MatchParser::parseMatchLink);
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

    private static boolean isMatchCompleted(String scoreDescription) {
        return !"live".equals(scoreDescription);
    }

    private static boolean isMatchConfirmed(String scoreDescription) {
        return !"unbestätigt".equals(scoreDescription);
    }
}
