package org.jetbrains.controller;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.service.BuildTriggerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BuildTriggerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuildTriggerService service;

    @InjectMocks
    private BuildTriggerController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();

    }

    @Test
    public void testTriggerBuild() throws Exception {
        String repositoryUrl = "https://github.com/sample/repo.git";
        String branch = "main";
        boolean isScheduled = true;

        String payload = String.format(
                "{\"repositoryUrl\":\"%s\",\"branch\":\"%s\",\"scheduled\":%s}",
                repositoryUrl, branch, isScheduled
        );

        mockMvc.perform(post("/api/builds/trigger-build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isAccepted());

        verify(service).handleBuildRequest(new BuildDto(repositoryUrl, branch, isScheduled));

    }
}
