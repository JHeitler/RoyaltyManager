package royalties.requests;

import org.springframework.beans.factory.annotation.Autowired;
import royalties.dal.RoyaltyDao;
import royalties.dto.AllPayments;
import royalties.dto.PaymentForAllStudios;
import royalties.model.Studio;
import royalties.model.View;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the All Payments request.
 *
 * /royaltymanager/payments
 *
 * This request returns a record for each studio that details the royalties due and the number of episodes watched by viewers.
 * Payment is based on the number of viewings of any episode from the studio.
 */
public class AllPaymentsHandler implements RequestHandler<NoParams, AllPayments> {
    @Autowired
    RoyaltyDao dao;

    @Override
    public AllPayments manageRequest(NoParams request) {
        List<Studio> studios = dao.getAllStudios();
        List<PaymentForAllStudios> payments = studios.stream().map( studio -> {
            List<View> views = dao.getViewingsByStudio(studio.getId());
            return new PaymentForAllStudios(studio.getId(), studio.getName(), studio.getPayment().multiply(new BigDecimal(views.size())), views.size());
        }).collect(Collectors.toList());
        AllPayments response = new AllPayments();
        response.setPayments(payments);
        return response;
    }
}
