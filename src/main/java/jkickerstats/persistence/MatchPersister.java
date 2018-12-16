package jkickerstats.persistence;

import jkickerstats.domain.Match;

public interface MatchPersister {

    void save(Match match);
}
