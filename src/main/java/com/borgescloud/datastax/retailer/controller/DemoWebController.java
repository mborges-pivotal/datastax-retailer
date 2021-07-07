package com.borgescloud.datastax.retailer.controller;

import javax.servlet.http.HttpServletRequest;

import com.borgescloud.datastax.retailer.model.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class DemoWebController extends BaseController {

  @Autowired
  private DemoRestController service;

  ////////////////////////////////////
  // Authentication
  ////////////////////////////////////

  // Login form
  @RequestMapping("login.html")
  public String login() {
    return "admin/login";
  }

  // Login form with error
  @RequestMapping("login-error.html")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "admin/login";
  }

  @GetMapping("logout.html")
  public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    return "redirect:/";
  }

  ////////////////////////////////////
  // Applications
  ////////////////////////////////////

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("stock", new Stock());
    model.addAttribute("products", service.listProducts());
    return "index";
  }

  @PostMapping("/stock")
  public String updateStock(@ModelAttribute Stock stock, Model model) throws JsonProcessingException {
    model.addAttribute("stock", stock);
    service.updateStoreStock(stock);
    log.info("stock updated");
    return "result";
  }

}
