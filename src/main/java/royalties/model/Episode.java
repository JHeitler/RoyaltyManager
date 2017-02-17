package royalties.model;

/**
 * An immutable version of an episode
 */
public class Episode {
    private String id;
    private String name;
    private String rightsowner;

    public Episode(String id, String name, String rightsowner) {
        this.id = id;
        this.name = name;
        this.rightsowner = rightsowner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRightsowner() {
        return rightsowner;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", id, name, rightsowner);
    }
}
