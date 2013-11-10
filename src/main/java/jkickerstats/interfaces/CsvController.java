package jkickerstats.interfaces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jkickerstats.usecases.MatchServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class CsvController {
	@RestController
	public class HelloController {

		@Autowired
		private MatchServiceInterface matchService;

		@Autowired
		private CsvCreator csvCreator;

		@RequestMapping("/csvexport")
		public String exportAsCSV(HttpSession session,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException {
			List<String> csvList = csvCreator.createCsvRowList(matchService
					.getAllMatches());

			response.setContentType("text/csv;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			for (String row : csvList) {
				writer.write(row);
				writer.write("\n");
			}
			writer.flush();
			writer.close();
			return "Greetings from Spring Boot!";

		}

	}
}
