package com.example.policy_based_access_control;

import com.example.policy_based_access_control.model.Condition;
import com.example.policy_based_access_control.model.EqualsSubjectCondition;
import com.example.policy_based_access_control.model.Policy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by igor on 2017-07-21.
 */
//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PolicyTest {



    @Test
    public void createAndReadPolicy(){

        Policy testPolicy = new Policy() {};
        Map<String, Condition> conditions = new HashMap<>();

                conditions.put("isBankUser", new StringEqualCondition("true"));
                conditions.put("resourceOwner", new EqualsSubjectCondition());
            testPolicy.setId("1");
            testPolicy.setDescription("One policy to rule them all.");
            testPolicy.setSubjects(Arrays.asList("users:<[peter|ken]>", "users:maria", "groups:admins"));
            testPolicy.setResources(Arrays.asList("resources:articles:<.*>", "resources:printer"));
            testPolicy.setActions(Arrays.asList("delete", "<[create|update]>"));
            testPolicy.setEffect("allow");
            testPolicy.setConditions(conditions);

        ObjectMapper mapper = new ObjectMapper();

        String json;
        try {
             json = mapper.writeValueAsString(testPolicy);
             log.info("json: {}", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
