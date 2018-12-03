package jkickerstats.interfaces;

import jkickerstats.types.Game;
import jkickerstats.types.Match;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
class CsvCreator {
    void createCsvFile(List<String> csvRowList) {
        Path path = Paths.get("allGames.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardCharsets.UTF_8)) {
            for (String csvRow : csvRowList) {
                writer.write(csvRow);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> createCsvRowList(List<Match> matches) {
        List<String> csvList = new ArrayList<>();
        matches.forEach(match -> match.getGames()
                .forEach(game -> {
                    addRow(csvList, match, game);
                }));
        return csvList;
    }

    private void addRow(List<String> csvList, Match match, Game game) {
        List<String> content = new ArrayList<>();
        content.add(formatDate(match.getMatchDate()));
        content.add(String.valueOf(match.getMatchDay()));
        content.add(String.valueOf(game.getPosition()));
        content.add(match.getHomeTeam());
        content.add(replaceEmptyNames(game.getHomePlayer1()));
        content.add(replaceEmptyNames(game.getHomePlayer2()));
        content.add(String.valueOf(game.getHomeScore()));
        content.add(String.valueOf(game.getGuestScore()));
        content.add(replaceEmptyNames(game.getGuestPlayer1()));
        content.add(replaceEmptyNames(game.getGuestPlayer2()));
        content.add(match.getGuestTeam());
        csvList.add(String.join(";", content));
    }

    String formatDate(Date matchDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(matchDate);
    }

    String replaceEmptyNames(String name) {
        String placeHolder = "XXXX";
        if (name == null) {
            return placeHolder;
        } else if (name.isEmpty()) {
            return placeHolder;
        } else {
            return name;
        }
    }
}
