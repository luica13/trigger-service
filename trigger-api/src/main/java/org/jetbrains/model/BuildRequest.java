package org.jetbrains.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BuildRequest {
  @NotBlank(message = "Repository URL cannot be empty")
  @Pattern(regexp = "https?://github\\.com/[\\w.-]+/[\\w.-]+", message = "Invalid GitHub repository URL")
  private String repositoryUrl;

  @NotBlank(message = "Branch cannot be empty")
  private String branch;

  private boolean scheduled;

  @JsonCreator
  public BuildRequest(
          @JsonProperty("repositoryUrl") String repositoryUrl,
          @JsonProperty("branch") String branch,
          @JsonProperty("scheduled") boolean scheduled) {
    this.repositoryUrl = repositoryUrl;
    this.branch = branch;
    this.scheduled = scheduled;
  }


  @Override
  public String toString() {
    return "BuildRequest{" +
        "repositoryUrl='" + repositoryUrl + '\'' +
        ", branch='" + branch + '\'' +
        '}';
  }

}
