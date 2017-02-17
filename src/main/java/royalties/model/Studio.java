package royalties.model;

import java.math.BigDecimal;

/**
 * Immutable version of a studio
 */
public class Studio {
    private String id;
    private String name;
    private BigDecimal payment;

    public Studio (String id, String name, BigDecimal payment) {
        this.id = id;
        this.name = name;
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPayment() {
        return payment;
    }
}
