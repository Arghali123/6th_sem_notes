package com.example.JspLearning;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/student-form")
    public String studentForm() {
        return "student-form"; // Resolves to /WEB-INF/jsp/student-form.jsp
    }

    @PostMapping("/student-result")
    public String studentResult() {
        return "student-result"; // Resolves to /WEB-INF/jsp/student-result.jsp
    }
}