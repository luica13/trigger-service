package trigger.strategy;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.entity.JobHistory;
import org.jetbrains.queue.BuildRequestProducer;
import org.jetbrains.repository.JobHistoryRepository;
import org.jetbrains.strategy.CommitBuildTriggerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class CommitBuildTriggerStrategyTest {

  @Mock
  private JobHistoryRepository jobHistoryRepository;

  @Mock
  private BuildRequestProducer buildRequestProducer;

  @InjectMocks
  private CommitBuildTriggerStrategy strategy;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    strategy.setClock(fixedClock);
  }

  @Test
  public void testShouldTrigger() {
    BuildDto scheduledRequest = new BuildDto();
    scheduledRequest.setScheduled(true);
    assertFalse(strategy.shouldTrigger(scheduledRequest));

    BuildDto notScheduledRequest = new BuildDto();
    notScheduledRequest.setScheduled(false);
    assertTrue(strategy.shouldTrigger(notScheduledRequest));
  }

}