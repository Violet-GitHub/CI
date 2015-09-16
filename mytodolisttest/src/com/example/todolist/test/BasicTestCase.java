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
	 * �����豸
	 * �����������һ�����Ա�ִ��֮ǰ��ִ��
	 * ��дsetUp�����������쳣���񣬽�ͼ����
	 * 
	 * @throws Exception
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		try {
			super.setUp();
			//���ø÷��������ֻ�����������������
			init();
		} catch (Throwable tr) {
			System.out.println(wakeScreenObject);
			//��ͼ����
			solo.takeScreenshot(this.getClass().getSimpleName());
			//�쳣����
			throw new SetUpException(tr);
		}
		
	}
	
	/**
	 * ���ƶ�����һ������֮ǰ��ȷ�����е���Դ��������������ռ����������า�����������Ӧ��ȷ���ڷ�����󶼵���super.tearDown()
	 * ��дtearDown�����������쳣���񣬽�ͼ����
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
			//�ر����д򿪵�Activity
			solo.finishOpenedActivities();
			uiHelper=null;
			super.tearDown();
		} catch (Throwable th) {
			//��ͼ����
			solo.takeScreenshot(this.getClass().getSimpleName());
			//�쳣����
			throw new TearDownException(th);
		}
		
	}
	
	/**
	 * �����豸����������������
	 * 
	 */
	public void init(){
		solo=new Solo(getInstrumentation(),getActivity());
		uiHelper=new UIHelper(solo);
		
		//�˴����÷�װ��Util���л����豸�ķ���
		if(wakeScreenObject==null){
			wakeScreenObject=Util.wakeScreen(this);
		}
		//�˴����÷�װ��Util���н����豸�ķ���
		Util.unlock(getInstrumentation());
		//�˴����÷�װ��Util������������ķ���
		NetworkUtil.setAirplaneModeOffAndNetworkOn(getInstrumentation().getContext());
		
	}
	
	/**
	 * ���в�������
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
	 * ���е�ǰ�ĵ�Ԫ���ԡ�
	 * ��������Ԫ���Ա�UIThreadע�ͣ����л���UI�߳�֮ǰǿ�ȴ������Activity
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
 * �����Զ�����쳣�࣬��ӦsetUpʱ�������쳣��
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
 * �����Զ�����쳣�࣬runTest�������쳣��
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
 * �����Զ�����쳣��TearDown�������쳣��
 * @author chenqianjiao
 *
 */
class TearDownException extends Exception{
	private static final long serialVersionUID=3l;
	TearDownException(Throwable e){
		super("Errer in BasicTestCase.tearDown()!",e);
	}
}