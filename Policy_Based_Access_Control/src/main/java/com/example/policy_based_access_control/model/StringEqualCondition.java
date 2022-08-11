package com.example.policy_based_access_control.model;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by igor on 2017-07-22.
 */
@Entity(name = "StringEqualCondition")
@DiscriminatorValue("StringEqualCondition")
@Slf4j
public class StringEqualCondition extends Condition {

    public  StringEqualCondition(String value){
        this.options.setProperty("equals", value);
    }

    public  StringEqualCondition(){

    }

    @Override
    public Boolean fullfills(String conditionKey,Request request) {

        if (!request.context.containsKey(conditionKey))
                return false;
        log.info("Condition Key: {}", conditionKey);
        log.info("Request: {}", request);
        String value = request.context.getProperty(conditionKey);
        String conditionValue = this.options.getProperty("equals");
        return conditionValue.equals(value);

    }
}
