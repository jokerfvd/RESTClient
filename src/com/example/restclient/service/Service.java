package com.example.restclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class Service extends IntentService {

	public static final String METHOD_EXTRA = "com.jeremyhaberman.restfulandroid.service.METHOD_EXTRA";

	public static final String METHOD_GET = "GET";

	public static final String RESOURCE_TYPE_EXTRA = "com.jeremyhaberman.restfulandroid.service.RESOURCE_TYPE_EXTRA";
	
	public static final String RESOURCE_ID = "com.example.restclient.service.RESOURCE_ID";
	
	public static final String ESTABS_TIPO = "com.example.restclient.service.ESTABS_TIPO";

	public static final int RESOURCE_TYPE_ESTABELECIMENTOS = 1;
	public static final int RESOURCE_TYPE_ESTABELECIMENTO = 2;
	public static final int RESOURCE_TYPE_USUARIO = 3;
	
	public static final int ESTABS_NORMAL = 1;
	public static final int ESTABS_COORDENADA = 2;
	public static final int ESTABS_FILTRO = 3;

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
		mOriginalRequestIntent = requestIntent;

		// Get request data from Intent
		String method = requestIntent.getStringExtra(Service.METHOD_EXTRA);
		int resourceType = requestIntent.getIntExtra(Service.RESOURCE_TYPE_EXTRA, -1);
		mCallback = requestIntent.getParcelableExtra(Service.SERVICE_CALLBACK);

		switch (resourceType) {
		case RESOURCE_TYPE_ESTABELECIMENTOS:
			int estabsType = requestIntent.getIntExtra(Service.ESTABS_TIPO, -1);
			if (method.equalsIgnoreCase(METHOD_GET)) {
				switch (estabsType) {
				case ESTABS_NORMAL:
					EstabelecimentosProcessor processor = new EstabelecimentosProcessor(getApplicationContext());
					processor.getEstabelecimentos(makeProcessorCallback());
					break;
				case ESTABS_COORDENADA:
					break;
				case ESTABS_FILTRO:
					break;
				default:
					mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
					break;
				}
			
				
			} else {
				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			}
			break;
		case RESOURCE_TYPE_ESTABELECIMENTO:
			if (method.equalsIgnoreCase(METHOD_GET)) {
				EstabelecimentoProcessor processor = new EstabelecimentoProcessor(getApplicationContext());
				processor.getEstabelecimento(makeProcessorCallback());
			}
			break;
		case RESOURCE_TYPE_USUARIO:
			break;
			
		default:
			mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			break;
		}

	}

	private ProcessorCallback makeProcessorCallback() {
		ProcessorCallback callback = new ProcessorCallback() {

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
