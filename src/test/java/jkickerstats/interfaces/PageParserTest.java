package jkickerstats.interfaces;

import jkickerstats.Application;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class PageParserTest {
    public static final String RECOURCES_DIRECTORY = System
            .getProperty("user.dir") + "/src/test/resources/";

    @Autowired
    private PageParser parser;

    @Before
    public void checkPreconditions() {
        assertThat(parser).isNotNull();
    }

    @Test
    public void returnsAllLigaLinks() throws IOException {
        File testFile = new File(RECOURCES_DIRECTORY + "uebersicht.html");
        Document doc = Jsoup.parse(testFile, "UTF-8", "");

        List<String> ligaLinksIDs = parser.findLigaLinks(doc);

        assertThat(ligaLinksIDs).hasSize(11);
        assertThat(
                ligaLinksIDs.get(0)).isEqualTo("http://www.kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=118");
    }
}
