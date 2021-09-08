package com.gsuitesafe.backup.controller;

import com.gsuitesafe.backup.domain.BackupStatus;
import com.gsuitesafe.backup.dto.CreatedBackupResponse;
import com.gsuitesafe.backup.dto.InitiatedBackupResponse;
import com.gsuitesafe.backup.exception.BackupNotFoundException;
import com.gsuitesafe.backup.service.BackupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BackupController.class)
@AutoConfigureMockMvc
@DisplayName("Gmail backup controller tests")
public class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BackupService service;

    @Test
    @DisplayName("Should start an asynchronous backup and return the backup id")
    void shouldCreateBackupAndGetBackupId() throws Exception {

        final CreatedBackupResponse createdBackupResponse = new CreatedBackupResponse(UUID.randomUUID());

        given(service.createBackup()).willReturn(createdBackupResponse);

        mockMvc.perform(post("/backups")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.backupId").isNotEmpty());

        verify(service, times(1)).createBackup();
    }

    @Test
    @DisplayName("Should list all initiated backups")
    void shouldListAllInitiatedBackups() throws Exception {

        final InitiatedBackupResponse response = new InitiatedBackupResponse(
                UUID.randomUUID(), LocalDate.now(), BackupStatus.IN_PROGRESS);
        List<InitiatedBackupResponse> list = Arrays.asList(response);

        given(service.getInitiatedBackupList()).willReturn(list);

        mockMvc.perform(get("/backups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].backupId").isNotEmpty())
                .andExpect(jsonPath("$[0].date").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());

        verify(service, times(1)).getInitiatedBackupList();
    }

    @Test
    @DisplayName("Should recover a content of a specific backup of messages in a zip file")
    void shouldRecoverZipFileBackupById() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(get("/exports/backupId").accept("application/zip"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();

        String headerValue = mvcResult.getResponse().getHeader("Content-Disposition");
        assertThat(headerValue).isEqualTo("attachment; filename=\"backupId.zip\"");
    }

    @Test
    @DisplayName("Should fail to return a content of a specific backup when backup id is not found")
    void shouldFailToRecoverZipFileBackupByNonExistentId() throws Exception {

        given(service.getMessagesBy("backupId")).willThrow(BackupNotFoundException.class);

        mockMvc.perform(get("/exports/backupId"))
                .andExpect(status().isNotFound())
                .andDo(print()).andReturn();

        verify(service, times(1)).getMessagesBy("backupId");
    }

    @Test
    @DisplayName("Should recover a content of a specific backup of messages grouped by label in a zip file ")
    void shouldRecoverZipFileBackupByIdAndLabel() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(get("/exports/backupId/label").accept("application/zip"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        String headerValue = mvcResult.getResponse().getHeader("Content-Disposition");
        assertThat(headerValue).isEqualTo("attachment; filename=\"backupId-label.zip\"");
    }
}
