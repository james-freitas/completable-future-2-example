package com.gsuitesafe.gmailbackup.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("Should list all initiated backups")
    void shouldListAllInitiatedBackups() throws Exception {

        mockMvc.perform(get("/backups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].backupId").isNotEmpty())
                .andExpect(jsonPath("$[0].date").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
    }

    @Test
    @DisplayName("Should recover a content of a specific backup in a zip file")
    void shouldRecoverZipFileBackupById() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(get("/exports/backupId").accept("application/zip"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        String headerValue = mvcResult.getResponse().getHeader("Content-Disposition");
        assertThat(headerValue).isEqualTo("attachment; filename=\"test.zip\"");
    }
}
