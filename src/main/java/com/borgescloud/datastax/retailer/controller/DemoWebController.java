package com.borgescloud.datastax.retailer.controller;

import com.borgescloud.datastax.retailer.model.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DemoWebController {

    @Autowired
    private DemoRestController service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("stock", new Stock());
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
