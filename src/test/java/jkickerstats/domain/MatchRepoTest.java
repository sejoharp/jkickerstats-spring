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
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(lister.noMatchesAvailable()).isTrue();
    }

    @Test
    public void countesTheNumberOfMatches() {
        assertThat(lister.countMatches()).isEqualTo(0);
        persister.save(MatchTestdata.createMatch());
        assertThat(lister.countMatches()).isEqualTo(1);
        persister.save(MatchTestdata.createMatchWithSinglegame());
        assertThat(lister.countMatches()).isEqualTo(2);
    }

    @Test
    public void detectsAlreadyPersistedMatches() {
        persister.save(MatchTestdata.createMatch());
        assertThat(lister.isNewMatch(MatchTestdata.createMatch())).isFalse();
        assertThat(lister.isNewMatch(MatchTestdata.createMatchWithSinglegame())).isTrue();
    }

    @Test
    public void dbHasMatches() {
        persister.save(MatchTestdata.createMatch());

        assertThat(lister.noMatchesAvailable()).isFalse();
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);
        assertThat(matchFromDb.getGames()).hasSize(2);
    }

    @Test
    public void savesACompleteMatch() {
        persister.save(MatchTestdata.createMatch());
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);

        assertThat(convertToMatch(matchFromDb)).isEqualTo(MatchTestdata.createMatch());
    }

    @Test
    public void convertsBothWays() {
        MatchFromDb matchFromDb = convertToMatchFromDb(MatchTestdata.createMatch());
        Match match = convertToMatch(matchFromDb);
        assertThat(match).isEqualTo(MatchTestdata.createMatch());
    }
}
