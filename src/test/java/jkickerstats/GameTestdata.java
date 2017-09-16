package jkickerstats;

import jkickerstats.domain.GameFromDb;
import jkickerstats.types.Game;

import java.util.Calendar;
import java.util.Date;

import static jkickerstats.types.Game.game;

public class GameTestdata {

    public static Game createSingleGame() {
        return game()
                .withDoubleMatch(false)
                .withGuestPlayer1("Matheuszik, Sven")
                .withGuestScore(7)
                .withHomePlayer1("Kränz, Ludwig")
                .withHomeScore(5)
                .withPosition(2);
    }

    public static Game createSecondSingleGame() {
        return game()
                .withDoubleMatch(false)
                .withGuestPlayer1("Bai, Minyoung")
                .withGuestScore(7)
                .withHomePlayer1("Arslan, Mehmet Emin")
                .withHomeScore(4)
                .withPosition(1);
    }

    public static Game createDoubleGame() {
        return game()
                .withDoubleMatch(true)
                .withHomePlayer1("Arslan, Mehmet Emin")
                .withHomePlayer2("Böckeler, Frank")
                .withHomeScore(4)
                .withGuestPlayer1("Bai, Minyoung")
                .withGuestPlayer2("Linnenberg, Sebastian")
                .withGuestScore(5)
                .withPosition(16);
    }

    public static Date createDate(int year, int month, int day, int hour,
                                  int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    public static Date createZeroCalendar() {
        Calendar matchDate = Calendar.getInstance();
        matchDate.setTimeInMillis(0);
        return matchDate.getTime();
    }

    public static GameFromDb createDoubleGameCouchDb() {
        GameFromDb gameCouchDb = new GameFromDb();
        gameCouchDb.setDoubleMatch(true);
        gameCouchDb.setGuestPlayer1("guest player1");
        gameCouchDb.setGuestPlayer2("guest player2");
        gameCouchDb.setGuestScore(10);
        gameCouchDb.setHomePlayer1("home player1");
        gameCouchDb.setHomePlayer2("home player2");
        gameCouchDb.setHomeScore(22);
        gameCouchDb.setPosition(2);
        return gameCouchDb;
    }

    public Game createDoubleGame2() {
        return game().withDoubleMatch(true)
                .withGuestPlayer1("guest player1")
                .withGuestPlayer2("guest player2")
                .withGuestScore(10)
                .withHomePlayer1("home player1")
                .withHomePlayer2("home player2")
                .withHomeScore(22)
                .withPosition(2);
    }
}
