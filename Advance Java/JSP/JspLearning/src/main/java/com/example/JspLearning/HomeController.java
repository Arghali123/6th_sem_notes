package com.example.JspLearning;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Resolves to /WEB-INF/jsp/index.jsp
    }

    @GetMapping("/student")
    public String student() {
        return "student"; // Resolves to /WEB-INF/jsp/student.jsp
    }
}