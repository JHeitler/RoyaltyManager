package royalties.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Information specific to a particular studio
 *
 * Note that this is not immutable and should be converted to an immutable object as part of the model.
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class InputStudio {
    private String id;
    private String name;
    private BigDecimal payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", id, name, payment);
    }
}
