package com.example.todolist.test.runner;

import junit.framework.TestSuite;

public class Runner1 extends CommonRunner{

	@Override
	public TestSuite getAllTests() {
		TestSuite suite = new TestSuite();
		suite.addTest(LoginRunner.geTestSuite());
		suite.addTest(TodoListSuite.geTestSuite());
		return suite;
	}
	
}
