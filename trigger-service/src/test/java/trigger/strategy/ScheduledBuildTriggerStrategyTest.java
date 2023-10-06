package trigger.strategy;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.entity.JobHistory;
import org.jetbrains.repository.JobHistoryRepository;
import org.jetbrains.strategy.ScheduledBuildTriggerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ScheduledBuildTriggerStrategyTest {

  @Mock
  private JobHistoryRepository jobHistoryRepository;

  @InjectMocks
  private ScheduledBuildTriggerStrategy strategy;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testShouldTrigger() {
    BuildDto scheduledRequest = new BuildDto();
    scheduledRequest.setScheduled(true);
    assertTrue(strategy.shouldTrigger(scheduledRequest));

    BuildDto notScheduledRequest = new BuildDto();
    notScheduledRequest.setScheduled(false);
    assertFalse(strategy.shouldTrigger(notScheduledRequest));
  }

  @Test
  public void testTriggerBuild() {
    BuildDto requestDto = new BuildDto();
    requestDto.setRepositoryUrl("https://github.com/test/repo.git");
    requestDto.setBranch("master");
    requestDto.setScheduled(true);

    strategy.triggerBuild(requestDto);

    assertTrue(strategy.getScheduledBuildRequests().contains(requestDto));
  }

  @Test
  public void testScheduledBuildTask() {
    BuildDto requestDto = new BuildDto();
    requestDto.setRepositoryUrl("https://github.com/test/repo.git");
    requestDto.setBranch("master");
    requestDto.setScheduled(true);

    strategy.triggerBuild(requestDto);
    strategy.scheduledBuildTask();

    verify(jobHistoryRepository).save(any(JobHistory.class));
  }
}