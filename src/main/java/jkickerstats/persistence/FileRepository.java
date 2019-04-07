package jkickerstats.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import jkickerstats.domain.Match;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FileRepository implements MatchPersister, MatchLister {

    static final String PATH = "cache/";

    public static FileRepository fileRepository() {
        return new FileRepository();
    }

    public long countMatches() {
        return findMatchFiles().count();
    }

    public void save(Match match) {
        FileMatch fileMatch = FileMatch.from(match);
        try {
            Files.createDirectories(Paths.get(PATH));
            FileOutputStream fileOutputStream = new FileOutputStream(PATH + fileMatch.fileName());
            new ObjectMapper().writeValue(fileOutputStream, fileMatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewMatch(Match match) {
        FileMatch fileMatch = FileMatch.from(match);
        return findMatchFiles().noneMatch(path -> path.getFileName().toString().equals(fileMatch.fileName()));
    }

    @Override
    public List<Match> getAllMatches() {
        return findMatchFiles()
                .map(this::readFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FileMatch::toMatch)
                .collect(toList());
    }

    private Stream<Path> findMatchFiles() {
        try {
            return Files.walk(Paths.get(PATH), 1)
                    .filter(Files::isRegularFile)
                    .filter(this::isMatchFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    Optional<FileMatch> readFile(Match match) {
        return readFile(Paths.get(PATH + FileMatch.from(match).fileName()));
    }

    Optional<FileMatch> readFile(Path path) {
        try {
            FileMatch fileMatch = new ObjectMapper().readValue(path.toUri().toURL(), FileMatch.class);
            return Optional.ofNullable(fileMatch);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    boolean isMatchFile(Path path) {
        return Pattern.matches(".{4}-.{2}-.{2}_.*_*.json", path.getFileName().toString());
    }

}
