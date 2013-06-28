package com.example.restclient.rest;

import com.example.restclient.resource.Resource;

public interface RestMethod<T extends Resource>{

	public RestMethodResult<T> execute();
}
