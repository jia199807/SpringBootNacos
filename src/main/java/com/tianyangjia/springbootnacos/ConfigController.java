// Refer to document:  https://github.com/nacos-group/nacos-examples/tree/master/nacos-spring-cloud-example/nacos-spring-cloud-config-example
package com.tianyangjia.springbootnacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${test:qqq}")
    private String test;

    @Value("${hello:qqq}")
    private String hello;

    @Value("${env:qqq}")
    private String env;

    @RequestMapping("/get")
    public Map<String, String> get() {
        Map<String, String> config = new HashMap<>();
        config.put("test", test);
        config.put("hello", hello);
        config.put("env", env);
        // 添加更多配置项到 Map 中
        return config;
    }
}
