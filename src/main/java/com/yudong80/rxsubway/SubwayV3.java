package com.yudong80.rxsubway;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.yudong80.rxsubway.SubwayConstants.URL;

public class SubwayV3 {
	private OkHttpClient client = new OkHttpClient();
	
	public void run() { 
		Single.just(URL)
			.map(this::sendHttpSync) //503 error? 
			.subscribe(
					System.out::println, 
					System.err::println);	
	}
	
	private String sendHttpSync(String url) { 
		Request request = new Request.Builder()
		        .url(url)
		        .build();
		try {
			return client.newCall(request).execute().body().string();
		} catch (IOException e) {
			return "ERROR:" + e.getMessage();
		}
	}
	
	public static void main(String[] args) { 
		new SubwayV3().run();	
	}
}
