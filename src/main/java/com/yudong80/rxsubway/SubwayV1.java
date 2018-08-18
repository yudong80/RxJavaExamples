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
	        "sample" + //user key�� �־�� �մϴ� 
			"/xml/realtimeStationArrival/0/5/����";
	
	private OkHttpClient client = new OkHttpClient();
		
	public void run() { 		
		Request request = new Request.Builder()
		        .url(URL)
		        .build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				//�������� �� 
				e.printStackTrace();	
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//��� ��� 
				System.out.println(response.body().string());
			} 			
		});
	}

	public static void main(String[] args) { 
		new SubwayV1().run();	
	}
}
