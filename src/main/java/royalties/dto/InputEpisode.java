package royalties.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Information about a single episode from teh JSON file.
 *
 * Note that this is not immutable and should be converted to an immutable object as part of the model.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class InputEpisode {
    private String id;
    private String name;
    private String rightsowner;

    public InputEpisode() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRightsowner() {
        return rightsowner;
    }

    public void setRightsowner(String rightsowner) {
        this.rightsowner = rightsowner;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", id, name, rightsowner);
    }
}
