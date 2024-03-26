package transaction.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import transaction.test.entity.Limit;
import transaction.test.service.ClientService;

@Controller
@RequestMapping(value = "client")
@Tag(name = "Клиентский контроллер", description = "Для внешних запросов от клиента: получение списка транзакций, превысивших лимит, установление нового лимита, получение всех лимитов")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("limit/add")
    @Scheduled(cron = "@monthly")
    @Operation(summary = "Установить лимит расходов", description = "Установить месячный лимит по расходам в долларах США (USD) раздельно для двух категорий расходов: товаров и услуг")
    public ResponseEntity<?> setLimit(@Parameter(description = "Сущность лимит") @RequestBody Limit limit) {
        return ResponseEntity.ok(clientService.saveLimit(limit));
    }

    @GetMapping("transactions")
    @Operation(summary = "Получить список всех транзакций", description = "Возвращает полный список всех транзакций")
    public ResponseEntity<?> getAllTransactions() {
        return ResponseEntity.ok(clientService.getAllTransactions());
    }

    @GetMapping("transactions/exceed-limit")
    @Operation(summary = "Получить список всех транзакций, превысивших лимит", description = "Возвращает полный список всех транзакций,  превысивших лимит, с указанием лимита, который был превышен")
    public ResponseEntity<?> getTransactionsExceededLimit() {
        return ResponseEntity.ok(clientService.getTransactionsExceededLimit());
    }
}