package jkickerstats.interfaces;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static jkickerstats.interfaces.PageDownloader.downloadPage;
import static jkickerstats.interfaces.PageDownloader.downloadSeason;
import static org.assertj.core.api.Assertions.assertThat;

public class PageDownloaderTest {

    @Test
    public void returnsAComplete() {
        Document document = downloadPage("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=8&id=2");
        assertThat(document.html()).contains("BSC Hamburg 07");
    }

    @Test
    public void returnsACompleteSeason() {
        Document document = downloadSeason(7);
        assertThat(document.html()).contains("Begegnungen …");
    }
}
