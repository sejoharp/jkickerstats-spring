package jkickerstats.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static jkickerstats.services.PageParser.findLigaLinks;
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
        List<String> ligaLinksIDs = findLigaLinks(doc).collect(toList());

        // then
        assertThat(ligaLinksIDs).hasSize(11);
        assertThat(ligaLinksIDs.get(0)).isEqualTo("https://kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=118");
    }

    @Test
    public void returnsAllLigaLinksFor2018() throws IOException {
        // given
        File testFile = new File(RECOURCES_DIRECTORY + "uebersicht2018_2019.html");
        Document doc = Jsoup.parse(testFile, "UTF-8", "");

        // when
        List<String> ligaLinksIDs = findLigaLinks(doc).collect(toList());

        // then
        assertThat(ligaLinksIDs).hasSize(13);
        assertThat(ligaLinksIDs.get(12)).isEqualTo("https://kickern-hamburg.de/de/competitions/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=164");
    }

    @Test
    public void detectsZwischenSaison2018() throws IOException {
        //given
        File testFile = new File(RECOURCES_DIRECTORY + "begegnung_2018.html");
        Document doc = Jsoup.parse(testFile, "UTF-8", "");

        //when
        boolean zwischenSaison2018 = PageParser.isZwischenSaison2018(doc);

        //then
        assertThat(zwischenSaison2018).isTrue();
    }
}
