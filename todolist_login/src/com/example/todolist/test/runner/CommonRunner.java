package com.example.todolist.test.runner;

import com.zutubi.android.junitreport.JUnitReportTestRunner;

import junit.framework.TestSuite;

public class CommonRunner extends JUnitReportTestRunner{
	
	@Override
	public TestSuite getAllTests() {
		TestSuite suite=new TestSuite();
		
		
		return super.getAllTests();
		
	}
	
}
