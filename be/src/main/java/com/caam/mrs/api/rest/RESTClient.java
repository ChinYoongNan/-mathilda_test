package com.caam.mrs.api.rest;

import org.springframework.util.MultiValueMap;

public interface RESTClient {
	Object post(String resourceUrl, MultiValueMap<String, String> request, String username, String password);
}
