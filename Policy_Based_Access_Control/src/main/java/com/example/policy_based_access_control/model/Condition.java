package com.example.policy_based_access_control.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.util.Properties;

/**
 * Created by igor on 2017-07-21.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EqualsSubjectCondition.class, name = "EqualsSubjectCondition")
})

@Entity
@Table(name = "tbl_condition")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Condition_Type")
public abstract class Condition {

     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    protected Long id;

    @Transient
    public Properties options = new Properties();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Properties getOptions() {
        return options;
    }

    public void setOptions(Properties options) {
        this.options = options;
    }

    public abstract Boolean fullfills(String conditionKey, Request request);
}
