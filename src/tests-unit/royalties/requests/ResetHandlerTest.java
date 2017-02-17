package royalties.requests;

/**
 * Tests for the Reset request
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import royalties.RoyaltyTestConfig;
import royalties.dal.RoyaltyDao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the reset handler
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoyaltyTestConfig.class)
public class ResetHandlerTest {
    @Autowired
    RoyaltyDao dao;

    @Autowired
    ResetHandler resetHandler;

    @Test
    public void resetHandlerClearsViewingInformation() {
        NoParams req = new NoParams();
        HttpStatus result = resetHandler.manageRequest(req);
        assertThat(result).isEqualTo(HttpStatus.ACCEPTED);
        verify(dao).resetViewings();
    }
}
