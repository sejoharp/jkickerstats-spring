package jkickerstats.interfaces;

import jkickerstats.persistence.MatchLister;
import jkickerstats.services.CsvCreator;
import jkickerstats.services.StatsUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


@Component
@EnableScheduling
public class StatsUpdaterBatch {
    private static final Logger LOG = Logger.getLogger(StatsUpdaterBatch.class.getName());

    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

    @Autowired
    private MatchLister matchLister;

    @Value("${csv.file.path}")
    private String csvFilePath;

    @Scheduled(cron = "0 0 2 * * *")
    public void schedule() throws IOException {
        statsUpdater.updateStats(null);
        List<String> csvList = csvCreator.createCsvRowList(matchLister.getAllMatches());
        writeFile(csvList);
    }

    private void writeFile(List<String> csvList) throws IOException {
        LOG.info("write csv file started.");
        FileWriter writer = new FileWriter(csvFilePath);

        csvList.forEach(row -> {
            writeLine(writer, row);
        });

        writer.flush();
        writer.close();
        LOG.info("write csv file finished.");
    }

    private void writeLine(FileWriter writer, String row) {
        try {
            writer.write(row);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
