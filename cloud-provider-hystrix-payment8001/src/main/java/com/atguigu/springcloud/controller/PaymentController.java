package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    public String serverPort;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_OK(id);
        log.info("*********result：" + result);
        return result;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    // 使用服务降级，运行超时会执行如下paymentInfo_Timeout_fallback方法，限定时间为2500
//    @HystrixCommand(fallbackMethod = "paymentInfo_Timeout_fallback", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2500")
//    })
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        String resultTimeout = paymentService.paymentInfo_Timeout(id);
        log.info("*********resultTimeout：" + resultTimeout);
        return resultTimeout;
    }

    public String paymentInfo_Timeout_fallback(Integer id) {
        return "resultTimeout：运行超时，请稍后重试";
    }

    // ==========服务熔断
    @GetMapping("/payment/hystrix/{id}")
    public String paymentHystrix(@PathVariable("id") Integer id) {
        return paymentService.paymentCircuitBreaker(id);
    }

}
