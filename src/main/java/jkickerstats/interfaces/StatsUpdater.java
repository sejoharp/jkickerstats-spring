package jkickerstats.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jkickerstats.domain.MatchRepoInterface;
import jkickerstats.types.Game;
import jkickerstats.types.Match;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsUpdater {
	private static final Logger LOG = Logger.getLogger(StatsUpdater.class
			.getName());

	@Autowired
	private PageParser kickerpageParser;
	@Autowired
	private PageDownloader pageDownloader;
	@Autowired
	private MatchRepoInterface matchRepo;

	private static String SEASONS_URL = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";

	public void updateStats() {
		LOG.info("updater batch started.");
		long matchesCountBefore = matchRepo.countMatches();
		if (matchRepo.noMatchesAvailable()) {
			getAllData();
		} else {
			updateData();
		}

		long matchesCountAfter = matchRepo.countMatches();
		LOG.info("updater batch finished and found "
				+ (matchesCountAfter - matchesCountBefore) + " new matches.");
	}

	protected List<Game> downloadAllGames() {
		List<Game> games = new ArrayList<>();
		getSeasonIDs().forEach(seasonId -> //
				getLigaLinks(seasonId).forEach(ligaLink -> //
						getMatchLinks(ligaLink).forEach(matchLink -> //
								games.addAll(getGames(matchLink)))));
		return games;
	}

	protected List<Match> downloadAllMatches() {
		List<Match> allMatches = new ArrayList<>();
		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<Match> matches = getMatches(ligaLink);
				for (Match match : matches) {
					Match fullMatch = new Match.MatchBuilder(match)//
							.withGames(getGames(match.getMatchLink()))//
							.build();
					allMatches.add(fullMatch);
				}
			}
		}
		return allMatches;
	}

	protected void getAllData() {
		List<Integer> seasonIds = getSeasonIDs();
		for (Integer seasonId : seasonIds) {
			List<String> ligaLinks = getLigaLinks(seasonId);
			for (String ligaLink : ligaLinks) {
				List<Match> matches = getMatches(ligaLink);
				for (Match match : matches) {
					Match fullMatch = new Match.MatchBuilder(match)//
							.withGames(getGames(match.getMatchLink()))//
							.build();
					matchRepo.save(fullMatch);
				}
			}
		}
	}

	protected void updateData() {
		List<Integer> seasonIds = getSeasonIDs();
		List<String> ligaLinks = getLigaLinks(getCurrentSeasonId(seasonIds));
		for (String ligaLink : ligaLinks) {
			List<Match> matches = getMatches(ligaLink);
			for (Match match : matches) {
				if (matchRepo.isNewMatch(match)) {
					Match fullMatch = new Match.MatchBuilder(match)//
							.withGames(getGames(match.getMatchLink()))//
							.build();
					matchRepo.save(fullMatch);
				}
			}
		}
	}

	protected int getCurrentSeasonId(List<Integer> seasons) {
		return seasons.stream().max(Integer::compare).get();
	}

	protected List<Game> getGames(String matchLink) {
		Document matchDoc = pageDownloader.downloadPage(matchLink);
		try {
			return kickerpageParser.findGames(matchDoc);
		} catch (Exception e) {
			LOG.severe("processing match: " + matchLink);
			throw e;
		}
	}

	protected List<String> getMatchLinks(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<String> matchLinks = kickerpageParser.findMatchLinks(ligaDoc);
		return matchLinks;
	}

	protected List<Match> getMatches(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		List<Match> matchLinks;
		try {
			matchLinks = kickerpageParser.findMatches(ligaDoc);
		} catch (Exception e) {
			LOG.severe("processing liga: " + ligaLink);
			throw e;
		}
		return matchLinks;
	}

	protected List<String> getLigaLinks(Integer seasonId) {
		Document seasonDoc = pageDownloader.downloadSeason(seasonId);
		List<String> ligaLinks = kickerpageParser.findLigaLinks(seasonDoc);
		return ligaLinks;
	}

	protected List<Integer> getSeasonIDs() {
		Document seasonsDoc = pageDownloader.downloadPage(SEASONS_URL);
		List<Integer> seasonIds = kickerpageParser.findSeasonIDs(seasonsDoc);
		return seasonIds;
	}
}
