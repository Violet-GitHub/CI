package com.example.todolist.test;

import com.example.todolist.LoginActivity;
import com.example.todolist.test.util.NetworkUtil;
import com.example.todolist.test.util.Util;
import com.robotium.solo.Solo;

import android.os.PowerManager;
import android.test.ActivityInstrumentationTestCase2;

public class BasicTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {
	public Solo solo=null;
	public UIHelper uiHelper=null;
	private PowerManager.WakeLock wakelockobject;
	
	public BasicTestCase() {
		super(LoginActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		try {
			super.setUp();
			init();
		} catch (Throwable tr) {
			solo.takeScreenshot(this.getClass().getSimpleName());
			throw new SetUpException(tr);
		}
		
	}
	
	@Override
	protected void runTest() throws Throwable {
		try {
			super.runTest();
		} catch (Throwable tr) {
			solo.takeScreenshot(this.getClass().getSimpleName());
			throw new RunTestException(tr);
		}
		
	}
	
	public void init(){
		solo=new Solo(getInstrumentation(),getActivity());
		uiHelper=new UIHelper(solo);
		
		//获取屏幕
		if (wakelockobject==null) {
			wakelockobject=Util.wakeScreen(this);
		}
		//解锁
		Util.unlock(getInstrumentation());
		//连接网络
		NetworkUtil.setAirplaneModeOffAndNetworkOn(getInstrumentation().getContext());
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			if (wakelockobject != null ) {
				wakelockobject.release();
				wakelockobject=null;
			}
			solo.finishOpenedActivities();
			uiHelper=null;
			super.tearDown();
		} catch (Throwable tr) {
			solo.takeScreenshot(this.getClass().getSimpleName());
			throw new TearDownException(tr);
		}
	}
	
	
	@Override
	public void runBare() throws Throwable {
		try {
			super.runBare();
		} catch (SetUpException sue) {
			tearDown();
			throw sue;
		}catch (RunTestException rte) {
			tearDown();
			throw rte;
		}catch (TearDownException tde) {
			tearDown();
			throw tde;
		}
	}
	

	class SetUpException extends Exception{
		public static final long serialVersionUID = 1L;
		public SetUpException(Throwable e) {
			super("Errer in BasicTestCase.setUp()! ",e);
		}
	}

	class RunTestException extends Exception{
		public static final long serialVersionUID = 2L;
		public RunTestException(Throwable e) {
			super("Errer in BasicTestCase.runTest()!",e);
		}
	}
	class TearDownException extends Exception{
		public static final long serialVersionUID = 3L;
		public TearDownException(Throwable e) {
			super("Errer in BasicTestCase.tearDown()!", e);
		}
	}
	
}

