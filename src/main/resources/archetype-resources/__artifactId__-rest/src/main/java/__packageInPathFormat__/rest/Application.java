package ${package}.rest;

import ${package}.rest.constant.ApiConstants;
import com.shedhack.exception.controller.spring.config.EnableExceptionController;
import com.shedhack.spring.actuator.config.EnableActuatorsAndInterceptors;
import com.shedhack.spring.actuator.interceptor.TraceRequestHandler;
import com.shedhack.thread.context.config.EnableThreadContextAspect;
import com.shedhack.trace.request.api.service.TraceRequestService;
import com.shedhack.trace.request.filter.DefaultLoggingHandler;
import com.shedhack.trace.request.filter.RequestTraceFilter;
import com.shedhack.trace.request.jpa.config.EnableTraceRequestJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.Arrays;

@SpringBootApplication
@EnableTraceRequestJpa
@EnableExceptionController
@EnableThreadContextAspect
@EnableActuatorsAndInterceptors
@PropertySources(value = {
        @PropertySource(value = "classpath:/git-build.properties")
})
// Uncomment when spring-cloud is available, see application.yml and bootstrap.yml
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class Application {

    // --------------------
    // Bean configurations:
    // --------------------

    @Value("${spring.application.name}")
    private String appName;

    /**
     * Service is configured via {@link EnableTraceRequestJpa}
     */
    @Autowired
    private TraceRequestService jpaTraceRequestService;

    /**
     * Service is configured via @EnableActuatorsAndInterceptors
     */
    @Autowired
    private TraceRequestHandler traceRequestHandler;

    /**
     * Filter records and logs all HTTP requests.
     * This requires an implementation of {@link TraceRequestService}.
     */
    @Bean
    public FilterRegistrationBean requestIdFilterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new RequestTraceFilter(appName, jpaTraceRequestService,
                Arrays.asList(new DefaultLoggingHandler(), traceRequestHandler)));
        filter.addUrlPatterns(ApiConstants.API_ROOT + "/*");

        return filter;
    }

    /**
     * Future datasource beans need to marked with {@link org.springframework.context.annotation.Primary}.
     * to prevent conflicts with the DS used by EnableTraceRequestJpa.
     */

    // ---------------------------
    // Main method to start Spring
    // ---------------------------
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
