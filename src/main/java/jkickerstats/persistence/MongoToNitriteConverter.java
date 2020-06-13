package jkickerstats.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MongoToNitriteConverter {

    private final MongoMatchRepo mongoMatchRepo;

    private NitriteMatchRepository nitriteMatchRepository = new NitriteMatchRepository("nitrite-kickerstats.db");

    @Autowired
    public MongoToNitriteConverter(MongoMatchRepo mongoMatchRepo) {
        this.mongoMatchRepo = mongoMatchRepo;
        convert();
    }

    public void convert() {
        System.out.println("matches in mongodb: " + mongoMatchRepo.countMatches());
        mongoMatchRepo.getAllMatches().forEach(match -> nitriteMatchRepository.save(match));
        System.out.println("matches in nitrite: " + nitriteMatchRepository.countMatches());
    }
}
