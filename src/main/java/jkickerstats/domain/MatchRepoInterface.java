package jkickerstats.domain;

import java.util.List;

import jkickerstats.types.Match;

public interface MatchRepoInterface {
	public boolean isNewMatch(Match match);

	public void save(Match match);

	public void save(List<Match> matches);

	public boolean noMatchesAvailable();
	
	public List<Match> getAllMatches();
}
