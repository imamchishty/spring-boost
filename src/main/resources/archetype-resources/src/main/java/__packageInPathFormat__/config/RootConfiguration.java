package ${package}.config;

import ${package}.api.constant.ApiConstants;
import com.google.gson.Gson;
import com.shedhack.exception.controller.spring.config.EnableExceptionController;
import com.shedhack.trace.request.filter.DefaultTraceRequestInterceptor;
import com.shedhack.trace.request.filter.RequestTraceFilter;
import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableExceptionController
public class RootConfiguration {

  // ---------------------------------------
  // Exception Controller : Bean definitions
  // ---------------------------------------

  @Bean
  public Gson gson() {
    return new Gson();
  }

  /**
   * Filter records and logs all HTTP requests. This requires implementation(s) of
   * TraceRequestInterceptors, suports the Exception Controller (fills required details such as
   * span, trace etc).
   */
  @Bean
  public FilterRegistrationBean requestIdFilterRegistrationBean() {
    FilterRegistrationBean filter = new FilterRegistrationBean();
    filter.setFilter(
        new RequestTraceFilter("", Arrays.asList(new DefaultTraceRequestInterceptor(gson()))));
    filter.addUrlPatterns(ApiConstants.API_ROOT + "/*");

    return filter;
  }

  // ---------------------------------------
  // Swagger : Bean definitions
  // ---------------------------------------

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("${package}.api.controller"))
                .paths(PathSelectors.regex(ApiConstants.API_ROOT + "/.*"))
                .build().apiInfo(apiEndPointsInfo());
    }
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Employee Management REST API")
                .contact(new Contact("NAME", "URL", "EMAIL"))
                .license("LICENSE")
                .licenseUrl("LICENSE")
                .version("1.0.0")
                .build();
    }
}
