package nil.ed.onlineexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {
    @Bean
    public Executor commonExecutor(){
        return new ThreadPoolExecutor(5, 20, 2, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50));
    }
}
