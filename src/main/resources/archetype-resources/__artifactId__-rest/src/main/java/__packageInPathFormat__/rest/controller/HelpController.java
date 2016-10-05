package ${package}.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import ${package}.exception.GlobalBusinessCodes;
import ${package}.rest.constant.ApiConstants;

/**
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

    @Autowired
    private ServerProperties serverProperties;

    @PostConstruct
    public void setup(){

        for(GlobalBusinessCodes code : GlobalBusinessCodes.values()) {
            businessCodes.put(code.getCode(), code.getDescription());
        }

        codes.put("businessCodes", businessCodes);


        for(HttpStatus httpStatus : HttpStatus.values()) {
            httpCodes.put(httpStatus.value(), httpStatus.getReasonPhrase());
        }

        codes.put("httpCodes", httpCodes);


        String baseUrl = getBase();

        // admin
        adminLinks.put("ping", baseUrl + "/api/ping");
        adminLinks.put("api", baseUrl + "/api/docs");
        adminLinks.put("build", baseUrl + "/admin/build");
        adminLinks.put("exceptions", baseUrl + "/admin/exceptions");
        adminLinks.put("requests", baseUrl + "/admin/requests");
        adminLinks.put("health", baseUrl + "/admin/health");
        adminLinks.put("autoconfig", baseUrl + "/admin/autoconfig");
        adminLinks.put("dump", baseUrl + "/admin/dump");
        adminLinks.put("beans", baseUrl + "/admin/beans");
        adminLinks.put("configprops", baseUrl + "/admin/configprops");
        adminLinks.put("env", baseUrl + "/admin/env");
        adminLinks.put("info", baseUrl + "/admin/info");
        adminLinks.put("mappings", baseUrl + "/admin/mappings");
        adminLinks.put("metrics", baseUrl + "/admin/metrics");
        adminLinks.put("trace", baseUrl + "/admin/trace");
        adminLinks.put("docs", baseUrl + "/admin/docs");

    }

    private String getBase() {
        String host = "localhost";
        int port = serverProperties.getPort();

        try {
            InetAddress ip = InetAddress.getLocalHost();
            host = ip.getHostAddress();
        } catch (UnknownHostException e) {
        }

        return "http://" + host + ":" + port;
    }

    @RequestMapping(path = ApiConstants.API_HELP, method = RequestMethod.GET)
    public ResponseEntity help(){
        return new ResponseEntity(codes, HttpStatus.OK);
    }

    @RequestMapping(path = ApiConstants.API_MAN_HELP, method = RequestMethod.GET)
    public ResponseEntity adminHelp(){
        return new ResponseEntity(adminLinks, HttpStatus.OK);
    }
}