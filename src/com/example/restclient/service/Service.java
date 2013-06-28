package com.example.restclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class Service extends IntentService {

	public static final String METHOD_EXTRA = "com.jeremyhaberman.restfulandroid.service.METHOD_EXTRA";

	public static final String METHOD_GET = "GET";

	public static final String RESOURCE_TYPE_EXTRA = "com.jeremyhaberman.restfulandroid.service.RESOURCE_TYPE_EXTRA";

	public static final int RESOURCE_TYPE_ESTABELECIMENTOS = 1;

	public static final String SERVICE_CALLBACK = "com.jeremyhaberman.restfulandroid.service.SERVICE_CALLBACK";

	public static final String ORIGINAL_INTENT_EXTRA = "com.jeremyhaberman.restfulandroid.service.ORIGINAL_INTENT_EXTRA";

	private static final int REQUEST_INVALID = -1;

	private ResultReceiver mCallback;

	private Intent mOriginalRequestIntent;

	public Service() {
		super("Service");
		//android.os.Debug.waitForDebugger();
		//System.out.println("Service()");
	}

	@Override
	protected void onHandleIntent(Intent requestIntent) {
		//android.os.Debug.waitForDebugger();
		//System.out.println("onHandleIntent");
		mOriginalRequestIntent = requestIntent;

		// Get request data from Intent
		String method = requestIntent.getStringExtra(Service.METHOD_EXTRA);
		int resourceType = requestIntent.getIntExtra(Service.RESOURCE_TYPE_EXTRA, -1);
		mCallback = requestIntent.getParcelableExtra(Service.SERVICE_CALLBACK);

		switch (resourceType) {
		case RESOURCE_TYPE_ESTABELECIMENTOS:

			if (method.equalsIgnoreCase(METHOD_GET)) {
				EstabelecimentosProcessor processor = new EstabelecimentosProcessor(getApplicationContext());
				processor.getEstabelecimentos(makeEstabelecimentosProcessorCallback());
			} else {
				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			}
			break;
			
		default:
			mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			break;
		}

	}

	private EstabelecimentosProcessorCallback makeEstabelecimentosProcessorCallback() {
		EstabelecimentosProcessorCallback callback = new EstabelecimentosProcessorCallback() {

			@Override
			public void send(int resultCode) {
				if (mCallback != null) {
					mCallback.send(resultCode, getOriginalIntentBundle());
				}
			}
		};
		return callback;
	}

	protected Bundle getOriginalIntentBundle() {
		Bundle originalRequest = new Bundle();
		originalRequest.putParcelable(ORIGINAL_INTENT_EXTRA, mOriginalRequestIntent);
		return originalRequest;
	}
}
