package com.example.restclient.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;


/**
 * Service API
 *
 * @author Duval
 */
public class ServiceHelper {

	
	public static String ACTION_REQUEST_RESULT = "REQUEST_RESULT";
	public static String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
	public static String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
	
	private static final String REQUEST_ID = "REQUEST_ID";

	private static final String estabelecimentosHashkey = "ESTABS";
	
	private static Object lock = new Object();

	private static ServiceHelper instance;

	//TODO: refactor the key
	private Map<String,Long> pendingRequests = new HashMap<String,Long>();
	private Context ctx;

	private ServiceHelper(Context ctx){
		this.ctx = ctx.getApplicationContext();
	}

	public static ServiceHelper getInstance(Context ctx){
		synchronized (lock) {
			if(instance == null){
				instance = new ServiceHelper(ctx);			
			}
		}

		return instance;		
	}

	public long getEstabelecimentos() {
		if(pendingRequests.containsKey(estabelecimentosHashkey)){
			return pendingRequests.get(estabelecimentosHashkey);
		}

		long requestId = generateRequestID();
		pendingRequests.put(estabelecimentosHashkey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetEstabelecimentosResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, Service.class);
		intent.putExtra(Service.METHOD_EXTRA, Service.METHOD_GET);
		intent.putExtra(Service.RESOURCE_TYPE_EXTRA, Service.RESOURCE_TYPE_ESTABELECIMENTOS);
		intent.putExtra(Service.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		//precisa declarar no manifesto
		this.ctx.startService(intent);
		
		return requestId;		
	}
	
/*	
	public long getProfile(){

		if(pendingRequests.containsKey(profileHashkey)){
			return pendingRequests.get(profileHashkey);
		}

		long requestId = generateRequestID();
		pendingRequests.put(profileHashkey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetProfileResponse(resultCode, resultData);
			}

		};

		Intent intent = new Intent(this.ctx, TwitterService.class);
		intent.putExtra(TwitterService.METHOD_EXTRA, TwitterService.METHOD_GET);
		intent.putExtra(TwitterService.RESOURCE_TYPE_EXTRA, TwitterService.RESOURCE_TYPE_PROFILE);
		intent.putExtra(TwitterService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);

		return requestId;
	}
*/	

	private long generateRequestID() {
		long requestId = UUID.randomUUID().getLeastSignificantBits();
		return requestId;
	}

	public boolean isRequestPending(long requestId){
		return this.pendingRequests.containsValue(requestId);
	}

/*	
	private void handleGetProfileResponse(int resultCode, Bundle resultData){


		Intent origIntent = (Intent)resultData.getParcelable(TwitterService.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null){
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(profileHashkey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);

			ctx.sendBroadcast(resultBroadcast);

		}
	}
*/

	private void handleGetEstabelecimentosResponse(int resultCode, Bundle resultData){

		Intent origIntent = (Intent)resultData.getParcelable(Service.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null){
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(estabelecimentosHashkey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);

			ctx.sendBroadcast(resultBroadcast);
		}
	}	
}
