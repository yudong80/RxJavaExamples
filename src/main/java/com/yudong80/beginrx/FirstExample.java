package com.yudong80.beginrx;

import io.reactivex.Observable;

public class FirstExample {
	public void notSubscribed() { 
		Observable.just("Hello").doOnNext(str -> System.out.println("say " + str));
	}
	
	public void subscribed() { 
		Observable.just("Hello").subscribe(str -> System.out.println("say " + str));
	}
	
	public static void main(String[] args) { 
		FirstExample demo = new FirstExample();
		demo.notSubscribed();
		demo.subscribed();
	}
}
