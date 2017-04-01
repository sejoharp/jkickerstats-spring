package jkickerstats;

import java.util.Arrays;

import jkickerstats.types.Match;
import jkickerstats.types.Match.MatchBuilder;

public class MatchTestdata {
	public static Match createMatch() {
		return new MatchBuilder()//
				.withGuestScore(10)//
				.withGuestTeam("guestteam")//
				.withHomeScore(22)//
				.withHomeTeam("hometeam")//
				.withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))//
				.withMatchDay(1)//
				.withHomeGoals(10)//
				.withGuestGoals(11)
				.withGames(Arrays.asList(GameTestdata.createSingleGame(), GameTestdata.createDoubleGame()))//
				.build();
	}

	public static Match createMatchWithSinglegame() {
		return new MatchBuilder()//
				.withGuestScore(10)//
				.withGuestTeam("Hamburg Privateers 08")//
				.withHomeScore(22)//
				.withHomeTeam("Tingeltangel FC St. Pauli")//
				.withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))//
				.withMatchDay(1)//
				.withHomeGoals(10)//
				.withGuestGoals(11)//
				.withGames(Arrays.asList(GameTestdata.createSingleGame()))//
				.build();
	}

	public static Match createMatchWithDoublegame() {
		return new MatchBuilder()//
				.withGuestScore(10)//
				.withGuestTeam("Die Maschinerie")//
				.withHomeScore(22)//
				.withHomeTeam("Cim Bom Bom")//
				.withMatchDate(GameTestdata.createDate(2013, 01, 28, 19, 1))//
				.withMatchDay(1)//
				.withHomeGoals(10)//
				.withGuestGoals(11)//
				.withGames(Arrays.asList(GameTestdata.createDoubleGame()))//
				.build();
	}

	public static Match createMatchWithLink() {
		return new MatchBuilder()//
				.withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))//
				.withHomeTeam("Kickerbande")//
				.withGuestTeam("St. Ellingen 1")//
				.withMatchDay(1)//
				.withHomeGoals(92)//
				.withGuestGoals(31)//
				.withHomeScore(32)//
				.withGuestScore(0)//
				.withMatchLink(
						"http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815")//
				.build();
	}
	public static Match createMatchWithLink2() {
		return new MatchBuilder()//
				.withMatchDate(GameTestdata.createDate(2017, 1, 14, 20, 0))//
				.withHomeTeam("Krabbeltüte")//
				.withGuestTeam("HTFC Gadgettos II")//
				.withMatchDay(1)//
				.withHomeGoals(65)//
				.withGuestGoals(81)//
				.withHomeScore(13)//
				.withGuestScore(19)//
				.withMatchLink(
						"http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=118&id=8675")//
				.build();
	}

	public static Match createMatchLinkWithDoubleGame() {
		return new MatchBuilder()//
				.withMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1))//
				.withHomeTeam("Kickerbande")//
				.withGuestTeam("St. Ellingen 1")//
				.withMatchDay(1)//
				.withHomeGoals(92)//
				.withGuestGoals(31)//
				.withHomeScore(32)//
				.withGuestScore(0)//
				.withGames(Arrays.asList(GameTestdata.createDoubleGame()))//
				.withMatchLink(
						"http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815")//
				.build();
	}

	public static Match createMatchLinkWithoutDate() {
		return new MatchBuilder()//
				.withMatchDate(GameTestdata.createDate(1970, 0, 1, 1, 0))//
				.withHomeTeam("Krabbeltüte")//
				.withGuestTeam("HTFC Gadgettos II")//
				.withMatchDay(1)//
				.withHomeGoals(65)//
				.withGuestGoals(81)//
				.withHomeScore(13)//
				.withGuestScore(19)//
				.withMatchLink(
						"http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=118&id=8675")//
				.build();
	}
}
