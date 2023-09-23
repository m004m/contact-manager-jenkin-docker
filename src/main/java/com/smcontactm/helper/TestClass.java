package com.smcontactm.helper;

import java.time.Clock;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Clock clock = Clock.systemDefaultZone();
		long milliSeconds=clock.millis();
		System.out.println(milliSeconds);

	}

}
