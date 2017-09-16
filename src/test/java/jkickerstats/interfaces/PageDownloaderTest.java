package jkickerstats.interfaces;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PageDownloaderTest {
    private PageDownloader pageDownloader;

    @Before
    public void setup() {
        pageDownloader = new PageDownloader();
    }

    @Test
    public void returnsAComplete() {
        Document document = pageDownloader
                .downloadPage("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=8&id=2");
        assertThat(document.html()).contains("BSC Hamburg 07");
    }

    @Test
    public void returnsACompleteSeason() {
        Document document = pageDownloader.downloadSeason(7);
        assertThat(document.html()).contains("Begegnungen …");
    }
}
