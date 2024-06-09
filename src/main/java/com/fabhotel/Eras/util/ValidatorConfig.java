package com.fabhotel.Eras.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ValidatorConfig {
	@Bean
    public PostEndorsementRequestValidator postEndorsementRequestValidator() {
        return new PostEndorsementRequestValidator();
    }
}
