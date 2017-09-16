package jkickerstats.domain;

import jkickerstats.types.Match;

public interface MatchPersister {

    void save(Match match);
}
