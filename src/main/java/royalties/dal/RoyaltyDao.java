package royalties.dal;

import royalties.model.Episode;
import royalties.model.Studio;
import royalties.model.View;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface
 */
public interface RoyaltyDao {
    Optional<Episode> getEpisode(String episodeId);

    Optional<Studio> getStudio(String studioId);

    List<Studio> getAllStudios();

    void addViewing(String episodeId, String name);

    void resetViewings();

    List<View> getViewingsByStudio(String owner);
}
