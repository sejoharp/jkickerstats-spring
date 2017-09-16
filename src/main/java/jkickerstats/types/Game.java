package jkickerstats.types;

public class Game {
    private final String homePlayer1;
    private final String homePlayer2;
    private final int homeScore;
    private final String guestPlayer1;
    private final String guestPlayer2;
    private final int guestScore;
    private final int position;
    private final boolean doubleMatch;

    private Game(String homePlayer1, String homePlayer2, int homeScore, String guestPlayer1, String guestPlayer2, int guestScore, int position, boolean doubleMatch) {
        this.homePlayer1 = homePlayer1;
        this.homePlayer2 = homePlayer2;
        this.homeScore = homeScore;
        this.guestPlayer1 = guestPlayer1;
        this.guestPlayer2 = guestPlayer2;
        this.guestScore = guestScore;
        this.position = position;
        this.doubleMatch = doubleMatch;
    }

    public static Game game(Game game) {
        return new Game(game.homePlayer1, game.homePlayer2, game.homeScore, game.guestPlayer1, game.guestPlayer2, game.guestScore, game.position, game.doubleMatch);
    }

    public static Game game() {
        return new Game(null, null, 0, null, null, 0, 0, false);
    }

    public Game withHomePlayer1(String homePlayer1) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withHomePlayer2(String homePlayer2) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withHomeScore(int homeScore) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withGuestPlayer1(String guestPlayer1) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withGuestPlayer2(String guestPlayer2) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withGuestScore(int guestScore) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withPosition(int position) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public Game withDoubleMatch(boolean doubleMatch) {
        return new Game(homePlayer1, homePlayer2, homeScore, guestPlayer1, guestPlayer2, guestScore, position, doubleMatch);
    }

    public String getHomePlayer1() {
        return homePlayer1;
    }

    public String getHomePlayer2() {
        return homePlayer2;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public String getGuestPlayer1() {
        return guestPlayer1;
    }

    public String getGuestPlayer2() {
        return guestPlayer2;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public int getPosition() {
        return position;
    }

    public boolean isDoubleMatch() {
        return doubleMatch;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (doubleMatch ? 1231 : 1237);
        result = prime * result
                + ((guestPlayer1 == null) ? 0 : guestPlayer1.hashCode());
        result = prime * result
                + ((guestPlayer2 == null) ? 0 : guestPlayer2.hashCode());
        result = prime * result + guestScore;
        result = prime * result
                + ((homePlayer1 == null) ? 0 : homePlayer1.hashCode());
        result = prime * result
                + ((homePlayer2 == null) ? 0 : homePlayer2.hashCode());
        result = prime * result + homeScore;
        result = prime * result + position;
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
        Game other = (Game) obj;
        if (doubleMatch != other.doubleMatch)
            return false;
        if (guestPlayer1 == null) {
            if (other.guestPlayer1 != null)
                return false;
        } else if (!guestPlayer1.equals(other.guestPlayer1))
            return false;
        if (guestPlayer2 == null) {
            if (other.guestPlayer2 != null)
                return false;
        } else if (!guestPlayer2.equals(other.guestPlayer2))
            return false;
        if (guestScore != other.guestScore)
            return false;
        if (homePlayer1 == null) {
            if (other.homePlayer1 != null)
                return false;
        } else if (!homePlayer1.equals(other.homePlayer1))
            return false;
        if (homePlayer2 == null) {
            if (other.homePlayer2 != null)
                return false;
        } else if (!homePlayer2.equals(other.homePlayer2))
            return false;
        if (homeScore != other.homeScore)
            return false;
        if (position != other.position)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Game [homePlayer1=" + homePlayer1 + ", homePlayer2="
                + homePlayer2 + ", homeScore=" + homeScore + ", guestPlayer1="
                + guestPlayer1 + ", guestPlayer2=" + guestPlayer2
                + ", guestScore=" + guestScore + ", position=" + position
                + ", doubleMatch=" + doubleMatch + "]";
    }
}
