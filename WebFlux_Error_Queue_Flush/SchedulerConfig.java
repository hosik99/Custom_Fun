package com.icetea.monstu_back;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    // 이 클래스에서는 설정만 활성화하고, 실제 스케줄링 작업은 별도의 클래스에서 @Scheduled로 구현
}

