package com.robotium.solo;

import android.widget.EditText;
import android.widget.Spinner;
import junit.framework.Assert;
import android.app.Instrumentation;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

/**
 * ������������
 * Contains press methods. Examples are pressMenuItem(),
 * pressSpinnerItem().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Presser{
	// �������������
	private final Clicker clicker;
	// Instrument ���ڷ����¼�
	private final Instrumentation inst;
	// �ȴ�������
	private final Sleeper sleeper;
	// View�ȴ�������
	private final Waiter waiter;
	// ����������
	private final DialogUtils dialogUtils;
	// View��ȡ������
	private final ViewFetcher viewFetcher;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param clicker the {@code Clicker} instance
	 * @param inst the {@code Instrumentation} instance
	 * @param sleeper the {@code Sleeper} instance
	 * @param waiter the {@code Waiter} instance
	 * @param dialogUtils the {@code DialogUtils} instance
	 */

	public Presser(ViewFetcher viewFetcher, Clicker clicker, Instrumentation inst, Sleeper sleeper, Waiter waiter, DialogUtils dialogUtils) {
		this.viewFetcher = viewFetcher;
		this.clicker = clicker;
		this.inst = inst;
		this.sleeper = sleeper;
		this.waiter = waiter;
		this.dialogUtils = dialogUtils;
	}


	/**
	 * ���Menu�еĵ�n��Item,Item�������Ҵ��ϵ��£���˳������,Ĭ��ÿ�а���3��Item
	 * Presses a {@link android.view.MenuItem} with a given index. Index {@code 0} is the first item in the
	 * first row, Index {@code 3} is the first item in the second row and
	 * index {@code 5} is the first item in the third row.
	 *
	 * @param index the index of the {@code MenuItem} to be pressed
	 */

	public void pressMenuItem(int index){
		// ����ÿ��3�� Item
		pressMenuItem(index, 3);
	}

	/**
	 * ���Menu�еĵ�n��Item,Item�������Ҵ��ϵ��£���˳������,
	 * index	              ��Ҫ����ĵ�n��Item,��1��ʼ
	 * itemsPerRow   ÿһ�а�����Item����
	 * Presses a {@link android.view.MenuItem} with a given index. Supports three rows with a given amount
	 * of items. If itemsPerRow equals 5 then index 0 is the first item in the first row, 
	 * index 5 is the first item in the second row and index 10 is the first item in the third row.
	 * 
	 * @param index the index of the {@code MenuItem} to be pressed
	 * @param itemsPerRow the amount of menu items there are per row.   
	 */

	public void pressMenuItem(int index, int itemsPerRow) {	
		//  Item���棬�洢4�У�ÿ�еĿ�ͷ���
		int[] row = new int[4];
		// ��ʼ��Item id ��Ϣ
		for(int i = 1; i <=3; i++)
			row[i] = itemsPerRow*i;
		// �ȴ�500ms
		sleeper.sleep();
		try{
			// ���Menu��ť
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
			// �ȴ�Menu����
			dialogUtils.waitForDialogToOpen(Timeout.getSmallTimeout(), true);
			// ���2���Ϸ����.Itemλ�ûص���һ��
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
		}catch(SecurityException e){
			Assert.fail("Can not press the menu!");
		}
		// ���ָ��Item�ڵ�һ��,���ڵ�һ���ƶ��������ƶ����ƶ���ָ����Item
		if (index < row[1]) {
			for (int i = 0; i < index; i++) {
				sleeper.sleepMini();
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
		// �ڵڶ���
		} else if (index >= row[1] && index < row[2]) {
			// ���Ƶ���һ�У����ڶ���
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);	
			// �ƶ���ָ����Item
			for (int i = row[1]; i < index; i++) {
				sleeper.sleepMini();
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
			// �ڵ�����,����֮�����
		} else if (index >= row[2]) {
			// �ƶ���������
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);	
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);	
			// �ƶ���ָ����Item
			for (int i = row[2]; i < index; i++) {
				sleeper.sleepMini();
				inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
			}
		}

		try{
			// ���ȷ��
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		}catch (SecurityException ignored) {}
	}
	
	/**
	 * �������̵�ǰ��������һ������
	 * Presses the soft keyboard next button. 
	 */

	public void pressSoftKeyboardNextButton(){
		// ��ȡһ��EditText.ֻ��EditText���������
		final EditText freshestEditText = viewFetcher.getFreshestView(viewFetcher.getCurrentViews(EditText.class));
		// ���Ի�ȡEditText
		if(freshestEditText != null){
			inst.runOnMainSync(new Runnable()
			{
				public void run()
				{
					// �����ǰ��ťλ�õ���һ����ť
					freshestEditText.onEditorAction(EditorInfo.IME_ACTION_NEXT); 
				}
			});
		}
	}

	/**
	 * �����spinnerIndex�� Spinner�ĵ�itemIndex��Item
	 * spinnerIndex     ָ����Spinner˳��
	 * itemIndex        ָ����Item˳��,�������ֵ����ô�����ƶ�����ֵ�����ƶ�
	 * Presses on a {@link android.widget.Spinner} (drop-down menu) item.
	 *
	 * @param spinnerIndex the index of the {@code Spinner} menu to be used
	 * @param itemIndex the index of the {@code Spinner} item to be pressed relative to the currently selected item.
	 * A Negative number moves up on the {@code Spinner}, positive moves down
	 */

	public void pressSpinnerItem(int spinnerIndex, int itemIndex)
	{	
		// ��������б�
		clicker.clickOnScreen(waiter.waitForAndGetView(spinnerIndex, Spinner.class));
		// �ȴ������б����
		dialogUtils.waitForDialogToOpen(Timeout.getSmallTimeout(), true);

		try{
			// �����¼�����ʼ��λ��,������
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		}catch(SecurityException ignored){}
		// ���ָ����itemIndexΪ��ֵ����ô�����ƶ�
		boolean countingUp = true;
		if(itemIndex < 0){
			countingUp = false;
			itemIndex *= -1;
		}
		// ����ָ����˳���ƶ� Item����Ӧ��λ��
		for(int i = 0; i < itemIndex; i++)
		{
			sleeper.sleepMini();
			// ����
			if(countingUp){
				try{
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
				}catch(SecurityException ignored){}
			// ����
			}else{
				try{
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
				}catch(SecurityException ignored){}
			}
		}
		// ���ȷ�ϰ�ť
		try{
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
		}catch(SecurityException ignored){}
	}
}