package jkickerstats.persistence;

import jkickerstats.GameTestdata;
import jkickerstats.domain.Game;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileGameTest {
    @Test
    public void createsFileGameFromGame() {
        //given
        Game game = GameTestdata.createDoubleGame();

        //when
        FileGame fileGame = FileGame.from(game);

        //then
        assertThat(fileGame.getGuestPlayer1()).isEqualTo("Bai, Minyoung");
        assertThat(fileGame.getGuestPlayer2()).isEqualTo("Linnenberg, Sebastian");
        assertThat(fileGame.getGuestScore()).isEqualTo(5);
        assertThat(fileGame.getHomePlayer1()).isEqualTo("Arslan, Mehmet Emin");
        assertThat(fileGame.getHomePlayer2()).isEqualTo("Böckeler, Frank");
        assertThat(fileGame.getHomeScore()).isEqualTo(4);
        assertThat(fileGame.getPosition()).isEqualTo(16);
        assertThat(fileGame.isDoubleMatch()).isTrue();
    }

    @Test
    public void transformsToGame() {
        //given
        Game originalGame = GameTestdata.createDoubleGame();
        FileGame fileGame = FileGame.from(originalGame);

        //when
        Game game = fileGame.toGame();

        //then
        assertThat(game).isEqualTo(originalGame);
    }
}