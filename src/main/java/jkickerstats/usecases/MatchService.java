package jkickerstats.usecases;

import java.util.List;

import jkickerstats.domain.MatchRepoInterface;
import jkickerstats.types.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchService implements MatchServiceInterface {

	@Autowired
	private MatchRepoInterface matchRepo;

	@Override
	public void saveMatches(List<Match> matches) {
		matchRepo.save(matches);

	}

	@Override
	public boolean isNewMatch(Match match) {
		return matchRepo.isNewMatch(match);
	}

	@Override
	public void saveMatch(Match match) {
		matchRepo.save(match);
	}

	@Override
	public boolean noDataAvailable() {
		return matchRepo.noMatchesAvailable();
	}

	@Override
	public List<Match> getAllMatches() {
		return matchRepo.getAllMatches();
	}

	@Override
	public long countMatches() {
		return matchRepo.countMatches();
	}

}
