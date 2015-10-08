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
		super.setUp();
		init();
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
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}
