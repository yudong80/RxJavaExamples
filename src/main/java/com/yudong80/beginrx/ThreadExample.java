package com.yudong80.beginrx;

public class ThreadExample {
	public void runnable() { 
		final String str = "hello";
		Runnable r = () -> System.out.println("say " + str);
		new Thread(r).start();
	}
	
	public void runnableWithArgs(String str) { 
		Runnable r = () -> System.out.println("say " + str);
		new Thread(r).start();		
	}
	
	//�̷��� ������ �� �����ϴ�. 
	//Runnable mRunnable = () -> System.out.println("say " + str);
	
	public static void main(String[] args) { 
		ThreadExample demo = new ThreadExample();
		demo.runnable();
		demo.runnableWithArgs("hello");
	}
}
