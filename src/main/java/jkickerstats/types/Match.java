package jkickerstats.types;

import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;

public class Match {
    private final String homeTeam;
    private final String guestTeam;
    private final int homeGoals;
    private final int guestGoals;
    private final int homeScore;
    private final int guestScore;
    private final Date matchDate;
    private final int matchDay;
    private final List<Game> games;
    private final String matchLink;

    private Match(String homeTeam, String guestTeam, int homeGoals, int guestGoals, int homeScore, int guestScore, Date matchDate, int matchDay, List<Game> games, String matchLink) {
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
        this.homeScore = homeScore;
        this.guestScore = guestScore;
        this.matchDate = matchDate;
        this.matchDay = matchDay;
        this.games = games;
        this.matchLink = matchLink;
    }

    public static Match createMatch(Match match) {
        return new Match(match.homeTeam, match.guestTeam, match.homeGoals, match.guestGoals, match.homeScore, match.guestScore, match.matchDate, match.matchDay, match.games, match.matchLink);
    }

    public static Match createMatch() {
        return new Match(null, null, 0, 0, 0, 0, null, 0, emptyList(), null);
    }

    public Match withMatchLink(String matchLink) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withHomeTeam(String homeTeam) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withGuestTeam(String guestTeam) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withHomeGoals(int homeGoals) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withGuestGoals(int guestGoals) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withHomeScore(int homeScore) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withGuestScore(int guestScore) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withMatchDate(Date matchDate) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withMatchDay(int matchDay) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public Match withGames(List<Game> games) {
        return new Match(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games, matchLink);
    }

    public String getMatchLink() {
        return matchLink;
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

    public int getGuestScore() {
        return guestScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public String toString() {
        return "Match [homeTeam=" + homeTeam + ", guestTeam=" + guestTeam + ", homeGoals=" + homeGoals + ", guestGoals="
                + guestGoals + ", homeScore=" + homeScore + ", guestScore=" + guestScore + ", matchDate=" + matchDate
                + ", matchDay=" + matchDay + ", games=" + games + ", matchLink=" + matchLink + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((games == null) ? 0 : games.hashCode());
        result = prime * result + guestGoals;
        result = prime * result + guestScore;
        result = prime * result
                + ((guestTeam == null) ? 0 : guestTeam.hashCode());
        result = prime * result + homeGoals;
        result = prime * result + homeScore;
        result = prime * result
                + ((homeTeam == null) ? 0 : homeTeam.hashCode());
        result = prime * result
                + ((matchDate == null) ? 0 : matchDate.hashCode());
        result = prime * result + matchDay;
        result = prime * result
                + ((matchLink == null) ? 0 : matchLink.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Match other = (Match) obj;
        if (games == null) {
            if (other.games != null)
                return false;
        } else if (!games.equals(other.games))
            return false;
        if (guestGoals != other.guestGoals)
            return false;
        if (guestScore != other.guestScore)
            return false;
        if (guestTeam == null) {
            if (other.guestTeam != null)
                return false;
        } else if (!guestTeam.equals(other.guestTeam))
            return false;
        if (homeGoals != other.homeGoals)
            return false;
        if (homeScore != other.homeScore)
            return false;
        if (homeTeam == null) {
            if (other.homeTeam != null)
                return false;
        } else if (!homeTeam.equals(other.homeTeam))
            return false;
        if (matchDate == null) {
            if (other.matchDate != null)
                return false;
        } else if (!matchDate.equals(other.matchDate))
            return false;
        if (matchDay != other.matchDay)
            return false;
        if (matchLink == null) {
            if (other.matchLink != null)
                return false;
        } else if (!matchLink.equals(other.matchLink))
            return false;
        return true;
    }
}
