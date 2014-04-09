package jkickerstats.types;

import java.util.Date;
import java.util.List;

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

	private Match(MatchBuilder builder) {
		this.homeTeam = builder.homeTeam;
		this.guestTeam = builder.guestTeam;
		this.homeGoals = builder.homeGoals;
		this.guestGoals = builder.guestGoals;
		this.homeScore = builder.homeScore;
		this.guestScore = builder.guestScore;
		this.matchDate = builder.matchDate;
		this.matchDay = builder.matchDay;
		this.games = builder.games;
		this.matchLink = builder.matchLink;
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

	public static class MatchBuilder {

		private String homeTeam;
		private String guestTeam;
		private int homeGoals;
		private int guestGoals;
		private int homeScore;
		private int guestScore;
		private Date matchDate;
		private int matchDay;
		private List<Game> games;
		private String matchLink;

		public MatchBuilder(Match match) {
			this.homeTeam = match.getHomeTeam();
			this.guestTeam = match.getGuestTeam();
			this.homeGoals = match.getHomeGoals();
			this.guestGoals = match.getGuestGoals();
			this.homeScore = match.getHomeScore();
			this.guestScore = match.getGuestScore();
			this.matchDate = match.getMatchDate();
			this.matchDay = match.getMatchDay();
			this.games = match.getGames();
			this.matchLink = match.getMatchLink();
		}

		public MatchBuilder() {
		}

		public MatchBuilder withMatchLink(String matchLink) {
			this.matchLink = matchLink;
			return this;
		}

		public MatchBuilder withHomeTeam(String homeTeam) {
			this.homeTeam = homeTeam;
			return this;
		}

		public MatchBuilder withGuestTeam(String guestTeam) {
			this.guestTeam = guestTeam;
			return this;
		}

		public MatchBuilder withHomeGoals(int homeGoals) {
			this.homeGoals = homeGoals;
			return this;
		}

		public MatchBuilder withGuestGoals(int guestGoals) {
			this.guestGoals = guestGoals;
			return this;
		}

		public MatchBuilder withHomeScore(int homeScore) {
			this.homeScore = homeScore;
			return this;
		}

		public MatchBuilder withGuestScore(int guestScore) {
			this.guestScore = guestScore;
			return this;
		}

		public MatchBuilder withMatchDate(Date matchDate) {
			this.matchDate = matchDate;
			return this;
		}

		public MatchBuilder withMatchDay(int matchDay) {
			this.matchDay = matchDay;
			return this;
		}

		public MatchBuilder withGames(List<Game> games) {
			this.games = games;
			return this;
		}

		public Match build() {
			return new Match(this);
		}
	}
}
