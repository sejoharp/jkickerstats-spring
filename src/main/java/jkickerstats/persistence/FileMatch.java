package jkickerstats.persistence;

import jkickerstats.domain.Match;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class FileMatch {
    private final String homeTeam;
    private final String guestTeam;
    private final Integer homeGoals;
    private final Integer guestGoals;
    private final Integer homeScore;
    private final Integer guestScore;
    private final Date matchDate;
    private final Integer matchDay;
    private final List<FileGame> games;

    private FileMatch() {
        games = null;
        homeTeam = null;
        matchDay = null;
        matchDate = null;
        guestScore = null;
        homeScore = null;
        guestGoals = null;
        guestTeam = null;
        homeGoals = null;
    }

    public FileMatch(String homeTeam,
                     String guestTeam,
                     int homeGoals,
                     int guestGoals,
                     int homeScore,
                     int guestScore,
                     Date matchDate,
                     int matchDay,
                     List<FileGame> games) {
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
        this.homeScore = homeScore;
        this.guestScore = guestScore;
        this.matchDate = matchDate;
        this.matchDay = matchDay;
        this.games = games;
    }

    public static FileMatch from(Match match) {
        return new FileMatch(match.getHomeTeam(),
                match.getGuestTeam(),
                match.getHomeGoals(),
                match.getGuestGoals(),
                match.getHomeScore(),
                match.getGuestScore(),
                match.getMatchDate(),
                match.getMatchDay(),
                match.getGames().stream().map(FileGame::from).collect(toList())
        );
    }

    Match toMatch() {
        return Match.createMatch()
                .withGames(games.stream().map(FileGame::toGame).collect(toList()))
                .withGuestTeam(guestTeam)
                .withGuestScore(guestScore)
                .withGuestGoals(guestGoals)
                .withHomeTeam(homeTeam)
                .withHomeScore(homeScore)
                .withHomeGoals(homeGoals)
                .withMatchDate(matchDate)
                .withMatchDay(matchDay);
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getGuestGoals() {
        return guestGoals;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }


    public Date getMatchDate() {
        return matchDate;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public List<FileGame> getGames() {
        return games;
    }

    public String fileName() {
        return String.format("%s_%s_%s.json", formatDate(),
                removeSpaces(homeTeam),
                removeSpaces(guestTeam));
    }

    private String removeSpaces(String team) {
        return StringUtils.replace(team, " ", "");
    }

    private String formatDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(matchDate);
    }

    @Override
    public String toString() {
        return "FileMatch{" +
                "homeTeam='" + homeTeam + '\'' +
                ", guestTeam='" + guestTeam + '\'' +
                ", homeGoals=" + homeGoals +
                ", guestGoals=" + guestGoals +
                ", homeScore=" + homeScore +
                ", guestScore=" + guestScore +
                ", matchDate=" + matchDate +
                ", matchDay=" + matchDay +
                ", games=" + games +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMatch fileMatch = (FileMatch) o;
        return Objects.equals(homeGoals, fileMatch.homeGoals) &&
                Objects.equals(guestGoals, fileMatch.guestGoals) &&
                Objects.equals(homeScore, fileMatch.homeScore) &&
                Objects.equals(guestScore, fileMatch.guestScore) &&
                Objects.equals(matchDay, fileMatch.matchDay) &&
                Objects.equals(homeTeam, fileMatch.homeTeam) &&
                Objects.equals(guestTeam, fileMatch.guestTeam) &&
                Objects.equals(matchDate, fileMatch.matchDate) &&
                Objects.equals(games, fileMatch.games);
    }
}
