package com.pos.wifi_connection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.pos.wifi_connection.model.Plan;
import com.pos.wifi_connection.service.PlanService;

@Controller
public class PlanController {
    

    @Autowired
    PlanService planService;


    @PostMapping("/settings/plans/save")
    public String addPlan(Plan plan) {
        planService.savePlan(plan);
        return "redirect:/settings";
    }

}
