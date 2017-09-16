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

import static jkickerstats.domain.MongoMatchLister.convertToMatch;
import static jkickerstats.domain.MongoMatchLister.convertToMatchFromDb;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class MatchRepoTest {
    @Autowired
    private MatchLister lister;
    @Autowired
    private MatchPersister persister;
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
        assertThat(lister.noMatchesAvailable(), is(true));
    }

    @Test
    public void countesTheNumberOfMatches() {
        assertThat(lister.countMatches(), is(0L));
        persister.save(MatchTestdata.createMatch());
        assertThat(lister.countMatches(), is(1L));
        persister.save(MatchTestdata.createMatchWithSinglegame());
        assertThat(lister.countMatches(), is(2L));
    }

    @Test
    public void detectsAlreadyPersistedMatches() {
        persister.save(MatchTestdata.createMatch());
        assertThat(lister.isNewMatch(MatchTestdata.createMatch()), is(false));
        assertThat(
                lister.isNewMatch(MatchTestdata.createMatchWithSinglegame()),
                is(true));
    }

    @Test
    public void dbHasMatches() {
        persister.save(MatchTestdata.createMatch());

        assertThat(lister.noMatchesAvailable(), is(false));
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);
        assertThat(matchFromDb.getGames().size(), is(2));
    }

    @Test
    public void savesACompleteMatch() {
        persister.save(MatchTestdata.createMatch());
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);

        assertThat(convertToMatch(matchFromDb), is(MatchTestdata.createMatch()));
    }

    @Test
    public void convertsBothWays() {
        MatchFromDb matchFromDb = convertToMatchFromDb(MatchTestdata.createMatch());
        Match match = convertToMatch(matchFromDb);
        assertThat(match, is(MatchTestdata.createMatch()));
    }
}
