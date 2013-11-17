package jkickerstats.interfaces;

import java.util.List;

import jkickerstats.types.Match;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class StatsUpdaterTest {
	@Autowired
	private StatsUpdater statsUpdater;

	@Autowired
	private CsvCreator csvCreator;

	@Ignore
	@Test
	public void createCSVFileWithAllGames() {
		List<Match> matches = statsUpdater.downloadAllMatches();
		List<String> gameStrings = csvCreator.createCsvRowList(matches);
		csvCreator.createCsvFile(gameStrings);
	}
	
	@Ignore
	@Test
	public void savesAllMatchesWithGames() {
		statsUpdater.getAllData();
	}
}
