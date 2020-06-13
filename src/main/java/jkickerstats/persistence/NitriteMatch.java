package jkickerstats.persistence;

import jkickerstats.domain.Game;
import jkickerstats.domain.Match;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class NitriteMatch implements Serializable {
    private String homeTeam;
    private String guestTeam;
    private int homeGoals;
    private int guestGoals;
    private int homeScore;
    private int guestScore;
    private Date matchDate;
    private int matchDay;
    private List<NitriteGame> games;

    public static NitriteMatch fromMatch(Match match) {
        return Builder
                .aNitriteMatch()
                .homeTeam(match.getHomeTeam())
                .guestTeam(match.getGuestTeam())
                .homeGoals(match.getHomeGoals())
                .guestGoals(match.getGuestGoals())
                .homeScore(match.getHomeScore())
                .guestScore(match.getGuestScore())
                .matchDate(match.getMatchDate())
                .matchDay(match.getMatchDay())
                .games(fromGames(match.getGames()))
                .build();
    }

    private static List<NitriteGame> fromGames(List<Game> games) {
        return games
                .stream()
                .map(game -> NitriteGame.fromGame(game))
                .collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NitriteMatch that = (NitriteMatch) o;
        return homeGoals == that.homeGoals &&
                guestGoals == that.guestGoals &&
                homeScore == that.homeScore &&
                guestScore == that.guestScore &&
                matchDay == that.matchDay &&
                Objects.equals(homeTeam, that.homeTeam) &&
                Objects.equals(guestTeam, that.guestTeam) &&
                Objects.equals(matchDate, that.matchDate) &&
                Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, guestTeam, homeGoals, guestGoals, homeScore, guestScore, matchDate, matchDay, games);
    }

    @Override
    public String toString() {
        return "NitriteMatch{" +
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

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getGuestGoals() {
        return guestGoals;
    }

    public void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    public List<NitriteGame> getGames() {
        return games;
    }

    public void setGames(List<NitriteGame> games) {
        this.games = games;
    }

    public Match toMatch() {
        return Match.createMatch()
                .withGuestScore(getGuestScore())
                .withGuestTeam(guestTeam)
                .withHomeScore(homeScore)
                .withHomeTeam(homeTeam)
                .withMatchDate(matchDate)
                .withMatchDay(matchDay)
                .withHomeGoals(homeGoals)
                .withGuestGoals(guestGoals)
                .withGames(toGames());
    }

    private List<Game> toGames() {
        return games
                .stream()
                .map(NitriteGame::toGame)
                .collect(toList());
    }

    public static final class Builder {
        private String homeTeam;
        private String guestTeam;
        private int homeGoals;
        private int guestGoals;
        private int homeScore;
        private int guestScore;
        private Date matchDate;
        private int matchDay;
        private List<NitriteGame> games;

        private Builder() {
        }

        public static Builder aNitriteMatch() {
            return new Builder();
        }

        public Builder homeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
            return this;
        }

        public Builder guestTeam(String guestTeam) {
            this.guestTeam = guestTeam;
            return this;
        }

        public Builder homeGoals(int homeGoals) {
            this.homeGoals = homeGoals;
            return this;
        }

        public Builder guestGoals(int guestGoals) {
            this.guestGoals = guestGoals;
            return this;
        }

        public Builder homeScore(int homeScore) {
            this.homeScore = homeScore;
            return this;
        }

        public Builder guestScore(int guestScore) {
            this.guestScore = guestScore;
            return this;
        }

        public Builder matchDate(Date matchDate) {
            this.matchDate = matchDate;
            return this;
        }

        public Builder matchDay(int matchDay) {
            this.matchDay = matchDay;
            return this;
        }

        public Builder games(List<NitriteGame> games) {
            this.games = games;
            return this;
        }

        public NitriteMatch build() {
            NitriteMatch nitriteMatch = new NitriteMatch();
            nitriteMatch.setHomeTeam(homeTeam);
            nitriteMatch.setGuestTeam(guestTeam);
            nitriteMatch.setHomeGoals(homeGoals);
            nitriteMatch.setGuestGoals(guestGoals);
            nitriteMatch.setHomeScore(homeScore);
            nitriteMatch.setGuestScore(guestScore);
            nitriteMatch.setMatchDate(matchDate);
            nitriteMatch.setMatchDay(matchDay);
            nitriteMatch.setGames(games);
            return nitriteMatch;
        }
    }
}
