package royalties;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import royalties.dal.RoyaltyDao;
import royalties.requests.AllPaymentsHandler;
import royalties.requests.PaymentByStudioHandler;
import royalties.requests.ResetHandler;
import royalties.requests.ViewingHandler;

/**
 * Allow us to inject mock items to the test classes
 */
@Profile("test")
@Configuration
public class RoyaltyTestConfig {
    @Lazy
    @Bean
    @Primary
    public RoyaltyDao dao() {
        return Mockito.mock(RoyaltyDao.class);
    }

    @Lazy
    @Bean
    @Primary
    public ViewingHandler viewingHandler() {
        return new ViewingHandler();
    }

    @Lazy
    @Bean
    @Primary
    public ResetHandler resetHandler() {
        return new ResetHandler();
    }

    @Lazy
    @Bean
    @Primary
    public PaymentByStudioHandler paymentByStudioHandler() { return new PaymentByStudioHandler(); }

    @Lazy
    @Bean
    @Primary
    public AllPaymentsHandler allPaymentsHandler() { return new AllPaymentsHandler(); }
}
