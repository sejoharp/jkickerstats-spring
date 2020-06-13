package jkickerstats.persistence;

import jkickerstats.domain.Game;

import java.io.Serializable;
import java.util.Objects;

public class NitriteGame implements Serializable {
    private String homePlayer1;
    private String homePlayer2;
    private int homeScore;
    private String guestPlayer1;
    private String guestPlayer2;
    private int guestScore;
    private int position;
    private boolean doubleGame;

    public static NitriteGame fromGame(Game game) {
        return Builder
                .aNitriteGame()
                .homePlayer1(game.getHomePlayer1())
                .homePlayer2(game.getHomePlayer2())
                .homeScore(game.getHomeScore())
                .guestPlayer1(game.getGuestPlayer1())
                .guestPlayer2(game.getGuestPlayer2())
                .guestScore(game.getGuestScore())
                .position(game.getPosition())
                .doubleMatch(game.isDoubleMatch())
                .build();
    }

    public String getHomePlayer1() {
        return homePlayer1;
    }

    public void setHomePlayer1(String homePlayer1) {
        this.homePlayer1 = homePlayer1;
    }

    public String getHomePlayer2() {
        return homePlayer2;
    }

    public void setHomePlayer2(String homePlayer2) {
        this.homePlayer2 = homePlayer2;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public String getGuestPlayer1() {
        return guestPlayer1;
    }

    public void setGuestPlayer1(String guestPlayer1) {
        this.guestPlayer1 = guestPlayer1;
    }

    public String getGuestPlayer2() {
        return guestPlayer2;
    }

    public void setGuestPlayer2(String guestPlayer2) {
        this.guestPlayer2 = guestPlayer2;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isDoubleGame() {
        return doubleGame;
    }

    public void setDoubleGame(boolean doubleGame) {
        this.doubleGame = doubleGame;
    }

    @Override
    public String toString() {
        return "NitriteGame{" +
                "homePlayer1='" + homePlayer1 + '\'' +
                ", homePlayer2='" + homePlayer2 + '\'' +
                ", homeScore=" + homeScore +
                ", guestPlayer1='" + guestPlayer1 + '\'' +
                ", guestPlayer2='" + guestPlayer2 + '\'' +
                ", guestScore=" + guestScore +
                ", position=" + position +
                ", doubleGame=" + doubleGame +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NitriteGame that = (NitriteGame) o;
        return homeScore == that.homeScore &&
                guestScore == that.guestScore &&
                position == that.position &&
                doubleGame == that.doubleGame &&
                Objects.equals(homePlayer1, that.homePlayer1) &&
                Objects.equals(homePlayer2, that.homePlayer2) &&
                Objects.equals(guestPlayer1, that.guestPlayer1) &&
                Objects.equals(guestPlayer2, that.guestPlayer2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleGame);
    }

    public Game toGame() {
        return Game.createGame()
                .withDoubleMatch(doubleGame)
                .withHomePlayer1(homePlayer1)
                .withHomePlayer2(homePlayer2)
                .withHomeScore(homeScore)
                .withGuestPlayer1(guestPlayer1)
                .withGuestPlayer2(guestPlayer2)
                .withGuestScore(guestScore)
                .withPosition(position);
    }

    public static final class Builder {
        private String homePlayer1;
        private String homePlayer2;
        private int homeScore;
        private String guestPlayer1;
        private String guestPlayer2;
        private int guestScore;
        private int position;
        private boolean doubleGame;

        private Builder() {
        }

        public static Builder aNitriteGame() {
            return new Builder();
        }

        public Builder homePlayer1(String homePlayer1) {
            this.homePlayer1 = homePlayer1;
            return this;
        }

        public Builder homePlayer2(String homePlayer2) {
            this.homePlayer2 = homePlayer2;
            return this;
        }

        public Builder homeScore(int homeScore) {
            this.homeScore = homeScore;
            return this;
        }

        public Builder guestPlayer1(String guestPlayer1) {
            this.guestPlayer1 = guestPlayer1;
            return this;
        }

        public Builder guestPlayer2(String guestPlayer2) {
            this.guestPlayer2 = guestPlayer2;
            return this;
        }

        public Builder guestScore(int guestScore) {
            this.guestScore = guestScore;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public Builder doubleMatch(boolean doubleMatch) {
            this.doubleGame = doubleMatch;
            return this;
        }

        public NitriteGame build() {
            NitriteGame nitriteGame = new NitriteGame();
            nitriteGame.setHomePlayer1(homePlayer1);
            nitriteGame.setHomePlayer2(homePlayer2);
            nitriteGame.setHomeScore(homeScore);
            nitriteGame.setGuestPlayer1(guestPlayer1);
            nitriteGame.setGuestPlayer2(guestPlayer2);
            nitriteGame.setGuestScore(guestScore);
            nitriteGame.setPosition(position);
            nitriteGame.setDoubleGame(doubleGame);
            return nitriteGame;
        }
    }
}
