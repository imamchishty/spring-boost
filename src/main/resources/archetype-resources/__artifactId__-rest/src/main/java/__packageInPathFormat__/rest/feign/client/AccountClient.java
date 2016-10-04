package ${package}.rest.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "${account.service.name}")
public interface AccountClient {

    @RequestMapping(value = "/api/accounts/{id}/balance", method = RequestMethod.GET)
    ResponseEntity<Integer> getBalance(@PathVariable("id") long id);
}
