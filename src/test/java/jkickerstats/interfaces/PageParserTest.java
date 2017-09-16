package jkickerstats.interfaces;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static jkickerstats.interfaces.PageParser.findLigaLinks;
import static org.assertj.core.api.Assertions.assertThat;

public class PageParserTest {
    static final String RECOURCES_DIRECTORY =
            System.getProperty("user.dir") + "/src/test/resources/";

    @Test
    public void returnsAllLigaLinks() throws IOException {
        // given
        File testFile = new File(RECOURCES_DIRECTORY + "uebersicht.html");
        Document doc = Jsoup.parse(testFile, "UTF-8", "");

        // when
        List<String> ligaLinksIDs = findLigaLinks(doc);

        // then
        assertThat(ligaLinksIDs).hasSize(11);
        assertThat(ligaLinksIDs.get(0)).isEqualTo("http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=118");
    }
}
