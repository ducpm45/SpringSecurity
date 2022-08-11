package com.example.policy_based_access_control.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Policy implements Serializable {

    public static Policy create(
            String id,
            String description,
            List<String> subjects,
            List<String> resources,
            List<String> actions,
            String accessPeriod,
            String effect){

        Policy policy = new Policy();

        policy.id = id;
        policy.description = description;
        policy.subjects = subjects;
        policy.effect = effect;
        policy.resources = resources;
        policy.actions = actions;
        policy.accessPeriod = accessPeriod;
        return policy;
    }

    @Id
    private String id;

    private String description;

    @ElementCollection
    private List<String> subjects = new ArrayList<>();

    private String accessPeriod;
    private String effect;
    @ElementCollection
    private List<String> resources = new ArrayList<>();

    @ElementCollection
    private List<String> actions = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Map<String,Condition> conditions = new HashMap<>();

    // AllowAccess returns true if the policy effect is allow, otherwise false.
    public Boolean allowAccess(){
        return effect.equals("allow");
    }


    public void addCondition(String key, Condition condition) {
        conditions.put(key, condition);
    }


    public Boolean passesConditions(Request request){
        for(Map.Entry<String, Condition> entry : conditions.entrySet()) {
            String key = entry.getKey();
            Condition condition = entry.getValue();

            if(Boolean.FALSE.equals(condition.fullfills(key,request))){
                return  false;
            }
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getAccessPeriod() {
        return accessPeriod;
    }

    public void setAccessPeriod(String accessPeriod) {
        this.accessPeriod = accessPeriod;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public Map<String, Condition> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }

}
