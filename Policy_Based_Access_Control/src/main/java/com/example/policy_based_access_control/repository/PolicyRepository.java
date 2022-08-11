package com.example.policy_based_access_control.repository;

import com.example.policy_based_access_control.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by igor on 2017-07-21.
 */
@Repository
public interface PolicyRepository extends JpaRepository<Policy,String> {
}
