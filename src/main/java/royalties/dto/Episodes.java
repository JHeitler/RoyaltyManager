package royalties.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains the episodes loaded from the JSON file.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class Episodes {
    private List<InputEpisode> episodes;

    public void setEpisodes(List<InputEpisode> episodes) {
        this.episodes = episodes;
    }

    public List<InputEpisode> getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return "Episodes: \n" + getEpisodes().stream().map(s -> s.toString()).collect(Collectors.joining("\n"));
    }
}
