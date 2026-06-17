package com.pos.wifi_connection.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Plan;
import com.pos.wifi_connection.repository.PlanRepository;
import com.pos.wifi_connection.service.PlanService;

import jakarta.transaction.Transactional;

@Service
public class PlanServiceImpl implements PlanService{

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<Plan> getPlans() {
       return planRepository.findAll();
        
    }

    @Override
    public Plan getPlanById(Integer id) {
        return planRepository.findById(id).orElse(null);
    }

    @Override
    public void savePlan(Plan plan) {
        planRepository.save(plan);
    }
    
}
