package doc.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing  // BOOT에서 JobBuilderFactory, StepBuilderFactory, JobRepository 자동 구성
public class BatchConfig {
}