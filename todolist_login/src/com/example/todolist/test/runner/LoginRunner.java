package com.example.todolist.test.runner;

import com.example.todolist.test.testcase.login.Login1;
import com.example.todolist.test.testcase.login.Login2;
import com.example.todolist.test.testcase.login.Login3;

import junit.framework.TestSuite;

public class LoginRunner {
	public static TestSuite geTestSuite(){
		TestSuite suite = new TestSuite();
		suite.addTestSuite(Login1.class);
		suite.addTestSuite(Login2.class);
		suite.addTestSuite(Login3.class);
		return suite;
	}
}
