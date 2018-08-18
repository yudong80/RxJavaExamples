package com.yudong80.rxsubway;

import static com.yudong80.rxsubway.SubwayConstants.URL;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubwayV2 {
	private OkHttpClient client = new OkHttpClient();
	
	public void run() { 
		getSubwaySingle(URL)
			.subscribe(
				System.out::println,   //정상적인 결과는 System.out 으로 출력
				System.err::println);  //오류는 System.err에 출력
	}
	
	public Single<String> getSubwaySingle(String url) { 
		return Single.create(e -> {
			Request request = new Request.Builder()
			        .url(url)
			        .build();
			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					e.onSuccess(response.body().string());
				} 			
				
				@Override
				public void onFailure(Call call, IOException ioe) {
					e.onError(ioe);						
				}
			});
		});
	}	
	
	public static void main(String[] args) { 
		new SubwayV2().run();	
	}
}
