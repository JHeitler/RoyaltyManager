package royalties.dal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import royalties.dto.Episodes;
import royalties.dto.Studios;
import royalties.model.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dao implemented as JSON files held on disk.
 *
 * Note: all changes are held locally so nothing is persisted.
 * In the real world this would have a database behind it but this is
 * good enough to verify the functionality of the service interface
 */
public class RoyaltyDaoJsonFileImpl implements RoyaltyDao, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(RoyaltyDaoJsonFileImpl.class);
    private List<Studio> studios;
    private List<Episode> episodes;
    private List<View> views = Collections.synchronizedList(new ArrayList<>());

//    @Autowired
//    private ResourceLoader resourceLoader;

    public RoyaltyDaoJsonFileImpl() throws Exception {
        log.info("JSON File DAO starting up");
    }

    /*
    * This ought to have worked by picking up the files from the classpath, but didn't...
     */
    public void afterPropertiesSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
//        Studios studioData = mapper.readerFor(Studios.class).readValue(resourceLoader.getResource("classpath:data/studios.json").getInputStream());
        // FIXME: There is a better way to pick up a resource from the classpath, but I can't get it to work...
        Studios studioData = mapper.readerFor(Studios.class).readValue(new File("L:/Documents and Work/Java Projects/RoyaltyManager/src/main/java/royalties/dal/data/studios.json"));
        log.info(studioData.toString());
        // And make the studio data immutable
        studios = Collections.unmodifiableList(studioData.getStudios().stream().map(is -> new Studio(is.getId(), is.getName(), is.getPayment())).collect(Collectors.toList()));

//        Episodes episodeData = mapper.readerFor(Episodes.class).readValue(resourceLoader.getResource("classpath:data/episodes.json").getInputStream());
        Episodes episodeData = mapper.readerFor(Episodes.class).readValue(new File("L:/Documents and Work/Java Projects/RoyaltyManager/src/main/java/royalties/dal/data/episodes.json"));
        log.info(episodeData.toString());
        // And make the episode data immutable
        episodes = Collections.unmodifiableList(episodeData.getEpisodes().stream().map(ie -> new Episode(ie.getId(), ie.getName(), ie.getRightsowner())).collect(Collectors.toList()));
    }

    @Override
    public Optional<Episode> getEpisode(String episodeId) {
        return episodes.stream().filter(e -> e.getId().equals(episodeId)).findFirst();
    }

    @Override
    public Optional<Studio> getStudio(String studioId) {
        return studios.stream().filter(s -> s.getId().equals(studioId)).findFirst();
    }

    @Override
    public List<Studio> getAllStudios() {
        // This is safe because the list is immutable
        return studios;
    }

    @Override
    public void addViewing(String episodeId, String name) {
        View viewing = new View(episodeId, name);
        views.add(viewing);
    }

    @Override
    public void resetViewings() {
        views.clear();
    }

    @Override
    public List<View> getViewingsByStudio(String owner) {
        Set<String> relevantEpisodes = episodes.stream().filter(e -> e.getRightsowner().equals(owner)).map(e -> e.getId()).collect(Collectors.toSet());
        List<View> ownerViews = views.stream().filter(v -> relevantEpisodes.contains(v.getEpisodeId())).collect(Collectors.toList());
        return ownerViews;
    }
}
