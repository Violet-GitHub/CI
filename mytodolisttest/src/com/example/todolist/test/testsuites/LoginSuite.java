package com.example.todolist.test.testsuites;
import com.example.todolist.test.testcases.login.TestLogin1;
import com.example.todolist.test.testcases.login.TestLogin2;
import com.example.todolist.test.testcases.login.TestLogin3;

import junit.framework.TestSuite;

public class LoginSuite {
	public static TestSuite getLoginSuite(){
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestLogin1.class);
		suite.addTestSuite(TestLogin2.class);
		suite.addTestSuite(TestLogin3.class);
		return suite;
	}
	
	
}
