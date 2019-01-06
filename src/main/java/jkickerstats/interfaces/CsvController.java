package jkickerstats.interfaces;

import jkickerstats.persistence.MatchLister;
import jkickerstats.services.CsvCreator;
import jkickerstats.services.StatsUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CsvController {
    private static final Logger LOG = Logger.getLogger(CsvController.class.getName());

    @Autowired
    private MatchLister matchLister;

    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

    @RequestMapping("/start")
    public void collectMatches(HttpSession session, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        statsUpdater.updateStats(null);
    }

    @RequestMapping(path = "/season/{id}", method = POST)
    public void updateSeason(@PathVariable int id) throws IOException {
        statsUpdater.updateStats(id);

        LOG.info(String.format("season %d updated.", id));
    }

    @RequestMapping("/csvexport")
    public void exportAsCSV(HttpSession session,
                            HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {
        List<String> csvList = csvCreator.createCsvRowList(matchLister
                .getAllMatches());
        writeRespone(response, csvList);

        LOG.info("cvs export delivered.");
    }

    private void writeRespone(HttpServletResponse response, List<String> csvList) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=allGames.csv");
        PrintWriter writer = response.getWriter();

        csvList.forEach(row -> {
            writer.write(row);
            writer.write("\n");
        });

        writer.flush();
        writer.close();
    }
}
