package transaction.test.utils;

import org.springframework.stereotype.Service;

@Service
public class TransactionUtils {

    private final static String API_KEY = "5cc7f374a0234c618247c4b8fefcd2ac";
    
    public String getUrl(String from) {
        return "https://api.twelvedata.com/exchange_rate?symbol=" + from + "/USD&apikey=" + API_KEY;
    }    
}