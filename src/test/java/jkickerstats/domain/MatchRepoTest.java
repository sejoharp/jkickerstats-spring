package jkickerstats.domain;

import jkickerstats.Application;
import jkickerstats.MatchTestdata;
import jkickerstats.types.Match;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class MatchRepoTest {
    @Autowired
    private MatchRepo matchRepo;
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
    public void countesTheNumberOfMatches() {
        assertThat(matchRepo.countMatches(), is(0L));
        matchRepo.save(MatchTestdata.createMatch());
        assertThat(matchRepo.countMatches(), is(1L));
        matchRepo.save(MatchTestdata.createMatchWithSinglegame());
        assertThat(matchRepo.countMatches(), is(2L));
    }

    @Test
    public void detectsAlreadyPersistedMatches() {
        matchRepo.save(MatchTestdata.createMatch());
        assertThat(matchRepo.isNewMatch(MatchTestdata.createMatch()), is(false));
        assertThat(
                matchRepo.isNewMatch(MatchTestdata.createMatchWithSinglegame()),
                is(true));
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

        assertThat(new MongoDbMatchRepo().convertToMatch(matchFromDb),
                is(MatchTestdata.createMatch()));
    }

    @Test
    public void convertsBothWays() {
        MatchFromDb matchFromDb = new MongoDbMatchRepo()
                .convertToMatchFromDb(MatchTestdata.createMatch());
        Match match = new MongoDbMatchRepo().convertToMatch(matchFromDb);
        assertThat(match, is(MatchTestdata.createMatch()));
    }
}
