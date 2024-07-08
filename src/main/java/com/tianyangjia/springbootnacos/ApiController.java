package com.tianyangjia.springbootnacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private NacosServiceManager nacosServiceManager;
    private String getServiceUrl(String serviceName) throws Exception {
        List<Instance> instances = nacosServiceManager.getNamingService().selectInstances(serviceName, true);
        if (instances != null && !instances.isEmpty()) {
            Instance instance = instances.get(0); // 获取第一个实例
            return "http://" + instance.getIp() + ":" + instance.getPort();
        }
        throw new Exception("No available instances for service: " + serviceName);
    }

    @PostMapping(value = "/post-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> postJson(@RequestBody String jsonRequestBody) throws Exception {
        String serviceUrl = getServiceUrl("apijson") + "/get";

        return webClientBuilder.build()
                .post()
                .uri(serviceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequestBody))
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/hello")
    public Mono<String> getHelloWorld() throws Exception {
        String serviceUrl = getServiceUrl("python-nacos") + "/hello";

        return webClientBuilder.build()
                .get()
                .uri(serviceUrl)
                .retrieve()
                .bodyToMono(String.class);
    }
}
