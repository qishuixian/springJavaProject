//Spring Boot 里最核心的三个要素：
//@RestController→ 标记类是控制器
//@GetMapping("/hello")→ 绑定网址和方法
//public String sayHello()→ 定义处理请求的方法

package com.example.backend1.controller;
// 告诉 Spring：“当用户在浏览器输入 /hello这个地址时，就运行这个方法。”
import org.springframework.web.bind.annotation.GetMapping;
// 它盖在类上面，告诉 Spring：“这个类是一个控制器，专门处理网页请求，并且返回的数据直接写给浏览器看（不需要经过 HTML 模板）。”
import org.springframework.web.bind.annotation.RestController;

// @RestController贴在一个类上，表示：这个类是一个“控制器”，专门处理浏览器发来的 HTTP 请求，并把结果直接返回给浏览器（不经过任何页面模板）。
// 一个方法对应一个 URL，方法名不能重复
@RestController
public class HelloController {

    @GetMapping("/hello")
    //  在同一个 Java 类里，不能有两个完全一样的方法声明。
    public String sayHello1() {
        return "你好，世界！这是我的第一个 Spring Boot 接口。";
    }
    // 定义多个接口
    @GetMapping("/hi")
    public String sayHi() {
        return "嗨，大家好";
    }

    @GetMapping("/bye")
    public String sayBye() {
        return "再见！";
    }
    // Java 允许方法名相同但参数不同，这叫方法重载（Overloading）：
    // 带参数的同名方法（可以存在）
    @GetMapping("/hello2")
    public String sayHello(String name) {
        return "你好，" + name + "！";
    }
}