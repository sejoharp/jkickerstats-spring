package jkickerstats.interfaces;

import java.util.List;

import jkickerstats.Application;
import jkickerstats.types.Match;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
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
	
	@Test
	@Ignore
	public void savesAllMatchesWithGames() {
		statsUpdater.getAllData();
	}
}
