package jkickerstats;

import jkickerstats.types.Match;

import java.util.Arrays;

import static jkickerstats.types.Match.createMatch;

public class MatchTestdata {
    public static Match createTestMatch() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("guestteam")
                .withHomeScore(22)
                .withHomeTeam("hometeam")
                .withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(Arrays.asList(GameTestdata.createSingleGame(), GameTestdata.createDoubleGame()));
    }

    public static Match createMatchWithSinglegame() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("Hamburg Privateers 08")
                .withHomeScore(22)
                .withHomeTeam("Tingeltangel FC St. Pauli")
                .withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(Arrays.asList(GameTestdata.createSingleGame()));
    }

    public static Match createMatchWithDoublegame() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("Die Maschinerie")
                .withHomeScore(22)
                .withHomeTeam("Cim Bom Bom")
                .withMatchDate(GameTestdata.createDate(2013, 01, 28, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(Arrays.asList(GameTestdata.createDoubleGame()));
    }

    public static Match createMatchWithLink() {
        return createMatch()
                .withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))
                .withHomeTeam("Kickerbande")
                .withGuestTeam("St. Ellingen 1")
                .withMatchDay(1)
                .withHomeGoals(92)
                .withGuestGoals(31)
                .withHomeScore(32)
                .withGuestScore(0)
                .withMatchLink(
                        "https://kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815");
    }

    public static Match createMatchWithLink2() {
        return createMatch()
                .withMatchDate(GameTestdata.createDate(2017, 1, 14, 20, 0))
                .withHomeTeam("Krabbeltüte")
                .withGuestTeam("HTFC Gadgettos II")
                .withMatchDay(1)
                .withHomeGoals(65)
                .withGuestGoals(81)
                .withHomeScore(13)
                .withGuestScore(19)
                .withMatchLink(
                        "https://kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=118&id=8675");
    }

    public static Match createMatchLinkWithDoubleGame() {
        return createMatch()
                .withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))
                .withHomeTeam("Kickerbande")
                .withGuestTeam("St. Ellingen 1")
                .withMatchDay(1)
                .withHomeGoals(92)
                .withGuestGoals(31)
                .withHomeScore(32)
                .withGuestScore(0)
                .withGames(Arrays.asList(GameTestdata.createDoubleGame()))
                .withMatchLink(
                        "https://kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815");
    }

    public static Match createMatchLinkWithoutDate() {
        return createMatch()
                .withMatchDate(GameTestdata.createDate(1970, 0, 1, 1, 0))
                .withHomeTeam("Krabbeltüte")
                .withGuestTeam("HTFC Gadgettos II")
                .withMatchDay(1)
                .withHomeGoals(65)
                .withGuestGoals(81)
                .withHomeScore(13)
                .withGuestScore(19)
                .withMatchLink(
                        "https://kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=118&id=8675");
    }
}
