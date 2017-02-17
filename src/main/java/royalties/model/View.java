package royalties.model;

/**
 * Signifies that an episode was viewed by someone
 */
public class View {
    private String episodeId;
    private String name;

    public View(String episodeId, String name) {
        this.episodeId = episodeId;
        this.name = name;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public String getName() {
        return name;
    }
}
