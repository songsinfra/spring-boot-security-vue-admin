package com.nexgrid.cgsg.admin.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AliveLogSchedule {

    @Scheduled(fixedRateString = "${cgsg.aliveLog.milliseconds}")
    public void executeLog() {
      log.info("Keep-Alive!");
    }
}
