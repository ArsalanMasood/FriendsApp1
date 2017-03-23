package restetapi;
public interface RestApiCallListener {
	
	void onResponse(String response, int pageId);
	void onError(String error);
}
