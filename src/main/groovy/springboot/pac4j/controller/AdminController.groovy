package springboot.pac4j.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AdminController {

    @RequestMapping("/admin")
    @PreAuthorize('hasRole("ROLE_ADMIN")')
    String admin() {
        return "admin/index"
    }

}

