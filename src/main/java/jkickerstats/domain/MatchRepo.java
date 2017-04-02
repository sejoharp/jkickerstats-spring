package jkickerstats.domain;

import jkickerstats.types.Match;

import java.util.List;

public interface MatchRepo {
    public boolean isNewMatch(Match match);

    public void save(Match match);

    public void save(List<Match> matches);

    public boolean noMatchesAvailable();

    public List<Match> getAllMatches();

    public long countMatches();
}
