package jkickerstats.persistence;

import jkickerstats.domain.Game;

import java.util.Objects;

public class FileGame {
    private final String homePlayer1;
    private final String homePlayer2;
    private final Integer homeScore;
    private final String guestPlayer1;
    private final String guestPlayer2;
    private final Integer guestScore;
    private final Integer position;
    private final Boolean doubleMatch;

    private FileGame() {
        homePlayer1 = null;
        doubleMatch = null;
        position = null;
        guestScore = null;
        guestPlayer2 = null;
        guestPlayer1 = null;
        homeScore = null;
        homePlayer2 = null;
    }

    public FileGame(String homePlayer1,
                    String homePlayer2,
                    int homeScore, String guestPlayer1, String guestPlayer2, int guestScore, int position, boolean doubleMatch) {
        this.homePlayer1 = homePlayer1;
        this.homePlayer2 = homePlayer2;
        this.homeScore = homeScore;
        this.guestPlayer1 = guestPlayer1;
        this.guestPlayer2 = guestPlayer2;
        this.guestScore = guestScore;
        this.position = position;
        this.doubleMatch = doubleMatch;
    }

    public static FileGame from(Game game) {
        return new FileGame(game.getHomePlayer1(),
                game.getHomePlayer2(),
                game.getHomeScore(),
                game.getGuestPlayer1(),
                game.getGuestPlayer2(),
                game.getGuestScore(),
                game.getPosition(),
                game.isDoubleMatch());
    }

    public Game toGame() {
        return Game.createGame()
                .withDoubleMatch(doubleMatch)
                .withGuestPlayer1(guestPlayer1)
                .withGuestPlayer2(guestPlayer2)
                .withGuestScore(guestScore)
                .withHomeScore(homeScore)
                .withHomePlayer1(homePlayer1)
                .withHomePlayer2(homePlayer2)
                .withPosition(position);
    }

    public boolean isDoubleMatch() {
        return doubleMatch;
    }

    public int getPosition() {
        return position;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public String getGuestPlayer2() {
        return guestPlayer2;
    }

    public String getGuestPlayer1() {
        return guestPlayer1;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public String getHomePlayer2() {
        return homePlayer2;
    }

    public String getHomePlayer1() {
        return homePlayer1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileGame fileGame = (FileGame) o;
        return Objects.equals(homeScore, fileGame.homeScore) &&
                Objects.equals(guestScore, fileGame.guestScore) &&
                Objects.equals(position, fileGame.position) &&
                Objects.equals(doubleMatch, fileGame.doubleMatch) &&
                Objects.equals(homePlayer1, fileGame.homePlayer1) &&
                Objects.equals(homePlayer2, fileGame.homePlayer2) &&
                Objects.equals(guestPlayer1, fileGame.guestPlayer1) &&
                Objects.equals(guestPlayer2, fileGame.guestPlayer2);
    }

    @Override
    public String toString() {
        return "FileGame{" +
                "homePlayer1='" + homePlayer1 + '\'' +
                ", homePlayer2='" + homePlayer2 + '\'' +
                ", homeScore=" + homeScore +
                ", guestPlayer1='" + guestPlayer1 + '\'' +
                ", guestPlayer2='" + guestPlayer2 + '\'' +
                ", guestScore=" + guestScore +
                ", position=" + position +
                ", doubleMatch=" + doubleMatch +
                '}';
    }
}
