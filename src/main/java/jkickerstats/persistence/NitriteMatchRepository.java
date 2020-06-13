package jkickerstats.persistence;

import jkickerstats.domain.Match;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Repository
public class NitriteMatchRepository implements MatchLister, MatchPersister {
    private final ObjectRepository<NitriteMatch> nitriteMatchObjectRepository;
    private final Nitrite db;

    public NitriteMatchRepository(@Value("${nitrite.db.path}") String path) {
        db = Nitrite.builder()
                .filePath(path)
                .openOrCreate();

        nitriteMatchObjectRepository = db.getRepository(NitriteMatch.class);
    }

    void deleteAllMatches() {
        nitriteMatchObjectRepository.drop();
    }

    @Override
    public long countMatches() {
        return nitriteMatchObjectRepository.size();
    }


    void closeDb() {
        db.close();
    }

    @Override
    public List<Match> getAllMatches() {
        return StreamSupport
                .stream(nitriteMatchObjectRepository.find().spliterator(), false)
                .map(NitriteMatch::toMatch)
                .collect(toList());
    }

    @Override
    public void save(Match match) {
        nitriteMatchObjectRepository.insert(NitriteMatch.fromMatch(match));
    }
}
