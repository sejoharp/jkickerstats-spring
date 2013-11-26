package jkickerstats.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jkickerstats.Application;
import jkickerstats.GameTestdata;
import jkickerstats.MatchTestdata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class MatchRepoTest {
	@Autowired
	private MatchRepoInterface matchRepo;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void cleanUpDb() {
		mongoTemplate.count(new Query(new Criteria()), MatchFromDb.class);
		Query query = new Query(new Criteria());
		mongoTemplate.remove(query, MatchFromDb.class);
	}

	@Test
	public void dbHasNoMatches() {
		assertThat(matchRepo.noMatchesAvailable(), is(true));
	}

	@Test
	public void detectsAlreadyPersistedMatches() {
		matchRepo.save(MatchTestdata.createMatch());
		assertThat(matchRepo.isNewMatch(MatchTestdata.createMatch()), is(false));
		assertThat(matchRepo.isNewMatch(MatchTestdata.createMatchWithSinglegame()), is(true));
	}
	
	@Test
	public void dbHasMatches() {
		matchRepo.save(MatchTestdata.createMatch());

		assertThat(matchRepo.noMatchesAvailable(), is(false));
		MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
				new Criteria()), MatchFromDb.class);
		assertThat(matchFromDb.getGames().size(), is(2));
	}

	@Test
	public void savesACompleteMatch() {
		matchRepo.save(MatchTestdata.createMatch());
		MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
				new Criteria()), MatchFromDb.class);

		assertThat(matchFromDb.getGuestScore(), is(10));
		assertThat(matchFromDb.getGuestTeam(), is("guestteam"));
		assertThat(matchFromDb.getHomeScore(), is(22));
		assertThat(matchFromDb.getHomeTeam(), is("hometeam"));
		assertThat(matchFromDb.getMatchDate(),
				is(GameTestdata.createDate(2013, 01, 27, 19, 1)));
		assertThat(matchFromDb.getMatchDay(), is(1));
		assertThat(matchFromDb.getHomeGoals(), is(10));
		assertThat(matchFromDb.getGuestGoals(), is(11));

		GameFromDb gameFromDb = matchFromDb.getGames().get(1);
		assertThat(gameFromDb.isDoubleMatch(), is(true));
		assertThat(gameFromDb.getHomePlayer1(), is("Arslan, Mehmet Emin"));
		assertThat(gameFromDb.getHomePlayer2(), is("Böckeler, Frank"));
		assertThat(gameFromDb.getHomeScore(), is(4));
		assertThat(gameFromDb.getGuestPlayer1(), is("Bai, Minyoung"));
		assertThat(gameFromDb.getGuestPlayer2(), is("Linnenberg, Sebastian"));
		assertThat(gameFromDb.getGuestScore(), is(5));
		assertThat(gameFromDb.getPosition(), is(16));
	}
}
