package jkickerstats.usecases;

import java.util.List;

import jkickerstats.types.Match;

public interface MatchServiceInterface {
	public void saveMatches(List<Match> matches);

	public void saveMatch(Match match);

	public boolean isNewMatch(Match match);
	
	public boolean noDataAvailable();
	
	public List<Match> getAllMatches();
	
	public long countMatches();
}
