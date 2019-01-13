package ${package}.api.controller;

import ${package}.api.constant.ApiConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Value("${spring.application.name}")
    private String appName;

    /** Used to provide tools a HTTP 200 OK when service is running. */
    @RequestMapping(path = ApiConstants.API_PING, method = RequestMethod.GET)
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>(appName + " is running.....", HttpStatus.OK);
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
