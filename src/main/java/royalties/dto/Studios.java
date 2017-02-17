package royalties.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds the studios read in from the DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Studios {
    private List<InputStudio> studios;

    public void setStudios(List<InputStudio> studios) {
        this.studios = studios;
    }

    public List<InputStudio> getStudios() {
        return studios;
    }

    @Override
    public String toString() {
        return "Studios: \n" + getStudios().stream().map(s -> s.toString()).collect(Collectors.joining("\n"));
    }
}
