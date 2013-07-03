package com.socialbar.android.restapi.rest;

import com.socialbar.android.restapi.resource.Resource;

public interface RestMethod<T extends Resource>{

	public RestMethodResult<T> execute();
}
