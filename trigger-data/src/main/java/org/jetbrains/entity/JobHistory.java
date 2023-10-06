package org.jetbrains.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "job_history")
public class JobHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "repository_url")
  private String repositoryUrl;
  @Column(name = "branch")
  private String branch;
  @Column(name = "start_time")
  private LocalDateTime startTime;
  @Column(name = "end_time")
  private LocalDateTime endTime;
  @Column(name = "build_status")
  private String status;  // "SUCCESS", "FAILURE"
  @Column(name = "triggered_by")
  private String triggeredBy; //"COMMIT", "SCHEDULE"
  @Column(name = "logs")
  private String logDetails;

}
