package royalties.dto;

import java.math.BigDecimal;

/**
 * Holds the details of payments due as required by the response to the AllPayments request.
 */
public class PaymentForAllStudios {
    private String rightsowner;
    private String name;
    private BigDecimal royalty;
    private Integer viewings;

    public PaymentForAllStudios(String rightsowner, String name, BigDecimal royalty, Integer viewings) {
        this.rightsowner = rightsowner;
        this.name = name;
        this.royalty = royalty;
        this.viewings = viewings;
    }

    public String getRightsowner() {
        return rightsowner;
    }

    public String getName() {
        return name;
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
