package com.yudong80.beginrx;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ObservableExample {
	Observable<String> sayObservable = Observable.create(new ObservableOnSubscribe<String>() {
		@Override
		public void subscribe(ObservableEmitter<String> e) throws Exception {
			e.onNext("hello");
			e.onComplete();
		}		
	});
	
	public Observable<String> getSayObservable() { 
		return sayObservable;
	}
	
	public void sayHello() { 
		getSayObservable().subscribe(v -> System.out.println("say " + v));
	}
	
	public static void main(String[] args) { 
		new ObservableExample().sayHello();
	}
}
