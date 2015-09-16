package com.example.todolist.test;

import com.example.todolist.LoginActivity;
import com.example.todolist.test.utils.NetworkUtil;
import com.example.todolist.test.utils.Util;
import com.robotium.solo.Solo;
import android.os.PowerManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Printer;

abstract public class BasicTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	protected UIHelper uiHelper;
	//private UIHelper uiHelper;
	private PowerManager.WakeLock wakeScreenObject=null;
	
	public BasicTestCase() {
		super(LoginActivity.class);
	}
	
	/**
	 * 设置设备
	 * 这个方法将在一个测试被执行之前被执行
	 * 重写setUp方法，进行异常捕获，截图处理
	 * 
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		try {
			super.setUp();
			//调用该方法唤醒手机、解锁、连接网络
			init();
		} catch (Throwable tr) {
			System.out.println(wakeScreenObject);
			//截图处理
			solo.takeScreenshot(this.getClass().getSimpleName());
			//异常处理
			throw new SetUpException(tr);
		}
		
	}
	
	/**
	 * 在移动到下一个测试之前，确定所有的资源都清理和垃圾被收集，所有子类覆盖这个方法后应该确保在方法最后都调用super.tearDown()
	 * 重写tearDown方法，进行异常捕获，截图处理
	 * 
	 * @throws Exception
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		try {
			if(wakeScreenObject!=null){
				wakeScreenObject.release();
				wakeScreenObject=null;
			}
			//关闭所有打开的Activity
			solo.finishOpenedActivities();
			uiHelper=null;
			super.tearDown();
		} catch (Throwable th) {
			//截图处理
			solo.takeScreenshot(this.getClass().getSimpleName());
			//异常处理
			throw new TearDownException(th);
		}
		
	}
	
	/**
	 * 唤醒设备、解锁、连接网络
	 * 
	 */
	public void init(){
		solo=new Solo(getInstrumentation(),getActivity());
		uiHelper=new UIHelper(solo);
		
		//此处调用封装在Util类中唤醒设备的方法
		if(wakeScreenObject==null){
			wakeScreenObject=Util.wakeScreen(this);
		}
		//此处调用封装在Util类中解锁设备的方法
		Util.unlock(getInstrumentation());
		//此处调用封装在Util类中连接网络的方法
		NetworkUtil.setAirplaneModeOffAndNetworkOn(getInstrumentation().getContext());
		
	}
	
	/**
	 * 运行测试序列
	 * 
	 */
	@Override
	public void runBare() throws Throwable {
		// TODO Auto-generated method stub
		try {
			super.runBare();
		} catch (SetUpException smte) {
			tearDown();
			throw smte;
		}catch (RunTestException rte) {
			tearDown();
			throw rte;
		}catch (TearDownException tde) {
			tearDown();
			throw tde;
		}
	}
	
	/**
	 * 运行当前的单元测试。
	 * 如果这个单元测试被UIThread注释，在切换到UI线程之前强迫创建这个Activity
	 * 
	 */
	@Override
	protected void runTest() throws Throwable {
		// TODO Auto-generated method stub
		try {
			super.runTest();
		} catch (Throwable th) {
			solo.takeScreenshot(getClass().getSimpleName());
			throw new RunTestException(th);
		}
	}
	
	
}
/**
 * 三个自定义的异常类，对应setUp时发生的异常；
 * @author chenqianjiao
 *
 */
class SetUpException extends Exception{
	private static final long serialVersionUID=1l;
	public SetUpException(Throwable e) {
		super("Errer in BasicTestCase.setUp()!",e);
	}
}

/**
 * 三个自定义的异常类，runTest发生的异常；
 * @author chenqianjiao
 *
 */
class RunTestException extends Exception{
	private static final long serialVersionUID=2l;
	RunTestException(Throwable e){
		super("Errer in BasicTestCase.runTest()!",e);
	}
}

/**
 * 三个自定义的异常类TearDown发生的异常；
 * @author chenqianjiao
 *
 */
class TearDownException extends Exception{
	private static final long serialVersionUID=3l;
	TearDownException(Throwable e){
		super("Errer in BasicTestCase.tearDown()!",e);
	}
}