package jkickerstats.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StatsUpdaterBatch {
	@Autowired
	private StatsUpdater statsUpdater;

	@Scheduled(cron="0 0 2 * * *")
	public void schedule() {
		statsUpdater.updateStats();
	}
}
