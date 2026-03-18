
package com.simuladorapis.infrastructureadaptadores.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @RequestMapping(value = {
        "/{path:^(?!api)(?!assets)(?!h2-console)[^\\.]+}",
        "/{path:^(?!api)(?!assets)(?!h2-console)[^\\.]+}/**"
    })
  public String forward() {
        return "forward:/index.html";
    }
}
