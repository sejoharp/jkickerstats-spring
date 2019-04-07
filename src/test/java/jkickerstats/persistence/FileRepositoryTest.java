package jkickerstats.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import jkickerstats.domain.Match;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static jkickerstats.MatchTestdata.createMatchWithDoubleGame;
import static jkickerstats.MatchTestdata.createTestMatch;
import static jkickerstats.persistence.FileRepository.PATH;
import static jkickerstats.persistence.FileRepository.fileRepository;
import static org.assertj.core.api.Assertions.assertThat;

public class FileRepositoryTest {

    private static final String HOMETEAM_GUESTTEAM_FILE = "2013-02-27_hometeam_guestteam.json";
    private static final String CIMBOMBOM_DIEMASCHINERIE_FILE = "2013-02-28_CimBomBom_DieMaschinerie.json";
    private static final String TEST_RESOURCES_PATH = "src/test/resources/";

    @After
    public void after() {
        new File(PATH + HOMETEAM_GUESTTEAM_FILE).delete();
        new File(PATH + CIMBOMBOM_DIEMASCHINERIE_FILE).delete();
    }

    @Test
    public void writesMatch() throws IOException {
        //given
        Match match = createTestMatch();

        //when
        fileRepository().save(match);

        //then
        FileMatch fileMatch = readFile(PATH + HOMETEAM_GUESTTEAM_FILE);
        assertThat(fileMatch).isEqualTo(FileMatch.from(match));
    }

    @Test
    public void readsMatchFromFileName() {
        //given
        Match match = createTestMatch();
        String path = "src/test/resources/" + HOMETEAM_GUESTTEAM_FILE;

        //when
        Optional<FileMatch> fileMatch = fileRepository().readFile(Paths.get(path));

        //then
        assertThat(fileMatch).isEqualTo(Optional.of(FileMatch.from(match)));
    }

    @Test
    public void handlesNonExistingFile() throws IOException {
        //given
        String path = "src/test/resources/does-not-exist.json";

        //when
        Optional<FileMatch> fileMatch = fileRepository().readFile(Paths.get(path));

        //then
        assertThat(fileMatch).isEmpty();
    }

    @Test
    public void readsAllMatches() throws IOException {
        //given
        String filename1 = HOMETEAM_GUESTTEAM_FILE;
        String filename2 = CIMBOMBOM_DIEMASCHINERIE_FILE;
        String path = TEST_RESOURCES_PATH;
        copyFile(filename1);
        copyFile(filename2);

        //when
        List<Match> allMatches = fileRepository().getAllMatches();

        //then
        assertThat(allMatches).hasSize(2);
        assertThat(allMatches).contains(createTestMatch(), createMatchWithDoubleGame());
    }

    @Test
    public void detectsFilePattern() {
        //given
        String pattern = HOMETEAM_GUESTTEAM_FILE;

        //when
        boolean matchFile = fileRepository().isMatchFile(Paths.get(pattern));

        //then
        assertThat(matchFile).isTrue();
    }

    @Test
    public void detectsFalseFilePattern() {
        //given
        String pattern = "201-02-27_hometeam_guestteam.json";

        //when
        boolean matchFile = fileRepository().isMatchFile(Paths.get(pattern));

        //then
        assertThat(matchFile).isFalse();
    }

    @Test
    public void countsMatchFiles() throws IOException {
        //given
        copyFile(HOMETEAM_GUESTTEAM_FILE);
        copyFile(CIMBOMBOM_DIEMASCHINERIE_FILE);

        //when
        long matchFile = fileRepository().countMatches();

        //then
        assertThat(matchFile).isEqualTo(2);
    }

    @Test
    public void countsMatchFiles2() {
        //given

        //when
        long matchFile = fileRepository().countMatches();

        //then
        assertThat(matchFile).isEqualTo(0);
    }

    @Test
    public void detectsMatchFiles() throws IOException {
        //given
        copyFile(HOMETEAM_GUESTTEAM_FILE);
        copyFile(CIMBOMBOM_DIEMASCHINERIE_FILE);

        //when
        boolean matchFile = fileRepository().noMatchesAvailable();

        //then
        assertThat(matchFile).isFalse();
    }

    @Test
    public void detectsMissingMatchFiles() {
        //given

        //when
        boolean matchFile = fileRepository().noMatchesAvailable();

        //then
        assertThat(matchFile).isTrue();
    }

    @Test
    public void detectsNewMatch() {
        //given
        Match testMatch = createTestMatch();

        //when
        boolean matchFile = fileRepository().isNewMatch(testMatch);

        //then
        assertThat(matchFile).isTrue();
    }

    @Test
    public void detectsOldMatch() throws IOException {
        //given
        copyFile(HOMETEAM_GUESTTEAM_FILE);

        Match testMatch = createTestMatch();

        //when
        boolean matchFile = fileRepository().isNewMatch(testMatch);

        //then
        assertThat(matchFile).isFalse();
    }

    private void copyFile(String filename) throws IOException {
        Files.copy(Paths.get(TEST_RESOURCES_PATH + filename), Paths.get(PATH + filename));
    }

    private FileMatch readFile(String fileName) throws IOException {
        File file = new File(fileName);
        return new ObjectMapper().readValue(file, FileMatch.class);
    }
}