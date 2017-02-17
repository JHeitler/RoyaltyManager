package royalties.requests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import royalties.RoyaltyTestConfig;
import royalties.dal.RoyaltyDao;
import royalties.model.Episode;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

/**
 * Unit tests for the viewing handler
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoyaltyTestConfig.class)
public class ViewingHandlerTest {
    @Autowired
    RoyaltyDao dao;

    @Autowired
    ViewingHandler viewingHandler;

    @Test
    public void HandlerReturnsAcceptedIfEpisodeExists() {
        when(dao.getEpisode("123")).thenReturn(Optional.of(new Episode("123", "thing", "xxx")));
        ViewingRequest req = new ViewingRequest();
        req.setCustomer("me");
        req.setEpisode("123");
        HttpStatus result = viewingHandler.manageRequest(req);
        assertThat(result).isEqualTo(HttpStatus.ACCEPTED);
        verify(dao).addViewing("123", "me");
    }

    @Test
    public void HandlerReturnsBadRequestIfEposideDoesntExist() {
        when(dao.getEpisode("456")).thenReturn(Optional.empty());
        ViewingRequest req = new ViewingRequest();
        req.setCustomer("me");
        req.setEpisode("456");
        HttpStatus result = viewingHandler.manageRequest(req);
        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
