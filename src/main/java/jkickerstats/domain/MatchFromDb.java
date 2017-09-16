package jkickerstats.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "matches")
public class MatchFromDb {
    private String id;
    private String homeTeam;
    private String guestTeam;
    private int homeGoals;
    private int guestGoals;
    private int homeScore;
    private int guestScore;
    private Date matchDate;
    private int matchDay;
    private List<GameFromDb> games;

    String getHomeTeam() {
        return homeTeam;
    }

    void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    String getGuestTeam() {
        return guestTeam;
    }

    void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    int getHomeGoals() {
        return homeGoals;
    }

    void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    int getGuestGoals() {
        return guestGoals;
    }

    void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }

    int getHomeScore() {
        return homeScore;
    }

    void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    int getGuestScore() {
        return guestScore;
    }

    void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    Date getMatchDate() {
        return matchDate;
    }

    void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    int getMatchDay() {
        return matchDay;
    }

    void setMatchDay(int matchDay) {
        this.matchDay = matchDay;
    }

    List<GameFromDb> getGames() {
        return games;
    }

    void setGames(List<GameFromDb> games) {
        this.games = games;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
