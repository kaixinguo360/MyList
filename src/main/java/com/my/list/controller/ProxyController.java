package com.my.list.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Enumeration;

@RestController
@RequestMapping("/p")
public class ProxyController {

    private final RestTemplate rest;
    
    public ProxyController() {
        this.rest = new RestTemplate();
    }
    
    @RequestMapping(value = "/img/{url64}")
    protected ResponseEntity<byte[]> proxyImage(HttpServletRequest request, 
                                                @PathVariable String url64) {

        String url = new String(Base64.getUrlDecoder().decode(url64));
        
        HttpHeaders headers = new HttpHeaders();
        Enumeration headerNames = request.getHeaderNames();
        
        while(headerNames!=null && headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            
            if(headerValue != null) {
                headers.add(headerName, headerValue);
            }
        }
        
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        
        try {
            return rest.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
        } catch (RestClientResponseException e) {
            HttpStatus status = HttpStatus.resolve(e.getRawStatusCode());
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
