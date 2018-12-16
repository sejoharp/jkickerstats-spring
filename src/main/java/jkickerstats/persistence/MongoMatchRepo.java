package jkickerstats.persistence;

import jkickerstats.domain.Game;
import jkickerstats.domain.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static jkickerstats.domain.Game.createGame;

@Repository
public class MongoMatchRepo implements MatchPersister, MatchLister {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoMatchRepo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean isNewMatch(Match match) {

        Query q = new Query(Criteria.where("matchDate").is(match.getMatchDate())
                .and("homeTeam").is(match.getHomeTeam())
                .and("guestTeam").is(match.getGuestTeam()));
        return mongoTemplate.count(q, MatchFromDb.class) == 0;
    }

    @Override
    public boolean noMatchesAvailable() {
        long numberOfMatches = mongoTemplate.count(new Query(new Criteria()),
                MatchFromDb.class);
        return numberOfMatches == 0;
    }

    @Override
    public List<Match> getAllMatches() {
        Query query = new Query(new Criteria());
        query.with(new Sort(Sort.Direction.DESC, "matchDate"));
        List<MatchFromDb> matches = mongoTemplate.find(query, MatchFromDb.class);
        return convertToMatchList(matches);
    }

    @Override
    public long countMatches() {
        return mongoTemplate
                .count(new Query(new Criteria()), MatchFromDb.class);
    }

    public void save(Match match) {
        mongoTemplate.save(convertToMatchFromDb(match));
    }

    private static List<MatchFromDb> convertToMatchFromDbList(List<Match> matches) {
        return matches.stream()
                .map(MongoMatchRepo::convertToMatchFromDb)
                .collect(Collectors.toList());
    }

    private static List<Match> convertToMatchList(List<MatchFromDb> matchFromDbs) {
        return matchFromDbs.stream()
                .map(MongoMatchRepo::convertToMatch)
                .collect(Collectors.toList());
    }

    static MatchFromDb convertToMatchFromDb(Match match) {
        MatchFromDb matchFromDb = new MatchFromDb();
        matchFromDb.setGuestGoals(match.getGuestGoals());
        matchFromDb.setGuestScore(match.getGuestScore());
        matchFromDb.setGuestTeam(match.getGuestTeam());
        matchFromDb.setHomeGoals(match.getHomeGoals());
        matchFromDb.setHomeScore(match.getHomeScore());
        matchFromDb.setHomeTeam(match.getHomeTeam());
        matchFromDb.setMatchDate(match.getMatchDate());
        matchFromDb.setMatchDay(match.getMatchDay());
        matchFromDb.setGames(convertToGameFromDbList(match.getGames()));
        return matchFromDb;
    }

    static Match convertToMatch(MatchFromDb matchFromDb) {
        return Match.createMatch()
                .withGuestGoals(matchFromDb.getGuestGoals())
                .withGuestScore(matchFromDb.getGuestScore())
                .withGuestTeam(matchFromDb.getGuestTeam())
                .withHomeGoals(matchFromDb.getHomeGoals())
                .withHomeScore(matchFromDb.getHomeScore())
                .withHomeTeam(matchFromDb.getHomeTeam())
                .withMatchDate(matchFromDb.getMatchDate())
                .withMatchDay(matchFromDb.getMatchDay())
                .withGames(convertToGameList(matchFromDb.getGames()));
    }

    private static List<GameFromDb> convertToGameFromDbList(List<Game> games) {
        return games.stream()
                .map(MongoMatchRepo::convertToGameFromDb)
                .collect(Collectors.toList());
    }

    private static List<Game> convertToGameList(List<GameFromDb> gameCouchDbList) {
        return gameCouchDbList.stream()
                .map(MongoMatchRepo::convertToGame)
                .collect(Collectors.toList());
    }

    private static GameFromDb convertToGameFromDb(Game game) {
        GameFromDb gameFromDb = new GameFromDb();
        gameFromDb.setDoubleMatch(game.isDoubleMatch());
        gameFromDb.setGuestPlayer1(game.getGuestPlayer1());
        gameFromDb.setGuestPlayer2(game.getGuestPlayer2());
        gameFromDb.setGuestScore(game.getGuestScore());
        gameFromDb.setHomePlayer1(game.getHomePlayer1());
        gameFromDb.setHomePlayer2(game.getHomePlayer2());
        gameFromDb.setHomeScore(game.getHomeScore());
        gameFromDb.setPosition(game.getPosition());
        return gameFromDb;
    }

    private static Game convertToGame(GameFromDb gameCouchDb) {
        return createGame().withDoubleMatch(gameCouchDb.isDoubleMatch())
                .withGuestPlayer1(gameCouchDb.getGuestPlayer1())
                .withGuestPlayer2(gameCouchDb.getGuestPlayer2())
                .withGuestScore(gameCouchDb.getGuestScore())
                .withHomePlayer1(gameCouchDb.getHomePlayer1())
                .withHomePlayer2(gameCouchDb.getHomePlayer2())
                .withHomeScore(gameCouchDb.getHomeScore())
                .withPosition(gameCouchDb.getPosition());
    }
}
