package jkickerstats.persistence;

import jkickerstats.MatchTestdata;
import jkickerstats.domain.Match;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static jkickerstats.MatchTestdata.createTestMatch;
import static jkickerstats.persistence.MongoMatchRepo.convertToMatch;
import static jkickerstats.persistence.MongoMatchRepo.convertToMatchFromDb;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(Repository.class))
@RunWith(SpringRunner.class)
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
        persister.save(createTestMatch());
        assertThat(lister.countMatches()).isEqualTo(1);
        persister.save(MatchTestdata.createMatchWithSingleGame());
        assertThat(lister.countMatches()).isEqualTo(2);
    }

    @Test
    public void detectsAlreadyPersistedMatches() {
        persister.save(createTestMatch());
        assertThat(lister.isNewMatch(createTestMatch())).isFalse();
        assertThat(lister.isNewMatch(MatchTestdata.createMatchWithSingleGame())).isTrue();
    }

    @Test
    public void dbHasMatches() {
        persister.save(createTestMatch());

        assertThat(lister.noMatchesAvailable()).isFalse();
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);
        assertThat(matchFromDb.getGames()).hasSize(2);
    }

    @Test
    public void savesACompleteMatch() {
        persister.save(createTestMatch());
        MatchFromDb matchFromDb = mongoTemplate.findOne(new Query(
                new Criteria()), MatchFromDb.class);

        assertThat(convertToMatch(matchFromDb)).isEqualTo(createTestMatch());
    }

    @Test
    public void findesAllMatches() {
        //given
        Match expectedMatch = createTestMatch();
        persister.save(expectedMatch);

        //when
        List<Match> matchFromDb = lister.getAllMatches();

        //then
        assertThat(matchFromDb.get(0)).isEqualTo(expectedMatch);
    }

    @Test
    public void convertsBothWays() {
        MatchFromDb matchFromDb = convertToMatchFromDb(createTestMatch());
        Match match = convertToMatch(matchFromDb);
        assertThat(match).isEqualTo(createTestMatch());
    }
}
