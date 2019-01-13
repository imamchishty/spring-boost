package ${package}.api.controller;

import ${package}.api.constant.ApiConstants;
import ${package}.exception.GlobalBusinessCodes;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * <pre>
 *      Controller provides clients with a definitive list of all HTTP and Business codes.
 * </pre>
 */
@RestController
public class HelpController {

    private Map<Object, String> businessCodes = new HashMap<>();
    private Map<Object, String> httpCodes = new HashMap<>();
    private Map<String, Map<Object, String>> codes = new HashMap<>();
    private Map<String, String> adminLinks = new HashMap<>();

    @PostConstruct
    public void setup() {

        for (GlobalBusinessCodes code : GlobalBusinessCodes.values()) {
            businessCodes.put(code.getCode(), code.getDescription());
        }

        codes.put("businessCodes", businessCodes);

        for (HttpStatus httpStatus : HttpStatus.values()) {
            httpCodes.put(httpStatus.value(), httpStatus.getReasonPhrase());
        }

        codes.put("httpCodes", httpCodes);

        // admin
        adminLinks.put("ping", "/api/ping");
        adminLinks.put("api", "/api/docs");
        adminLinks.put("build", "/admin/build");
        adminLinks.put("auditevents", "/admin/auditevents");
        adminLinks.put("beans", "/admin/beans");
        adminLinks.put("caches", "/admin/caches");
        adminLinks.put("conditions", "/admin/conditions");
        adminLinks.put("configprops", "/admin/configprops");
        adminLinks.put("beans", "/admin/beans");
        adminLinks.put("configprops", "/admin/configprops");
        adminLinks.put("env", "/admin/env");
        adminLinks.put("flyway", "/admin/flyway");
        adminLinks.put("health", "/admin/health");
        adminLinks.put("info", "/admin/info");
        adminLinks.put("loggers", "/admin/loggers");
        adminLinks.put("metrics", "/admin/metrics");
        adminLinks.put("mappings", "/admin/mappings");
        adminLinks.put("scheduledtasks", "/admin/scheduledtasks");
        adminLinks.put("sessions", "/admin/sessions");
        adminLinks.put("threaddump", "/admin/threaddump");
        adminLinks.put("heapdump", "/admin/heapdump");
        adminLinks.put("jolokia", "/admin/jolokia");
        adminLinks.put("logfile", "/admin/logfile");
        adminLinks.put("prometheus", "/admin/prometheus");
    }

    @RequestMapping(path = ApiConstants.API_HELP, method = RequestMethod.GET)
    public ResponseEntity help() {
        return new ResponseEntity(codes, HttpStatus.OK);
    }

    @RequestMapping(path = ApiConstants.API_MAN_HELP, method = RequestMethod.GET)
    public ResponseEntity adminHelp() {
        return new ResponseEntity(adminLinks, HttpStatus.OK);
    }
}
