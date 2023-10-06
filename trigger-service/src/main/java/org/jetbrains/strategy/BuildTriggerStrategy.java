package org.jetbrains.strategy;

import org.jetbrains.dto.BuildDto;

public interface BuildTriggerStrategy {
  boolean shouldTrigger(BuildDto request);

  void triggerBuild(BuildDto requestDto);

}
