package transaction.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import transaction.test.entity.Expense;
import transaction.test.service.TransactionService;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void setExpense() throws Exception {
        Expense testExpense = Expense.builder()
                .accountFrom(1000000000)
                .accountTo(1000000000)
                .currencyShortname("USD")
                .sum(BigDecimal.valueOf(500))
                .expenseCategory("product")
                .build();

        String testExpenseJson = mapper.writeValueAsString(testExpense);
        mockMvc.perform(
                post("/transactions/expense/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testExpenseJson))
                .andExpect(status().isOk());
    }

    @Test
    void setExchengeRate() throws Exception {
        mockMvc.perform(
                post("/transactions/exchenge-rate"))
                .andExpect(status().isOk());
    }
}