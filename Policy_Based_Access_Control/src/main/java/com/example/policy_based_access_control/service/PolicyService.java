package com.example.policy_based_access_control.service;

import com.example.policy_based_access_control.model.Policy;
import com.example.policy_based_access_control.model.Request;

import java.util.List;


public interface PolicyService {
    Policy create(Policy policy);
    List<Policy> findAll();
    Boolean isAllowed(Request request);
}
