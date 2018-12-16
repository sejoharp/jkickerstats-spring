package jkickerstats.interfaces;

import jkickerstats.types.Game;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

import static jkickerstats.types.Match.createMatch;

@Component
class PageParser {
    static Stream<String> findLigaLinks(Document doc) {
        Elements elements = doc.select("table.contentpaneopen > tbody > tr > td > a.readon");
        return elements.stream()
                .map(element -> MatchParser.DOMAIN + element.attr("href"))
                .filter(link -> !link.contains("ticker"));
    }

    static Stream<Integer> findSeasonIDs(Document doc) {
        Elements elements = doc.select("select option");
        return elements.stream()
                .map(element -> Integer.valueOf(element.attr("value")));
    }
}
