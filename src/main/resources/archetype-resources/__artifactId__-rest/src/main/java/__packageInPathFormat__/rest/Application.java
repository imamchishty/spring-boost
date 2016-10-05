package ${package}.rest;

import ${package}.rest.constant.ApiConstants;
import com.google.gson.Gson;
import com.shedhack.exception.controller.spring.ExceptionInterceptor;
import com.shedhack.exception.controller.spring.config.EnableExceptionController;
import com.shedhack.spring.actuator.config.EnableActuatorsAndInterceptors;
import com.shedhack.spring.actuator.interceptor.ActuatorExceptionInterceptor;
import com.shedhack.spring.actuator.interceptor.ActuatorTraceRequestInterceptor;
import com.shedhack.thread.context.config.EnableThreadContextAspect;
import com.shedhack.trace.request.filter.DefaultTraceRequestInterceptor;
import com.shedhack.trace.request.filter.RequestTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
@EnableExceptionController
@EnableThreadContextAspect
@EnableActuatorsAndInterceptors
@PropertySources(value = {
        @PropertySource(value = "classpath:/git-build.properties")
})
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
public class Application extends WebMvcConfigurerAdapter {

    // --------------------
    // Bean configurations:
    // --------------------

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ActuatorTraceRequestInterceptor actuatorTraceRequestInterceptor;

    @Autowired
    private ActuatorExceptionInterceptor actuatorExceptionInterceptor;

    @Bean
    public Gson gson() {
        return new Gson();
    }

    /**
     * Filter records and logs all HTTP requests.
     * This requires implementation(s) of TraceRequestInterceptors.
     */
    @Bean
    public FilterRegistrationBean requestIdFilterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new RequestTraceFilter(appName,
                Arrays.asList(new DefaultTraceRequestInterceptor(gson()), actuatorTraceRequestInterceptor)));
        filter.addUrlPatterns(ApiConstants.API_ROOT + "/*");

        return filter;
    }

    @Bean
    public List<ExceptionInterceptor> exceptionInterceptors() {
        return Arrays.asList(actuatorExceptionInterceptor);
    }

    @Bean
    public Sampler defaultSampler() {
        return new AlwaysSampler();
    }

    // ---------------------------------------
    // Swagger setup for the API documentation
    // ---------------------------------------

    // If you add spring security then you can easily secure these resources.

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(ApiConstants.API_DOCS_ROOT + "/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController(ApiConstants.API_DOCS_ROOT + "/configuration/ui", "/configuration/ui");
        registry.addRedirectViewController(ApiConstants.API_DOCS_ROOT +"/configuration/security", "/configuration/security");
        registry.addRedirectViewController(ApiConstants.API_DOCS_ROOT +"/swagger-resources", "/swagger-resources");
        registry.addRedirectViewController(ApiConstants.API_DOCS_ROOT, "/swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/" + ApiConstants.API_DOCS_ROOT  + "/**")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kungfu.panda.rest.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documentation")
                .description("API Documentation")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
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
