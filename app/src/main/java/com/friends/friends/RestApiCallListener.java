package com.friends.friends;
public interface RestApiCallListener {
	
	void onResponse(String response, int pageId);
	void onError(String error);
}
