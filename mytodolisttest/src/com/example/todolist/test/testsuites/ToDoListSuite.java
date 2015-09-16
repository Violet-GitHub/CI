package com.example.todolist.test.testsuites;

import com.example.todolist.test.testcases.todolist.ToDoList_1;
import com.example.todolist.test.testcases.todolist.ToDoList_2;

import junit.framework.TestSuite;

public class ToDoListSuite {
	public static TestSuite getTodolistSuite(){
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ToDoList_1.class);
		suite.addTestSuite(ToDoList_2.class);
		return suite;
	}
}
