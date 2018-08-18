package com.yudong80.rxsubway;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubwayV1 {
	private static final String URL = 
			"http://swopenapi.seoul.go.kr/api/subway/" + 
	        "sample" + //user key를 넣어야 합니다 
			"/xml/realtimeStationArrival/0/5/서울";
	
	private OkHttpClient client = new OkHttpClient();
		
	public void run() { 		
		Request request = new Request.Builder()
		        .url(URL)
		        .build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				//실패했을 때 
				e.printStackTrace();	
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//결과 출력 
				System.out.println(response.body().string());
			} 			
		});
	}

	public static void main(String[] args) { 
		new SubwayV1().run();	
	}
}
