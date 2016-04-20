package ${package}.rest.feign;

import ${package}.rest.feign.interceptor.TraceRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *     Configuration for Feign Clients.
 * </pre>
 */
@Configuration
public class FeignConfiguration {

    @Bean
    public TraceRequestInterceptor traceRequestInterceptor() {
        return new TraceRequestInterceptor();
    }
}
