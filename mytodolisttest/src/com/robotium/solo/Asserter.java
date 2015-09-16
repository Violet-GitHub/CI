package com.robotium.solo;

import junit.framework.Assert;
import android.app.Activity;
import android.app.ActivityManager;

/**
 * ���ԵĶ��Է������ṩ����֧�֣���Ҫ��װjunit�ṩ��
 * Contains assert methods examples are assertActivity() and assertLowMemory().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 *
 */

class Asserter {
	// activity����������
	private final ActivityUtils activityUtils;
	// �ȴ�������
	private final Waiter waiter;

	/**
	 * �������ʼ��
	 * Constructs this object.
	 *
	 * @param activityUtils the {@code ActivityUtils} instance.
	 * @param waiter the {@code Waiter} instance.
	 */

	public Asserter(ActivityUtils activityUtils, Waiter waiter) {
		this.activityUtils = activityUtils;
		this.waiter = waiter;
	}

	/**
	 * �����жϵ�ǰactivity�Ƿ�����Ҫ��
	 * message ��ǰactivity����name���ֲ�һ�������������ʾ
	 * name    ������activity����
	 * Asserts that an expected {@link Activity} is currently active one.
	 *
	 * @param message the message that should be displayed if the assert fails
	 * @param name the name of the {@code Activity} that is expected to be active e.g. {@code "MyActivity"}
	 */

	public void assertCurrentActivity(String message, String name) {
		// ʹ��wait���ߵȴ�������activity����,ֱ�ӻ�ȡactivity��ջ��ջ��activity,Ĭ�ϳ�ʱ10s
		boolean foundActivity = waiter.waitForActivity(name);
		// ���������activityδ�ҵ������ö�����ʾ����쳣
		if(!foundActivity)
			Assert.assertEquals(message, name, activityUtils.getCurrentActivity().getClass().getSimpleName());		
	}

	/**
	 * ����Class����Ե�ǰactivity�Ƿ���������activity
	 * message ������������ģ����Ե���ʾ��Ϣ
	 * expectedClass ������activity��
	 * Asserts that an expected {@link Activity} is currently active one.
	 *
	 * @param message the message that should be displayed if the assert fails
	 * @param expectedClass the {@code Class} object that is expected to be active e.g. {@code MyActivity.class}
	 */

	public void assertCurrentActivity(String message, Class<? extends Activity> expectedClass) {
		// null���
		if(expectedClass == null){
			Assert.fail("The specified Activity is null!");
		}
		// ���������class��Ӧ��activity�Ƿ����,ֱ�ӻ�ȡactivity��ջ��ջ��activity,Ĭ�ϳ�ʱ10s
		boolean foundActivity = waiter.waitForActivity(expectedClass);
		// δ�ҵ������Ը���������ʾ
		if(!foundActivity) {
			Assert.assertEquals(message, expectedClass.getName(), activityUtils.getCurrentActivity().getClass().getName());
		}
	}

	/**
	 * ���Ե�ǰactivity�Ƿ��������activity����һ��
	 * message ��һ�µ���ʾ��Ϣ
	 * name    ������activity����
	 * isNewInstance Ϊtrue��ȴ����³��ֵ�activity,Ϊfalse��ֱ�ӻ�ȡactivity��ջ��ջ��activity���Ƚ�
	 * Asserts that an expected {@link Activity} is currently active one, with the possibility to
	 * verify that the expected {@code Activity} is a new instance of the {@code Activity}.
	 * 
	 * @param message the message that should be displayed if the assert fails
	 * @param name the name of the {@code Activity} that is expected to be active e.g. {@code "MyActivity"}
	 * @param isNewInstance {@code true} if the expected {@code Activity} is a new instance of the {@code Activity}
	 */

	public void assertCurrentActivity(String message, String name, boolean isNewInstance) {
		// ��鵱ǰactivity�������Ƿ���������
		assertCurrentActivity(message, name);
		// ��鵱ǰactivity��������Ƿ��Ǵ���
		assertCurrentActivity(message, activityUtils.getCurrentActivity().getClass(),
				isNewInstance);
	}

	/**
	 * ���class���Ƿ�Ϊ��ǰ��activity
	 * message ������������activityʱ��ʾ�쳣��Ϣ
	 * expectedClass ������activity��
	 * isNewInstance Ϊtrue��ȴ����³��ֵ�activity,Ϊfalse��ֱ�ӻ�ȡactivity��ջ��ջ��activity���Ƚ�
	 * Asserts that an expected {@link Activity} is currently active one, with the possibility to
	 * verify that the expected {@code Activity} is a new instance of the {@code Activity}.
	 * 
	 * @param message the message that should be displayed if the assert fails
	 * @param expectedClass the {@code Class} object that is expected to be active e.g. {@code MyActivity.class}
	 * @param isNewInstance {@code true} if the expected {@code Activity} is a new instance of the {@code Activity}
	 */

	public void assertCurrentActivity(String message, Class<? extends Activity> expectedClass,
			boolean isNewInstance) {
		boolean found = false;
		// ���жϵ�ǰ���Ƿ���������
		assertCurrentActivity(message, expectedClass);
		// ��ȡactivity��ջ��ջ��activity
		Activity activity = activityUtils.getCurrentActivity(false);
		// �жϵ�ǰ�򿪵����е�activity���Ƿ����������
		for (int i = 0; i < activityUtils.getAllOpenedActivities().size() - 1; i++) {
			String instanceString = activityUtils.getAllOpenedActivities().get(i).toString();
			if (instanceString.equals(activity.toString()))
				found = true;
		}
		// �����ж��Ƿ����
		Assert.assertNotSame(message, isNewInstance, found);
	}

	/**
	 * ��鵱ǰ�Ƿ��ڴ����
	 * Asserts that the available memory is not considered low by the system.
	 */

	public void assertMemoryNotLow() {
		// �����ڴ���Ϣ����
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		// ��ȡ��ǰactivity�����ȡ�ڴ���Ϣ
		((ActivityManager)activityUtils.getCurrentActivity().getSystemService("activity")).getMemoryInfo(mi);
		// ͨ��lowMemory״̬�ж��Ƿ��ڴ����
		Assert.assertFalse("Low memory available: " + mi.availMem + " bytes!", mi.lowMemory);
	}

}