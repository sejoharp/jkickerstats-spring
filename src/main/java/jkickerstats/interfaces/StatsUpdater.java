package jkickerstats.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import jkickerstats.domain.MatchRepo;
import jkickerstats.types.Game;
import jkickerstats.types.Match;
import jkickerstats.types.Match.MatchBuilder;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsUpdater {
	private static final Logger LOG = Logger.getLogger(StatsUpdater.class.getName());

	@Autowired
	private PageParser pageParser;
	@Autowired
	private PageDownloader pageDownloader;
	@Autowired
	private MatchRepo matchRepo;

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
		LOG.info("updater batch finished and found " + (matchesCountAfter - matchesCountBefore) + " new matches.");
	}

	protected List<Game> downloadAllGames() {
		List<Game> games = new ArrayList<>();
		getSeasonIDs().forEach(seasonId -> getLigaLinks(seasonId)//
				.forEach(ligaLink -> getMatchLinks(ligaLink)//
						.forEach(matchLink -> games.addAll(getGames(matchLink)))));
		return games;
	}

	protected List<Match> downloadAllMatches() {
		List<Match> allMatches = new ArrayList<>();
		getSeasonIDs().forEach(seasonId -> getLigaLinks(seasonId) //
				.forEach(ligaLink -> getMatches(ligaLink) //
						.forEach(match -> allMatches.add(new MatchBuilder(match)//
								.withGames(getGames(match.getMatchLink()))//
								.build()))));
		return allMatches;
	}

	protected void getAllData() {
		getSeasonIDs().forEach(seasonId -> getLigaLinks(seasonId)//
				.forEach(ligaLink -> getMatches(ligaLink)//
						.forEach(this::saveCompleteMatch)));
	}

	private void saveCompleteMatch(Match match) {
		Match fullMatch = new Match.MatchBuilder(match)//
				.withGames(getGames(match.getMatchLink()))//
				.build();
		matchRepo.save(fullMatch);
	}

	protected void updateData() {
		Integer seasonId = getCurrentSeasonId(getSeasonIDs());
		getLigaLinks(seasonId)//
				.forEach(ligaLink -> saveNewMatches(getMatches(ligaLink)));
	}

	protected void saveNewMatches(List<Match> matches) {
		matches.stream()//
				.filter(matchRepo::isNewMatch)//
				.forEach(this::saveCompleteMatch);
	}

	protected int getCurrentSeasonId(List<Integer> seasons) {
		return seasons.stream().max(Integer::compare).get();
	}

	protected List<Game> getGames(String matchLink) {
		Document matchDoc = pageDownloader.downloadPage(matchLink);
		try {
			return pageParser.findGames(matchDoc);
		} catch (Exception e) {
			LOG.severe("processing match: " + matchLink);
			throw e;
		}
	}

	protected List<String> getMatchLinks(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		return pageParser.findMatchLinks(ligaDoc);
	}

	protected List<Match> getMatches(String ligaLink) {
		Document ligaDoc = pageDownloader.downloadPage(ligaLink);
		try {
			return pageParser.findMatches(ligaDoc);
		} catch (Exception e) {
			LOG.severe("processing liga: " + ligaLink);
			throw e;
		}
	}

	protected List<String> getLigaLinks(Integer seasonId) {
		Document seasonDoc = pageDownloader.downloadSeason(seasonId);
		return pageParser.findLigaLinks(seasonDoc);
	}

	protected List<Integer> getSeasonIDs() {
		Document seasonsDoc = pageDownloader.downloadPage(SEASONS_URL);
		return pageParser.findSeasonIDs(seasonsDoc);
	}
}
