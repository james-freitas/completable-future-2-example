package com.gsuitesafe.gmailbackup.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GmailBackupController.class)
@AutoConfigureMockMvc
@DisplayName("Gmail backup controller tests")
public class GmailBackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should start an asynchronous backup and return the backup id")
    void shouldCreateBackupAndGetBackupId() throws Exception {

        mockMvc.perform(post("/backups")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.backupId").isNotEmpty());
    }
}
