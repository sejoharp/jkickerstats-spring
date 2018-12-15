package jkickerstats.interfaces;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ParsedMatchesRetrieverImplTest {

    @Test
    public void getSeasonIDs() {
        //given
        ParsedMatchesRetrieverImpl parsedMatchesRetriever = new ParsedMatchesRetrieverImpl();

        //when
        List<Integer> seasonIDs = parsedMatchesRetriever.getSeasonIDs().collect(toList());

        //then
        assertThat(seasonIDs).hasSize(11);
    }
}
