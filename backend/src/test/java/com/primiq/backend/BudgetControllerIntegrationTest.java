package com.primiq.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.primiq.backend.model.dto.BudgetRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "media.storage.root=target/test-uploads"
})
class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createBudgetWorks() throws Exception {
        BudgetRequestDto request = new BudgetRequestDto();
        request.setName("Marketing Budget");
        request.setAmount(5000.0);
        request.setDescription("Budget für Marketing-Kampagnen");

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Marketing Budget"))
                .andExpect(jsonPath("$.amount").value(5000.0))
                .andExpect(jsonPath("$.description").value("Budget für Marketing-Kampagnen"));
    }

    @Test
    void getAllBudgetsWorks() throws Exception {
        // Erst ein Budget erstellen
        BudgetRequestDto request = new BudgetRequestDto();
        request.setName("Test Budget");
        request.setAmount(1000.0);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Dann alle abrufen
        mockMvc.perform(get("/api/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void getBudgetByIdWorks() throws Exception {
        // Budget erstellen
        BudgetRequestDto request = new BudgetRequestDto();
        request.setName("Specific Budget");
        request.setAmount(2500.0);

        MvcResult createResult = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        // Budget abrufen
        mockMvc.perform(get("/api/budgets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Specific Budget"));
    }

    @Test
    void getBudgetByIdReturns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/budgets/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBudgetWorks() throws Exception {
        // Budget erstellen
        BudgetRequestDto createRequest = new BudgetRequestDto();
        createRequest.setName("Original Budget");
        createRequest.setAmount(1000.0);

        MvcResult createResult = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        // Budget aktualisieren
        BudgetRequestDto updateRequest = new BudgetRequestDto();
        updateRequest.setName("Updated Budget");
        updateRequest.setAmount(2000.0);

        mockMvc.perform(put("/api/budgets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Budget"))
                .andExpect(jsonPath("$.amount").value(2000.0));
    }

    @Test
    void deleteBudgetWorks() throws Exception {
        // Budget erstellen
        BudgetRequestDto request = new BudgetRequestDto();
        request.setName("To Delete Budget");
        request.setAmount(500.0);

        MvcResult createResult = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String id = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        // Budget löschen
        mockMvc.perform(delete("/api/budgets/{id}", id))
                .andExpect(status().isNoContent());

        // Prüfen dass es nicht mehr existiert
        mockMvc.perform(get("/api/budgets/{id}", id))
                .andExpect(status().isNotFound());
    }
}
