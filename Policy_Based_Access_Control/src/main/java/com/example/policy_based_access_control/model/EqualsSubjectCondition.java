package com.example.policy_based_access_control.model;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by igor on 2017-07-22.
 */
@Entity(name = "EqualsSubjectCondition")
@DiscriminatorValue("EqualsSubjectCondition")
@Slf4j
public class EqualsSubjectCondition extends Condition {

    @Override
    public Boolean fullfills(String conditionKey,Request request) {

        if (!request.context.containsKey(conditionKey))
            return false;
        log.info("Condition Key: {}", conditionKey);
        log.info("Request: {}", request);
        String value = request.context.getProperty(conditionKey);

        return request.subject.equals(value);
    }
}
