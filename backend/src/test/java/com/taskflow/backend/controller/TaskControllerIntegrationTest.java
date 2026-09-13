package com.taskflow.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.backend.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndFetchTask_endToEnd() throws Exception {
        Task task = new Task("Buy milk", "2% please", false);

        // Create
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Buy milk")))
                .andReturn().getResponse().getContentAsString();

        Task created = objectMapper.readValue(response, Task.class);

        // Fetch
        mockMvc.perform(get("/api/tasks/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Buy milk")));
    }

    @Test
    void createTask_withBlankTitle_returnsBadRequest() throws Exception {
        Task invalid = new Task("", "no title", false);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTaskById_whenMissing_returns404() throws Exception {
        mockMvc.perform(get("/api/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_removesIt() throws Exception {
        Task task = new Task("Temp task", "delete me", false);
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();
        Task created = objectMapper.readValue(response, Task.class);

        mockMvc.perform(delete("/api/tasks/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
