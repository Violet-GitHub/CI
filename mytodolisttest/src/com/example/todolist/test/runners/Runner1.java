package com.example.todolist.test.runners;

import com.example.todolist.test.testsuites.LoginSuite;
import com.example.todolist.test.testsuites.ToDoListSuite;

import android.test.InstrumentationTestRunner;
import junit.framework.TestSuite;

public class Runner1 extends CommonRunner {
	
	@Override
	public TestSuite getAllTests() {
		// TODO Auto-generated method stub
		TestSuite suite = new TestSuite();
		suite.addTest(LoginSuite.getLoginSuite());
		suite.addTest(ToDoListSuite.getTodolistSuite());
		return suite;	
	}

}
