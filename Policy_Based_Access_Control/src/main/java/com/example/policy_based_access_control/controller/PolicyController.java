package com.example.policy_based_access_control.controller;

import com.example.policy_based_access_control.model.Policy;
import com.example.policy_based_access_control.model.Request;
import com.example.policy_based_access_control.model.Response;
import com.example.policy_based_access_control.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Created by igor on 2017-07-22.
 */
@RestController
@RequestMapping(path="/policies")

public class PolicyController {

    private final PolicyService service;

    @Autowired
    public PolicyController(PolicyService service){
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Policy>> getList(){
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<Policy> list = service.findAll();

        return new ResponseEntity<>(list, httpHeaders, HttpStatus.OK);

    }

//    @RequestMapping( method = RequestMethod.POST )
    @PostMapping("")
    public ResponseEntity<Policy> create(@RequestBody Policy item ){
        HttpHeaders httpHeaders = new HttpHeaders();

        if (item == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }
        final Policy savedItem = service.create(item);

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(item.getId()).toUri());

        return new ResponseEntity<>(savedItem, httpHeaders, HttpStatus.CREATED);
    }

//    @RequestMapping( method = RequestMethod.POST , path = "/isAllowed")
    @PostMapping("/isAllowed")
    public ResponseEntity<Response> isAllowed(@RequestBody Request request ){
        HttpHeaders httpHeaders = new HttpHeaders();

        if (request == null)
        {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.EXPECTATION_FAILED);
        }

        Boolean isAllowed = service.isAllowed(request);

        if(!isAllowed){
            return new ResponseEntity<>(new Response("Deny"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new Response("Allow"), HttpStatus.OK);

    }
}
