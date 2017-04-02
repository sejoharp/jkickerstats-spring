package jkickerstats.domain;

import jkickerstats.types.Game;
import jkickerstats.types.Game.GameBuilder;
import jkickerstats.types.Match;
import jkickerstats.types.Match.MatchBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MongoDbMatchRepo implements MatchRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean isNewMatch(Match match) {

        Query q = new Query(Criteria.where("matchDate")
                .is(match.getMatchDate()).and("homeTeam")
                .is(match.getHomeTeam()).and("guestTeam")
                .is(match.getGuestTeam()));
        long numberOfMatches = mongoTemplate.count(q, MatchFromDb.class);
        return numberOfMatches == 0;
    }

    @Override
    public void save(Match match) {
        mongoTemplate.save(convertToMatchFromDb(match));
    }

    @Override
    public void save(List<Match> matches) {
        mongoTemplate.save(convertToMatchFromDbList(matches));
    }

    @Override
    public boolean noMatchesAvailable() {
        long numberOfMatches = mongoTemplate.count(new Query(new Criteria()),
                MatchFromDb.class);
        return numberOfMatches == 0;
    }

    @Override
    public List<Match> getAllMatches() {
        Criteria criteria = new Criteria().all(MatchFromDb.class);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "matchDate"));
        List<MatchFromDb> matches = mongoTemplate
                .find(query, MatchFromDb.class);
        return convertToMatchList(matches);
    }

    protected List<MatchFromDb> convertToMatchFromDbList(List<Match> matches) {
        return matches.stream()//
                .map(this::convertToMatchFromDb)//
                .collect(Collectors.toList());
    }

    protected List<Match> convertToMatchList(List<MatchFromDb> matchFromDbs) {
        return matchFromDbs.stream()//
                .map(this::convertToMatch)//
                .collect(Collectors.toList());
    }

    protected MatchFromDb convertToMatchFromDb(Match match) {
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

    protected Match convertToMatch(MatchFromDb matchFromDb) {
        return new MatchBuilder()//
                .withGuestGoals(matchFromDb.getGuestGoals())//
                .withGuestScore(matchFromDb.getGuestScore())//
                .withGuestTeam(matchFromDb.getGuestTeam())//
                .withHomeGoals(matchFromDb.getHomeGoals())//
                .withHomeScore(matchFromDb.getHomeScore())//
                .withHomeTeam(matchFromDb.getHomeTeam())//
                .withMatchDate(matchFromDb.getMatchDate())//
                .withMatchDay(matchFromDb.getMatchDay())//
                .withGames(convertToGameList(matchFromDb.getGames()))//
                .build();
    }

    protected List<GameFromDb> convertToGameFromDbList(List<Game> games) {
        return games.stream()//
                .map(game -> convertToGameFromDb(game))//
                .collect(Collectors.toList());
    }

    protected List<Game> convertToGameList(List<GameFromDb> gameCouchDbList) {
        return gameCouchDbList.stream()//
                .map(this::convertToGame)//
                .collect(Collectors.toList());
    }

    protected GameFromDb convertToGameFromDb(Game game) {
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

    protected Game convertToGame(GameFromDb gameCouchDb) {
        GameBuilder builder = new Game.GameBuilder();
        builder.withDoubleMatch(gameCouchDb.isDoubleMatch());
        builder.withGuestPlayer1(gameCouchDb.getGuestPlayer1());
        builder.withGuestPlayer2(gameCouchDb.getGuestPlayer2());
        builder.withGuestScore(gameCouchDb.getGuestScore());
        builder.withHomePlayer1(gameCouchDb.getHomePlayer1());
        builder.withHomePlayer2(gameCouchDb.getHomePlayer2());
        builder.withHomeScore(gameCouchDb.getHomeScore());
        builder.withPosition(gameCouchDb.getPosition());
        return builder.build();
    }

    @Override
    public long countMatches() {
        return mongoTemplate
                .count(new Query(new Criteria()), MatchFromDb.class);
    }

}
