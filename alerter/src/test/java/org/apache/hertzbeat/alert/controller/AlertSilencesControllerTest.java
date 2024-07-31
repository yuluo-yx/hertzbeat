package org.apache.hertzbeat.alert.controller;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hertzbeat.alert.service.AlertSilenceService;
import org.apache.hertzbeat.common.constants.CommonConstants;
import org.apache.hertzbeat.common.entity.alerter.AlertSilence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * test case for {@link AlertSilencesController}
 */

@ExtendWith(MockitoExtension.class)
class AlertSilencesControllerTest {

	private MockMvc mockMvc;

	@Mock
	private AlertSilenceService alertSilenceService;

	private AlertSilence alertSilence;

	@InjectMocks
	private AlertSilencesController alertSilencesController;

	@BeforeEach
	void setUp() {

		this.mockMvc = standaloneSetup(alertSilencesController).build();

		alertSilence = AlertSilence
				.builder()
				.id(1L)
				.type((byte) 1)
				.name("Test Silence")
				.build();
	}

	@Test
	void testGetAlertSilences() throws Exception {

		Page<AlertSilence> alertSilencePage = new PageImpl<>(Collections.singletonList(alertSilence));
		when(alertSilenceService.getAlertSilences(
				any(Specification.class),
				any(PageRequest.class))
		).thenReturn(alertSilencePage);

		mockMvc.perform(get("/api/alert/silences")
						.param("ids", "1")
						.param("search", "Test")
						.param("sort", "id")
						.param("order", "desc")
						.param("pageIndex", "0")
						.param("pageSize", "8")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value((int) CommonConstants.SUCCESS_CODE))
				.andExpect(jsonPath("$.data.content[0].id").value(1))
				.andExpect(jsonPath("$.data.content[0].name").value("Test Silence"));
	}

	@Test
	void testDeleteAlertDefines() throws Exception {

		doNothing().when(alertSilenceService).deleteAlertSilences(any());

		mockMvc.perform(delete("/api/alert/silences")
						.param("ids", "1,2,3")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value((int) CommonConstants.SUCCESS_CODE));
	}

}
