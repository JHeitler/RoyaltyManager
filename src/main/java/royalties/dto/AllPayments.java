package royalties.dto;

import java.util.List;

/**
 * Response object for the AllPayments request
 */
public class AllPayments {
    // I'm sure there is an annotation somewhere to prevent the serialized Json from adding "payments" before the list of payments
    // But I can't find it anywhere.
    private List<PaymentForAllStudios> payments;

    public List<PaymentForAllStudios> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentForAllStudios> payments) {
        this.payments = payments;
    }
}
