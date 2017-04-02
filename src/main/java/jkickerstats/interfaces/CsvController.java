package jkickerstats.interfaces;

import jkickerstats.domain.MatchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
public class CsvController {

    @Autowired
    private MatchRepo matchRepo;

    @Autowired
    private StatsUpdater statsUpdater;

    @Autowired
    private CsvCreator csvCreator;

    @RequestMapping("/start")
    public void collectMatches(HttpSession session, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        statsUpdater.updateStats();
    }

    @RequestMapping("/csvexport")
    public void exportAsCSV(HttpSession session, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        List<String> csvList = csvCreator.createCsvRowList(matchRepo
                .getAllMatches());

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
