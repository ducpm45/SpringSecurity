package com.example.policy_based_access_control.service;

import java.util.List;

public interface ExpressionMatcherService {
    Boolean Matches(List<String> expressions, String searchString );
}
