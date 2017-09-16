package jkickerstats.domain;

import jkickerstats.types.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static jkickerstats.domain.MongoMatchLister.convertToMatchFromDb;

@Repository
public class MongoMatchPersister implements MatchPersister{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoMatchPersister(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save(Match match) {
        mongoTemplate.save(convertToMatchFromDb(match));
    }

}
