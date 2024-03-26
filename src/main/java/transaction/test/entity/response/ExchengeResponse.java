package transaction.test.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchengeResponse {
    private String symbol;
    private double rate;
    private long timestamp;
}