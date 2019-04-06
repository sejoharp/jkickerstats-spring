package jkickerstats;

import jkickerstats.domain.Match;

import static java.util.Arrays.asList;
import static jkickerstats.GameTestdata.createDate;
import static jkickerstats.domain.Match.createMatch;

public class MatchTestdata {
    public static Match createTestMatch() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("guestteam")
                .withHomeScore(22)
                .withHomeTeam("hometeam")
                .withMatchDate(createDate(2013, 01, 27, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(asList(GameTestdata.createSingleGame(), GameTestdata.createDoubleGame()));
    }

    public static Match createTestMatchForId() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("guest team")
                .withHomeScore(22)
                .withHomeTeam("home team")
                .withMatchDate(createDate(2013, 01, 27, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(asList(GameTestdata.createSingleGame(), GameTestdata.createDoubleGame()));
    }

    public static Match createMatchWithSingleGame() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("Hamburg Privateers 08")
                .withHomeScore(22)
                .withHomeTeam("Tingeltangel FC St. Pauli")
                .withMatchDate(createDate(2013, 01, 27, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(asList(GameTestdata.createSingleGame()));
    }

    public static Match createMatchWithDoubleGame() {
        return createMatch()
                .withGuestScore(10)
                .withGuestTeam("Die Maschinerie")
                .withHomeScore(22)
                .withHomeTeam("Cim Bom Bom")
                .withMatchDate(createDate(2013, 01, 28, 19, 1))
                .withMatchDay(1)
                .withHomeGoals(10)
                .withGuestGoals(11)
                .withGames(asList(GameTestdata.createDoubleGame()));
    }

    public static Match createMatchWithLink() {
        return createMatch()
                .withMatchDate(createDate(2013, 01, 27, 19, 1))
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
                .withMatchDate(createDate(2017, 1, 14, 20, 0))
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
                .withMatchDate(createDate(2013, 01, 27, 19, 1))
                .withHomeTeam("Kickerbande")
                .withGuestTeam("St. Ellingen 1")
                .withMatchDay(1)
                .withHomeGoals(92)
                .withGuestGoals(31)
                .withHomeScore(32)
                .withGuestScore(0)
                .withGames(asList(GameTestdata.createDoubleGame()))
                .withMatchLink(
                        "https://kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815");
    }

    public static Match createMatchLinkWithoutDate() {
        return createMatch()
                .withMatchDate(createDate(1970, 0, 1, 1, 0))
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
