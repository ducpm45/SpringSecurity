package com.example.policy_based_access_control.service.impl;

import com.example.policy_based_access_control.model.Policy;
import com.example.policy_based_access_control.model.Request;
import com.example.policy_based_access_control.repository.PolicyRepository;
import com.example.policy_based_access_control.service.ExpressionMatcherService;
import com.example.policy_based_access_control.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository repository;
    private final ExpressionMatcherService matcher;

    @Autowired
    public PolicyServiceImpl(PolicyRepository repository, ExpressionMatcherService matcher){
        this.repository = repository;
        this.matcher = matcher;
    }

    @Override
    public Policy create(Policy policy) {

        return this.repository.saveAndFlush(policy);
    }

    @Override
    public List<Policy> findAll() {
        return repository.findAll();
    }

    @Override
    public Boolean isAllowed(Request request) {

        boolean allowed = false;

        List<Policy> policies= repository.findAll();

        // Iterate through all policies
        for (Policy policy: policies) {
            if(null != policy.getAccessPeriod()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String accessPeriod = policy.getAccessPeriod();
                LocalDateTime accessPeriodTime = LocalDateTime.parse(accessPeriod, formatter);

                String currentTime = request.getCurrentTime();
                LocalDateTime requestTime = LocalDateTime.parse(currentTime, formatter);

                return requestTime.isBefore(accessPeriodTime);
            }
            // Does the action match with one of the policy actions?
            // This is the first check because usually actions are a superset of get|update|delete|set
            // and thus match faster.
            if(!matcher.Matches(policy.getActions(), request.getAction())){
                // no, continue to next policy
                continue;
            }

            // Does the subject match with one of the policy subjects?
            // There are usually less subjects than resources which is why this is checked
            // before checking for resources.
            if(!matcher.Matches(policy.getSubjects(),request.getSubject())){
                // no, continue to next policy
                continue;
            }

            // Does the resource match with one of the policy resources?
            if(!matcher.Matches(policy.getResources(), request.getResource())){
                // no, continue to next policy
                continue;
            }

            // Are the policies conditions met?
            // This is checked first because it usually has a small complexity.
            if (!policy.passesConditions(request)){
                continue;
            }

            // Is the policies effect deny? If yes, this overrides all allow policies -> access denied.

            if(!policy.allowAccess()){
                return false;
            }

            //we found at least one policy that allows this request
            allowed = true;
        }

        return  allowed;
    }
}
