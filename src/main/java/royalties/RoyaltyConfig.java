package royalties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import royalties.dal.RoyaltyDao;
import royalties.dal.RoyaltyDaoJsonFileImpl;
import royalties.requests.AllPaymentsHandler;
import royalties.requests.PaymentByStudioHandler;
import royalties.requests.ResetHandler;
import royalties.requests.ViewingHandler;

@Configuration
public class RoyaltyConfig {
    @Bean
    public RoyaltyDao dao() throws Exception {
        return new RoyaltyDaoJsonFileImpl();
    }

    @Bean
    public ViewingHandler viewingHandler() {
        return new ViewingHandler();
    }

    @Bean
    public ResetHandler resetHandler() {
        return new ResetHandler();
    }

    @Bean
    public PaymentByStudioHandler paymentByStudioHandler() { return new PaymentByStudioHandler(); }

    @Bean
    public AllPaymentsHandler allPaymentsHandler() { return new AllPaymentsHandler(); }
}
