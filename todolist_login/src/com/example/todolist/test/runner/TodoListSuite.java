package com.example.todolist.test.runner;

import com.example.todolist.test.testcase.addtodolist.Addtodolist;
import com.example.todolist.test.testcase.deletetodolist.DeleteTodolist;
import com.example.todolist.test.testcase.edittodolist.Edittodolist;

import junit.framework.TestSuite;

public class TodoListSuite {

	public static TestSuite geTestSuite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(Addtodolist.class);
		suite.addTestSuite(Edittodolist.class);
		suite.addTestSuite(DeleteTodolist.class);
		return suite;
	}
	
}
