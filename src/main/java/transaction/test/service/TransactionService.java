package transaction.test.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import transaction.test.entity.ExchengeRate;
import transaction.test.entity.Expense;
import transaction.test.entity.Limit;
import transaction.test.entity.response.ExchengeResponse;
import transaction.test.repository.ExchengeRatesRepository;
import transaction.test.repository.ExpenseRepository;
import transaction.test.repository.LimitRepository;
import transaction.test.utils.TransactionUtils;

@Service
public class TransactionService {

    private WebClient.Builder webClient = WebClient.builder();
    @Autowired
    private ExchengeRatesRepository ratesRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private LimitRepository limitRepository;
    @Autowired
    private TransactionUtils utils;

    public Expense saveExpense(Expense transaction) {
        Date currentDate = new Date();
        String expenseCategory = transaction.getExpenseCategory();
        Limit nearestLimit = getNearestLimitByCurrentDate(currentDate, expenseCategory);
        Expense nearstRemaining = getNearestRemainingLimitByCurrentDate(currentDate, expenseCategory);

        BigDecimal currentRemainingMonthLimit = getNearestDate(nearestLimit, nearstRemaining);
        BigDecimal transactionSum = transaction.getSum();
        BigDecimal remainingMonthLimit = getRemainingMonthLimit(currentRemainingMonthLimit, transactionSum);

        return expenseRepository.save(
                Expense.builder()
                        .accountFrom(transaction.getAccountFrom())
                        .accountTo(transaction.getAccountTo())
                        .currencyShortname(transaction.getCurrencyShortname())
                        .sum(transactionSum)
                        .expenseCategory(expenseCategory)
                        .datetime(currentDate)
                        .remainingMonthLimit(remainingMonthLimit)
                        .limitExceeded(remainingMonthLimit.compareTo(BigDecimal.ZERO) < 0)
                        .limit(nearestLimit)
                        .build());
    }

    private BigDecimal getRemainingMonthLimit(BigDecimal currentRemainingMonthLimit, BigDecimal transactionSum) {
        return currentRemainingMonthLimit.subtract(transactionSum, new MathContext(2));
    }

    private BigDecimal getNearestDate(Limit nearestLimit, Expense nearstRemaining) {
        return nearstRemaining == null || nearestLimit.getLimitDatetime().before(nearstRemaining.getDatetime())
                ? nearestLimit.getLimitSum()
                : getConvertedLimitToUSD(nearstRemaining);
    }

    private BigDecimal getConvertedLimitToUSD(Expense expense) {
        String currency = expense.getCurrencyShortname();
        return currency == "RUB"
                ? ratesRepository.getConvertedRUBSumByActualUSDRate(expense.getSum(), new Date())
                : currency == "USD"
                        ? ratesRepository.getConvertedRUBSumByActualUSDRate(expense.getSum(), new Date())
                        : expense.getSum();
    }

    private Expense getNearestRemainingLimitByCurrentDate(Date currentDate, String currentCategory) {
        return expenseRepository.findNearestRemainingLimitByCurrentDate(currentDate, currentCategory);
    }

    private Limit getNearestLimitByCurrentDate(Date currentDate, String currentCategory) {
        return Optional.ofNullable(limitRepository.findNearestLimitByCurrentDate(currentDate, currentCategory))
                .orElseThrow();
    }

    public ExchengeRate saveExchengeRate() {
        return ratesRepository.save(
                ExchengeRate.builder()
                        .kztToUsd(new BigDecimal(getExchengeResponse("KZT").getRate()))
                        .rubToUsd(new BigDecimal(getExchengeResponse("RUB").getRate()))
                        .createdDate(new Date())
                        .build());
    }

    public ExchengeResponse getExchengeResponse(String currencyShortname) {
        return webClient.build().get()
                .uri(utils.getUrl(currencyShortname))
                .retrieve()
                .bodyToMono(ExchengeResponse.class)
                .block();
    }
}