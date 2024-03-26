package transaction.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import transaction.test.entity.Expense;
import transaction.test.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping(value = "transactions")
@Tag(name = "Контроллер для приема транзакций", description = "Условно интеграция с банковскими сервисами")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("expense/add")
    @Operation(summary = "Установить расход", description = "Получение информации о каждой расходной операции в тенге (KZT), рублях (RUB) и других валютах в реальном времени")
    public ResponseEntity<?> setExpense(@Parameter(description = "Сущность расход") @RequestBody Expense transaction) {
        return ResponseEntity.ok(transactionService.saveExpense(transaction));
    }

    @PostMapping("exchenge-rate")
    @Scheduled(cron = "@daily")
    @Operation(summary = "Установить обменный курс", description = "Запрашивает данные биржевых курсов валютных пар KZT/USD, RUB/USD по дневному интервалу (1day/daily), можно установить самостаятельно")
    public ResponseEntity<?> setExchengeRate() {
        return ResponseEntity.ok(transactionService.saveExchengeRate());
    }
}