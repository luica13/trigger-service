package org.jetbrains.controller;

import jakarta.validation.Valid;
import org.jetbrains.dto.BuildDto;
import org.jetbrains.model.BuildRequest;
import org.jetbrains.service.BuildTriggerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/builds")
@Validated
public class BuildTriggerController {

  private final BuildTriggerService service;

  public BuildTriggerController(BuildTriggerService service) {
    this.service = service;
  }

  @PostMapping(value = "/trigger-build",  consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> triggerBuild(@Valid @RequestBody BuildRequest request) {
    service.handleBuildRequest( new BuildDto(request.getRepositoryUrl(), request.getBranch(), request.isScheduled()));
    return ResponseEntity.accepted().build();
  }

}
