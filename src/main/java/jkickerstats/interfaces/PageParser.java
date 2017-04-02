package jkickerstats.interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import jkickerstats.types.Game;
import jkickerstats.types.Match;

@Component
class PageParser {
    public static final String DOMAIN = "http://www.kickern-hamburg.de";
    public static final String CSS_QUERY_MATCH_DATE = "table.contentpaneopen:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1)";

    public List<Game> findGames(Document doc) {
        Elements gameSnippets = filterGameSnippets(doc);
        return isValidGameList(gameSnippets) ? extractGames(gameSnippets) : new ArrayList<>();
    }

    private List<Game> extractGames(Elements gameSnippets) {
        final boolean withImages = hasImages(gameSnippets);
        return gameSnippets.stream()//
                .map(snippet -> createGame(withImages, snippet))//
                .collect(Collectors.toList());
    }

    protected Game createGame(final boolean withImages, Element snippet) {
        Elements rawPlayerNames = snippet.select("td a");
        Boolean doubleMatch = isDoubleMatch(snippet);
        return new Game.GameBuilder()//
                .withDoubleMatch(doubleMatch)//
                .withPosition(parseGamePosition(snippet))//
                .withHomeScore(parseHomeScore(snippet, withImages))//
                .withGuestScore(parseGuestScore(snippet, withImages))//
                .withHomePlayer1(parsePlayerName(rawPlayerNames, 0))//
                .withHomePlayer2(parseDoublePlayerName(rawPlayerNames, 1, doubleMatch))//
                .withGuestPlayer1(parseGuestPlayerName(rawPlayerNames, doubleMatch))//
                .withGuestPlayer2(parseDoublePlayerName(rawPlayerNames, 3, doubleMatch))//
                .build();
    }

    protected boolean hasImages(Elements elements) {
        return elements.first().children().size() == 6;
    }

    protected boolean isValidGameList(Elements elements) {
        if (elements.isEmpty()) {
            return false;
        } else {
            int columnCount = elements.first().children().size();
            return columnCount == 6 || columnCount == 4;
        }
    }

    protected Elements filterGameSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(5) > tbody");
        return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
    }

    protected String parseHomeTeam(Document doc) {
        Elements teams = doc.select(
                "table.contentpaneopen:nth-child(3) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > h2:nth-child(1)");
        return removeTeamDescriptions(teams.first().text());
    }

    protected String parseGuestTeam(Document doc) {
        Elements teams = doc.select(
                "table.contentpaneopen:nth-child(3) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > h2:nth-child(1)");
        return removeTeamDescriptions(teams.last().text());
    }

    protected String removeTeamDescriptions(String teamString) {
        return teamString.split("\\(")[0].trim();
    }

    protected boolean hasMatchDate(Document doc) {
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String[] dateChunks = rawData.split(",");
        return dateChunks.length == 3;
    }

    protected Date parseMatchDate(Document doc, boolean matchDateAvailable) {
        if (matchDateAvailable == false) {
            Calendar matchDate = Calendar.getInstance();
            matchDate.setTimeInMillis(0);
            return matchDate.getTime();
        }
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String rawDate = rawData.split(",")[1];
        return parseDate(rawDate);
    }

    protected int parseMatchDay(Document doc, boolean matchDateAvailable) {
        String rawData = doc.select(CSS_QUERY_MATCH_DATE).text();
        String[] dateChunks = rawData.split(",");
        int dateChunk = matchDateAvailable ? 2 : 1;
        String matchDayString = dateChunks[dateChunk].split("\\.")[0].trim();
        return Integer.parseInt(matchDayString);
    }

    protected String[] parseScore(Element doc, boolean imagesAvailable) {
        int index = imagesAvailable ? 3 : 2;
        String scoreString = doc.children().eq(index).text();
        return scoreString.split(":");
    }

    protected int parseHomeScore(Element doc, boolean imagesAvailable) {
        String homescore = parseScore(doc, imagesAvailable)[0].trim();
        return Integer.parseInt(homescore);
    }

    protected int parseGuestScore(Element doc, boolean imagesAvailable) {
        String guestscore = parseScore(doc, imagesAvailable)[1].trim();
        return Integer.parseInt(guestscore);
    }

    protected Date parseDate(String rawDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            return dateFormat.parse(rawDate);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> findLigaLinks(Document doc) {
        Elements elements = doc.select("table.contentpaneopen > tbody > tr > td > a.readon");
        return elements.stream()//
                .map(element -> DOMAIN + element.attr("href"))
                .filter(link -> !link.contains("ticker"))
                .collect(Collectors.toList());
    }

    public List<Integer> findSeasonIDs(Document doc) {
        Elements elements = doc.select("select option");
        return elements.stream()//
                .map(element -> Integer.valueOf(element.attr("value")))//
                .collect(Collectors.toList());
    }

    public List<String> findMatchLinks(Document doc) {
        Elements elements = filterMatchLinkSnippets(doc);
        return elements.stream()//
                .filter(this::isValidMatchLink)//
                .map(this::parseMatchLink)//
                .collect(Collectors.toList());
    }

    protected String parseMatchLink(Element element) {
        return DOMAIN + element.select("a[href]").attr("href");
    }

    public List<Match> findMatches(Document doc) {
        Elements elements = filterMatchSnippets(doc);
        return recursiveFindMatches(elements, 0, new ArrayList<>());
    }

    public List<Match> recursiveFindMatches(Elements elements, int matchDay, List<Match> result) {
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

    protected boolean isMatchDayElement(Element element) {
        return element.hasClass("sectiontableheader") && element.select("i").text().isEmpty() == false;
    }

    protected Match parseMatch(Element element, int matchDay) {
        return new Match.MatchBuilder()//
                .withMatchDate(parseMatchDate(element))//
                .withMatchDay(matchDay)//
                .withHomeScore(parseMatchHomeScore(element))//
                .withGuestScore(parseMatchGuestScore(element))//
                .withHomeTeam(removeTeamDescriptions(element.children().eq(1).text()))//
                .withGuestTeam(removeTeamDescriptions(element.children().eq(2).text()))//
                .withGuestGoals(parseMatchGuestGoals(element))//
                .withHomeGoals(parseMatchHomeGoals(element))//
                .withMatchLink(parseMatchLink(element))//
                .build();
    }

    protected boolean isNewMatchFormat(Element element) {
        return element.children().size() == 5;
    }

    protected int parseMatchHomeGoals(Element element) {
        return parseMatchGoals(element, 0);
    }

    protected int parseMatchGuestGoals(Element element) {
        return parseMatchGoals(element, 1);
    }

    private int parseMatchGoals(Element element, int chunk) {
        if (isNewMatchFormat(element)) {
            String goals = element.children().eq(3).text();
            return Integer.parseInt(goals.split(":")[chunk]);
        } else {
            return 0;
        }
    }

    protected int parseMatchHomeScore(Element element) {
        return parseMatchScore(element, 0);
    }

    protected int parseMatchGuestScore(Element element) {
        return parseMatchScore(element, 1);
    }

    private int parseMatchScore(Element element, int chunk) {
        String score = element.children().last().text();
        return Integer.parseInt(score.split(":")[chunk]);
    }

    protected Date parseMatchDate(Element element) {
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

    protected Elements filterMatchLinkSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(6)");
        return rawDoc.select("tr.sectiontableentry1, tr.sectiontableentry2");
    }

    protected Elements filterMatchSnippets(Document doc) {
        Elements rawDoc = doc.select("table.contentpaneopen:nth-child(6)");
        return rawDoc.select("tr.sectiontableheader,tr.sectiontableentry1, tr.sectiontableentry2");
    }

    protected boolean isValidMatchLink(Element element) {
        boolean alreadyPlayed = element.select("a").size() == 4;
        if (alreadyPlayed == false) {
            return false;
        }
        String scoreDescription = element.select("td:nth-child(5) small").text();
        return isMatchUnconfirmed(scoreDescription) == false && isMatchRunning(scoreDescription) == false;
    }

    protected boolean isMatchRunning(String scoreDescription) {
        return "live".equals(scoreDescription);
    }

    protected boolean isMatchUnconfirmed(String scoreDescription) {
        return "unbestätigt".equals(scoreDescription);
    }

    protected Boolean isDoubleMatch(Element gameDoc) {
        return gameDoc.select("td a").size() == 4;
    }

    protected Integer parseGamePosition(Element gameDoc) {
        return Integer.parseInt(gameDoc.children().first().text());
    }

    protected String parseDoublePlayerName(Elements rawPlayerNames, int position, boolean doubleMatch) {
        if (doubleMatch) {
            return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
        } else
            return null;
    }

    protected String parsePlayerName(Elements rawPlayerNames, int position) {
        return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
    }

    protected String parseGuestPlayerName(Elements rawPlayerNames, boolean doubleMatch) {
        int position = doubleMatch ? 2 : 1;
        return rawPlayerNames.size() > position ? rawPlayerNames.get(position).text() : "";
    }
}
