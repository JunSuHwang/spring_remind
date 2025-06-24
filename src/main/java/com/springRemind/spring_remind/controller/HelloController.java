package com.springRemind.spring_remind.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @GetMapping(value = "/helloworld/string")
    @ResponseBody
    public String helloworldString() {
        return "helloworld and jenkins 222";
    }

    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }

    // @Controller -> RestController 사용시 에러 해결
    @GetMapping(value = "/helloworld/page")
    public ModelAndView helloworld() {
        String message = "freemarker의 메시지";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("helloworld");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/helloworld/long-process")
    @ResponseBody
    public String pause() throws InterruptedException {
        Thread.sleep(10000);
        return "Process finised";
    }

    @Setter
    @Getter
    public static class Hello{
        private String message;
    }

}
