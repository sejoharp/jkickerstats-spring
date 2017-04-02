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

    private Game(GameBuilder builder) {
        this.homePlayer1 = builder.homePlayer1;
        this.homePlayer2 = builder.homePlayer2;
        this.homeScore = builder.homeScore;
        this.guestPlayer1 = builder.guestPlayer1;
        this.guestPlayer2 = builder.guestPlayer2;
        this.guestScore = builder.guestScore;
        this.position = builder.position;
        this.doubleMatch = builder.doubleMatch;
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

    public static class GameBuilder {

        private String homePlayer1;
        private String homePlayer2;
        private int homeScore;
        private String guestPlayer1;
        private String guestPlayer2;
        private int guestScore;
        private int position;
        private boolean doubleMatch;

        public GameBuilder() {
        }

        public GameBuilder(Game game) {
            this.homePlayer1 = game.getHomePlayer1();
            this.homePlayer2 = game.getHomePlayer2();
            this.homeScore = game.getHomeScore();
            this.guestPlayer1 = game.getGuestPlayer1();
            this.guestPlayer2 = game.getGuestPlayer2();
            this.guestScore = game.getGuestScore();
            this.position = game.getPosition();
            this.doubleMatch = game.isDoubleMatch();
        }

        public GameBuilder withHomePlayer1(String homePlayer1) {
            this.homePlayer1 = homePlayer1;
            return this;
        }

        public GameBuilder withHomePlayer2(String homePlayer2) {
            this.homePlayer2 = homePlayer2;
            return this;
        }

        public GameBuilder withHomeScore(int homeScore) {
            this.homeScore = homeScore;
            return this;
        }

        public GameBuilder withGuestPlayer1(String guestPlayer1) {
            this.guestPlayer1 = guestPlayer1;
            return this;
        }

        public GameBuilder withGuestPlayer2(String guestPlayer2) {
            this.guestPlayer2 = guestPlayer2;
            return this;
        }

        public GameBuilder withGuestScore(int guestScore) {
            this.guestScore = guestScore;
            return this;
        }

        public GameBuilder withPosition(int position) {
            this.position = position;
            return this;
        }

        public GameBuilder withDoubleMatch(boolean doubleMatch) {
            this.doubleMatch = doubleMatch;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}
