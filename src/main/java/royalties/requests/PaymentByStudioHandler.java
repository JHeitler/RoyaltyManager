package royalties.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import royalties.dal.RoyaltyDao;
import royalties.dto.PaymentForStudio;
import royalties.model.Studio;
import royalties.model.View;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Handles the payment for a single studio request
 *
 * /royaltymanager/payments/{owner}

 * This request returns a royalties due and number of episodes watched by viewers for the given studio.
 * Payment is based on the number of viewings of any episode from the studio.
 */
public class PaymentByStudioHandler implements RequestHandler<String, PaymentForStudio> {
    private static final Logger log = LoggerFactory.getLogger(PaymentByStudioHandler.class);

    @Autowired
    RoyaltyDao dao;

    @Override
    public PaymentForStudio manageRequest(String owner) {
        log.info(String.format("Getting payments for studio %s", owner));
        Optional<Studio> studio = dao.getStudio(owner);
        if (studio.isPresent()) {
            List<View> allViews = dao.getViewingsByStudio(owner);
            BigDecimal totalRoyalties = studio.get().getPayment().multiply(new BigDecimal(allViews.size()));
            PaymentForStudio payments = new PaymentForStudio(studio.get().getName(), totalRoyalties, allViews.size());
            log.info("Returning " + payments.toString());
            return payments;
        } else {
            log.error("Studio not found");
            throw new IllegalArgumentException("Studio not found");
        }
    }
}
