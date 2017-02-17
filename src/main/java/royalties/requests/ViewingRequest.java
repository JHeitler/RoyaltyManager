package royalties.requests;

/**
 * Details of the Viewing request received
 */
public class ViewingRequest {
    private String episode;
    private String customer;

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return String.format("Viewing Request {%s, %s}", episode, customer);
    }
}
