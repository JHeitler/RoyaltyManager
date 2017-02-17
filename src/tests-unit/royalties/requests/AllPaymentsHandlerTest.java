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
import royalties.dto.AllPayments;
import royalties.dto.PaymentForAllStudios;
import royalties.model.Episode;
import royalties.model.Studio;
import royalties.model.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for AllPaymentsHandler
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoyaltyTestConfig.class)
public class AllPaymentsHandlerTest {
    @Autowired
    RoyaltyDao dao;

    @Autowired
    AllPaymentsHandler allPaymentsHandler;

    private final String FILMMANIA_NAME = "FIlm Mania";
    private final String FILMMANIA_ID = "1a2b3c4d5e6f";
    private final String ECHOES_NAME = "Echoes of The Past";
    private final String ECHOES_ID = "oooooohhhhhhaaaahhhh";
    private final String EPIC_NAME = "Epic Films 2017";
    private final String EPIC_ID = "bleep123bleep123";
    private final Studio filmMania = new Studio(FILMMANIA_ID, FILMMANIA_NAME, new BigDecimal("13.50"));
    private final Studio echoesOfThePast = new Studio(ECHOES_ID, ECHOES_NAME, new BigDecimal("19.75"));
    private final Studio epicFIlms = new Studio(EPIC_ID, EPIC_NAME, new BigDecimal("18.20"));


    @Before
    public void setUpMockDao() {
        List<Studio> studios = new ArrayList<>();
        studios.add(filmMania);
        studios.add(echoesOfThePast);
        studios.add(epicFIlms);
        when(dao.getAllStudios()).thenReturn(studios);

        List<View> filmmaniaViewings = new ArrayList<>();
        filmmaniaViewings.add(new View("aabbccdd", "Theatre Of Evanescense - part 1"));
        filmmaniaViewings.add(new View("aabb0011", "Theatre Of Evanescense - part 2"));

        List<View> echoesOfThePastViewings = new ArrayList<>();
        echoesOfThePastViewings.add(new View("oooo", "Great Insects"));

        List<View> epicFIlmsViews = new ArrayList<>();
        epicFIlmsViews.add(new View("bleep1", "Ancestral recall"));
        epicFIlmsViews.add(new View("bleep2", "The Filming Of Ben Hur"));
        epicFIlmsViews.add(new View("bleep3", "The Filming Of Ben Hur - The sequel"));
        epicFIlmsViews.add(new View("bleep4", "Atlas Shrugged, The Musical"));

        when(dao.getViewingsByStudio(FILMMANIA_ID)).thenReturn(filmmaniaViewings);
        when(dao.getViewingsByStudio(ECHOES_ID)).thenReturn(echoesOfThePastViewings);
        when(dao.getViewingsByStudio(EPIC_ID)).thenReturn(epicFIlmsViews);
    }

    private void checkPayment(PaymentForAllStudios payment, String rightsOwner, String name, BigDecimal royalties, Integer viewings ) {
        assertThat(payment.getRightsowner()).isEqualTo(rightsOwner);
        assertThat(payment.getName()).isEqualTo(name);
        assertThat(payment.getRoyalty()).isEqualTo(royalties);
        assertThat(payment.getViewings()).isEqualTo(viewings);
    }

    @Test
    public void AllPaymentsHandlerReturnsAllPayments() {
        AllPayments response = allPaymentsHandler.manageRequest(new NoParams());
        List<PaymentForAllStudios> payments = response.getPayments();
        assertThat(payments.size()).isEqualTo(3);
        checkPayment(payments.get(0), FILMMANIA_ID, FILMMANIA_NAME, new BigDecimal("27.00"), 2);
        checkPayment(payments.get(1), ECHOES_ID, ECHOES_NAME, new BigDecimal("19.75"), 1);
        checkPayment(payments.get(2), EPIC_ID, EPIC_NAME, new BigDecimal("72.80"), 4);
    }
}
