package jkickerstats.persistence;

import jkickerstats.domain.Match;

import java.util.List;

public interface MatchLister {
    List<Match> getAllMatches();

    default boolean isNewMatch(Match match) {
        return getAllMatches().stream()
                .filter(storedMatch ->
                        storedMatch.getMatchDate().equals(match.getMatchDate())
                                && storedMatch.getHomeTeam().equals(match.getHomeTeam())
                                && storedMatch.getGuestTeam().equals(match.getGuestTeam()))
                .count() == 0;
    }

    default boolean noMatchesAvailable() {
        return getAllMatches().size() == 0;
    }

    default long countMatches() {
        return getAllMatches().size();
    }
}
