package transaction.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import transaction.test.entity.Limit;
import transaction.test.service.ClientService;

@WebMvcTest(ClientController.class)
class ClientControllerTests {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void setLimit() throws Exception {
        Limit testLimit = Limit.builder()
                .limitSum(BigDecimal.valueOf(2500))
                .limitCategory("product")
                .build();

        String testLimitJson = mapper.writeValueAsString(testLimit);
        mockMvc.perform(
                post("/client/limit/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testLimitJson))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTransactions() throws Exception {
        mockMvc.perform(
                get("/client/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    void getTransactionsExceededLimit() throws Exception {
        mockMvc.perform(get("/client/transactions/exceed-limit"))
                .andExpect(status().isOk());
    }
}