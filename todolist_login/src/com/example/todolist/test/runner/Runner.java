package com.example.todolist.test.runner;

import android.test.InstrumentationTestRunner;
import junit.framework.TestSuite;

public class Runner extends InstrumentationTestRunner{

	@Override
	public TestSuite getAllTests() {
		TestSuite suite = new TestSuite();
		suite.addTest(LoginRunner.geTestSuite());
		//suite.addTest(TodoListSuite.geTestSuite());
		return super.getAllTests();
	}
	
}
