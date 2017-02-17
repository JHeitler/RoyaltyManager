package royalties.requests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import royalties.RoyaltyTestConfig;
import royalties.dal.RoyaltyDao;
import royalties.dto.PaymentForStudio;
import royalties.model.Studio;
import royalties.model.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for payments by studio request
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoyaltyTestConfig.class)
public class PaymentByStudioHandlerTest {
    @Autowired
    RoyaltyDao dao;

    @Autowired
    PaymentByStudioHandler paymentByStudioHandler;

    private final String BAD_ID =  "Unknown";
    private final String GOOD_ID = "NiceName";
    private final String GOOD_NAME = "Filmmania";
    private final String UNPOPULAR_ID = "badchoice";
    private final String UNPOPULAR_NAME = "NoiseomeFilms";

    @Before
    public void setUpMockDao() {
        when(dao.getStudio(BAD_ID)).thenReturn(Optional.empty());
        when(dao.getStudio(GOOD_ID)).thenReturn(Optional.of(new Studio(GOOD_ID, GOOD_NAME, new BigDecimal("12.30"))));
        when(dao.getStudio(UNPOPULAR_ID)).thenReturn(Optional.of(new Studio(UNPOPULAR_ID, UNPOPULAR_NAME, new BigDecimal("21.50"))));
        List<View> allViews = new ArrayList<>();
        allViews.add(new View("abcd1234", "sillyUser"));
        allViews.add(new View("abcd1355", "sensibleUser"));
        allViews.add(new View("dbca5555", "curiousUser"));
        when(dao.getViewingsByStudio(GOOD_ID)).thenReturn(allViews);
        when(dao.getViewingsByStudio(UNPOPULAR_ID)).thenReturn(new ArrayList<>());
    }

    @Test
    public void paymentByStudioHandlerReturnsErrorIfStudioNotFound() {
        // Send in a bad studio.
        try {
            paymentByStudioHandler.manageRequest(BAD_ID);
            fail("Expected an exception");
        } catch (IllegalArgumentException iae) {
            assertThat(iae.getMessage().equals("Studio not found"));
        }
    }

    @Test
    public void paymentByStudioHandlerReturnsViewingsIfStudioFound() {
        PaymentForStudio payment = paymentByStudioHandler.manageRequest(GOOD_ID);
        assertThat(payment.getRightsowner().equals(GOOD_NAME));
        assertThat(payment.getViewings() == 3);
        assertThat(payment.getRoyalty().equals(new BigDecimal("36.90")));
    }

    @Test
    public void paymentByStudioHandlerCorrectlyHandlesNoViewings() {
        PaymentForStudio payment = paymentByStudioHandler.manageRequest(UNPOPULAR_ID);
        assertThat(payment.getRightsowner().equals(UNPOPULAR_NAME));
        assertThat(payment.getViewings() == 0);
        assertThat(payment.getRoyalty().equals(new BigDecimal("0.00")));
    }
}
