package springboot.pac4j.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ErrorsController {

    @RequestMapping("/denied")
    String denied() {
        return "/errors/403"
    }

    @RequestMapping("/notFound")
    String notFound() {
        return "/errors/404"
    }

}