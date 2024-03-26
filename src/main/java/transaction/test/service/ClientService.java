package transaction.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import transaction.test.entity.Expense;
import transaction.test.entity.Limit;
import transaction.test.repository.ExpenseRepository;
import transaction.test.repository.LimitRepository;

@Service
public class ClientService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private LimitRepository limitRepository;

    public Limit saveLimit(Limit limit) {
        return limitRepository.save(
                Limit.builder()
                        .limitSum(getLimitSum(limit))
                        .limitDatetime(new Date())
                        .limitCurrencyShortname("USD")
                        .limitCategory(limit.getLimitCategory())
                        .build());
    }

    private BigDecimal getLimitSum(Limit limit) {
        return Optional.ofNullable(limit.getLimitSum()).orElseGet(() -> new BigDecimal(1000));
    }

    public List<Expense> getAllTransactions() {
        return expenseRepository.findAll();
    }

    public List<Expense> getTransactionsExceededLimit() {
        return expenseRepository.findAllExceededLimit();
    }
}