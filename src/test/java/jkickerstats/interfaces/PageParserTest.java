package jkickerstats.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class PageParserTest {
	public static final String RECOURCES_DIRECTORY = System
			.getProperty("user.dir") + "/src/test/resources/";

	@Autowired
	private PageParser parser;

	@Before
	public void checkPreconditions() {
		assertThat(parser, notNullValue());
	}

	@Test
	public void returnsAllLigaLinks() throws IOException {
		File testFile = new File(RECOURCES_DIRECTORY + "uebersicht.html");
		Document doc = Jsoup.parse(testFile, "UTF-8", "");

		List<String> ligaLinksIDs = parser.findLigaLinks(doc);

		assertThat(ligaLinksIDs.size(), is(5));
		assertThat(
				ligaLinksIDs.get(0),
				is("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=veranstaltung&veranstaltungid=8"));
	}
}
