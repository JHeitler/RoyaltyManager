package royalties.dto;

import java.math.BigDecimal;

/**
 * Holds the details of payments due as required by the response to the PaymentsByStudio request.
 */
public class PaymentForStudio {
    private String rightsowner;
    private BigDecimal royalty;
    private Integer viewings;

    public PaymentForStudio(String rightsowner, BigDecimal royalty, Integer viewings) {
        this.rightsowner = rightsowner;
        this.royalty = royalty;
        this.viewings = viewings;
    }

    public String getRightsowner() {
        return rightsowner;
    }

    public BigDecimal getRoyalty() {
        return royalty;
    }

    public Integer getViewings() {
        return viewings;
    }

    public String toString() {
        return String.format("{%s %S %s}", rightsowner, royalty, viewings);
    }
}
