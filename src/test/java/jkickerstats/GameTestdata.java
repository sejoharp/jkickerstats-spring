package jkickerstats;

import jkickerstats.domain.Game;

import java.util.Calendar;
import java.util.Date;

import static jkickerstats.domain.Game.createGame;

public class GameTestdata {

    static Game createSingleGame() {
        return createGame()
                .withDoubleMatch(false)
                .withGuestPlayer1("Matheuszik, Sven")
                .withGuestScore(7)
                .withHomePlayer1("Kränz, Ludwig")
                .withHomeScore(5)
                .withPosition(2);
    }

    public static Game createSecondSingleGame() {
        return createGame()
                .withDoubleMatch(false)
                .withGuestPlayer1("Bai, Minyoung")
                .withGuestScore(7)
                .withHomePlayer1("Arslan, Mehmet Emin")
                .withHomeScore(4)
                .withPosition(1);
    }

    public static Game createDoubleGame() {
        return createGame()
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

}
