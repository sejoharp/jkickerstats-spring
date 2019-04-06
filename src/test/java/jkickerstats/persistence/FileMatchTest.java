package jkickerstats.persistence;

import jkickerstats.domain.Match;
import org.junit.Test;

import static jkickerstats.GameTestdata.createDate;
import static jkickerstats.MatchTestdata.createTestMatch;
import static jkickerstats.MatchTestdata.createTestMatchForId;
import static org.assertj.core.api.Assertions.assertThat;

public class FileMatchTest {
    @Test
    public void createsFileMatchFromMatch() {
        //given
        Match testMatch = createTestMatch();

        //when
        FileMatch fileMatch = FileMatch.from(testMatch);

        //then
        assertThat(fileMatch.getGuestGoals()).isEqualTo(11);
        assertThat(fileMatch.getGuestTeam()).isEqualTo("guestteam");
        assertThat(fileMatch.getGuestScore()).isEqualTo(10);
        assertThat(fileMatch.getHomeTeam()).isEqualTo("hometeam");
        assertThat(fileMatch.getHomeGoals()).isEqualTo(10);
        assertThat(fileMatch.getHomeScore()).isEqualTo(22);
        assertThat(fileMatch.getMatchDay()).isEqualTo(1);
        assertThat(fileMatch.getMatchDate()).isEqualTo(createDate(2013,01,27, 19,1));
        assertThat(fileMatch.getGames()).hasSize(2);
    }

    @Test
    public void generateFileName() {
        //given
        FileMatch match = FileMatch.from(createTestMatchForId());

        //when
        String id = match.fileName();

        //then
        assertThat(id).isEqualTo("2013-02-27_hometeam_guestteam.json");
    }

    @Test
    public void transformsToMatch() {
        //given
        Match originalMatch = createTestMatch();
        FileMatch fileMatch = FileMatch.from(originalMatch);

        //when
        Match match = fileMatch.toMatch();

        //then
        assertThat(match).isEqualTo(originalMatch);
    }
}