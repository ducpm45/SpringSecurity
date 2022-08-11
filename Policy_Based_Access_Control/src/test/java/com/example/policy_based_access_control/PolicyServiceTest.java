package com.example.policy_based_access_control;

import com.example.policy_based_access_control.model.*;
import com.example.policy_based_access_control.service.PolicyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional()
@Slf4j
public class PolicyServiceTest {

    private PolicyService policyService;

@Autowired
    public void PolicyServiceImpl(PolicyService service){
        this.policyService = service;
    }
    @Before
    public void setup(){
        Policy testPolicy = Policy.create(
                "1",
                "Allow - no conditions",
                Arrays.asList("users:<peter|ken>", "users:maria", "groups:admins"),
                Arrays.asList("resources:articles:<.*>", "resources:printer"),
                Arrays.asList("delete", "<create|update>"),
                null,
                "allow");

        policyService.create(testPolicy);

        testPolicy = Policy.create("2",
                "My profile - all",
                Collections.singletonList("users:<.*>"),
                Collections.singletonList("resources:profile:<.*>"),
                Arrays.asList("update","create"),
                null,
                "allow");


        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());

        policyService.create(testPolicy);

        testPolicy = Policy.create("3",
                "Deny my profile change approval",
                Collections.singletonList("users:<.*>"),
                Collections.singletonList("resources:profile:<.*>"),
                Collections.singletonList("approve"),
                null,
                "deny");

        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());

        policyService.create(testPolicy);

        testPolicy = Policy.create("4",
                 "Admin profile change approval",
                Collections.singletonList("groups:admins"),
                Collections.singletonList("resources:profile:<.*>"),
                Collections.singletonList("approve"),
                 null,
                 "allow");

        testPolicy.addCondition("isBankUser", new StringEqualCondition("true"));
        testPolicy.addCondition("isWorkingHours", new StringEqualCondition("true"));

        policyService.create(testPolicy);

        testPolicy = Policy.create(
                "5",
                "User profile change update and 1 day",
                Collections.singletonList("users:<.*>"),
                Collections.singletonList("resources:document:<.*>"),
                Collections.singletonList("update"),
                null,
                "allow"
        );
        testPolicy.addCondition("isBankUser", new StringEqualCondition("true"));

        policyService.create(testPolicy);

        testPolicy = Policy.create(
                "6",
                "User document create in 1 day",
                Collections.singletonList("users:<.*>"),
                Collections.singletonList("resources:document:<.*>"),
                Collections.singletonList("update"),
                "2022-12-08 17:00",
                "allow"
        );
        testPolicy.addCondition("resourceOwner", new EqualsSubjectCondition());
    }

    @Test
    public void peter_can_create_articles_returns_true() {
        Request request = new Request() {
            {
                subject ="users:peter";
                resource="resources:articles:";
                action="create";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void george_can_create_articles_returns_false() {
        Request request = new Request() {
            {
                subject ="users:george";
                resource="resources:articles:";
                action="create";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(false));
    }

    @Test
    public void admins_can_delete_printers_returns_true() {
        Request request = new Request() {
            {
                subject ="groups:admins";
                resource="resources:printer";
                action="delete";
            }
        };

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void any_user_can_update_his_address() {
        Request request = new Request() {
            {
                subject ="users:anon";
                resource="resources:profile:address";
                action="update";
            }
        };

        request.getContext().setProperty("resourceOwner","users:anon");

        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void any_user_authorizing_changes_to_profile_returns_false() {
        Request request = new Request() {
            {
                subject ="users:anon";
                resource="resources:profile:";
                action="approve";
            }
        };

        request.getContext().setProperty("resourceOwner","users:anon");

        assertThat(policyService.isAllowed(request), equalTo(false));
    }

    @Test
    public void admin_user_authorizing_changes_to_profile_returns_true() {
        Request request = new Request() {
            {
                subject ="groups:admins";
                resource="resources:profile:";
                action="approve";
            }
        };

        request.getContext().setProperty("isBankUser","true");
        request.getContext().setProperty("isWorkingHours","true");
        log.info("Request: {}", request);
        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void peter_can_authorizing_update_document() {
        Request request = new Request() {
            {
                subject = "users:ken";
                resource = "resources:document:1";
                action = "update";
            }
        };
        request.getContext().setProperty("isBankUser", "true");
        request.getContext().setProperty("accessTime", "2022-10-08 16:51");
        log.info("Request: {}", request);
        assertThat(policyService.isAllowed(request), equalTo(true));
    }

    @Test
    public void peter_can_authorizing_update_document_in_1day() {
        Request request = new Request(){
            {
                subject="users:peter";
                resource="resources:document:1234";
                action="update";
                currentTime="2022-08-11 14:17";
            }
        };
        request.getContext().setProperty("resourceOwner","users:peter");
        assertThat(policyService.isAllowed(request), equalTo(true));
    }
}

