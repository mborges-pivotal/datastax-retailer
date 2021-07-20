package com.borgescloud.datastax.retailer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HackathonWebController extends BaseController {

  @GetMapping("/hackathon")
  public String hackathon(Model model) {
    log.info("Hackathon page");
    model.addAttribute("pageName", "Hackathon");
    return "hackathon";
  }

}
