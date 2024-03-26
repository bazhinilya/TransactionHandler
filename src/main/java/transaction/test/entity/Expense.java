package transaction.test.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int accountFrom;
    private int accountTo;
    private String currencyShortname;
    private BigDecimal sum;
    private String expenseCategory;
    private Date datetime;

    private BigDecimal remainingMonthLimit;
    private boolean limitExceeded;

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private Limit limit;
}