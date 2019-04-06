package jkickerstats.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import jkickerstats.domain.Match;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static jkickerstats.MatchTestdata.createTestMatch;

public class JsonMatchRepoTest {
    @Test
    public void savesAMatch() throws IOException {
        FileMatch match = FileMatch.from(createTestMatch());
        FileOutputStream fileOutputStream = new FileOutputStream("testoutput.json");
        new ObjectMapper().writeValue(fileOutputStream, match);
    }
}
