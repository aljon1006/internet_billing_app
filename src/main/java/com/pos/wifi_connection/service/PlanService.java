package com.pos.wifi_connection.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Plan;

@Service
public interface PlanService {
    List<Plan> getPlans();
    Plan getPlanById(Integer id);
    public void savePlan(Plan plan);
}
