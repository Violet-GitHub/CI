package com.robotium.solo;


import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * ����������
 * Contains the waitForDialogToClose() method.
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class DialogUtils {
	// activity����������
	private final ActivityUtils activityUtils;
	// view��ȡ������
	private final ViewFetcher viewFetcher;
	// �ȴ�������
	private final Sleeper sleeper;
	// 1s
	private final static int TIMEOUT_DIALOG_TO_CLOSE = 1000;
	// 200ms
	private final int MINISLEEP = 200;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param sleeper the {@code Sleeper} instance
	 */

	public DialogUtils(ActivityUtils activityUtils, ViewFetcher viewFetcher, Sleeper sleeper) {
		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		this.sleeper = sleeper;
	}


	/**
	 * �����ָ��ʱ���ڵ����Ƿ�ر���.
	 * Waits for a {@link android.app.Dialog} to close.
	 *
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if the {@code Dialog} is closed before the timeout and {@code false} if it is not closed
	 */

	public boolean waitForDialogToClose(long timeout) {
		// �ȵȴ��������
		waitForDialogToOpen(TIMEOUT_DIALOG_TO_CLOSE, false);
		// ���ó�ʱʱ��
		final long endTime = SystemClock.uptimeMillis() + timeout;
		// ѭ����鵯���Ƿ�ر���
		while (SystemClock.uptimeMillis() < endTime) {

			if(!isDialogOpen()){
				return true;
			}
			// �ȴ�200ms
			sleeper.sleep(MINISLEEP);
		}
		return false;
	}



	/**
	 * ���ָ��ʱ���ڣ��Ƿ��е�����֣�
	 * timeout    ���õ�ָ����ʱʱ��,��λ ms
	 * sleepFirst �Ƿ���Ҫ�ȵȴ�500ms,�������
	 * Waits for a {@link android.app.Dialog} to open.
	 *
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if the {@code Dialog} is opened before the timeout and {@code false} if it is not opened
	 */

	public boolean waitForDialogToOpen(long timeout, boolean sleepFirst) {
		// ���ó�ʱʱ��
		final long endTime = SystemClock.uptimeMillis() + timeout;
		// �Ƿ���Ҫ�ȴ�500ms���ٲ���
		if(sleepFirst)
			sleeper.sleep();
		// ѭ������Ƿ񵯿������
		while (SystemClock.uptimeMillis() < endTime) {

			if(isDialogOpen()){
				return true;
			}
			// �ȴ�300ms
			sleeper.sleepMini();
		}
		return false;
	}

	/**
	 * ����Ƿ��е������
	 * Checks if a dialog is open. 
	 * 
	 * @return true if dialog is open
	 */

	private boolean isDialogOpen(){
		// ��ȡ��ǰ��ʾ��activity
		final Activity activity = activityUtils.getCurrentActivity(false);
		// ��ȡ��ǰ������DecorView����View
		final View[] views = viewFetcher.getWindowDecorViews();
		// ��ȡ���µ�DecorView,DecorView�Ǹ�
		View view = viewFetcher.getRecentDecorView(views);	
		// ��������Ƿ��д򿪵ĵ���
		if(!isDialog(activity, view)){
			for(View v : views){
				if(isDialog(activity, v)){
					return true;
				}
			}
		}
		else {
			return true;
		}
		return false;
	}
	
	/**
	 * �ж�decorView�Ƿ��Ǹ���activity�ģ�����鵯���Ƿ��ǵ�ǰactivity��
	 * Checks that the specified DecorView and the Activity DecorView are not equal.
	 * 
	 * @param activity the activity which DecorView is to be compared
	 * @param decorView the DecorView to compare
	 * @return true if not equal
	 */
	
	private boolean isDialog(Activity activity, View decorView){
		// ���decorView�Ƕ��ɼ��ģ����ɼ�ֱ�ӷ���false
		if(decorView == null || !decorView.isShown()){
			return false;
		}
		// ��ȡContext
		Context viewContext = null;
		if(decorView != null){
			viewContext = decorView.getContext();
		}
		// ��ȡ��Ҫ��Context
		if (viewContext instanceof ContextThemeWrapper) {
			ContextThemeWrapper ctw = (ContextThemeWrapper) viewContext;
			viewContext = ctw.getBaseContext();
		}
		// ��ȡactivity��Ӧ��Context
		Context activityContext = activity;
		Context activityBaseContext = activity.getBaseContext();
		// ���Context �Ƿ���һ�µ�,���� activity�����ڵ����е�
		return (activityContext.equals(viewContext) || activityBaseContext.equals(viewContext)) && (decorView != activity.getWindow().getDecorView());
	}

	/**
	 * ���������
	 * editText ָ���ı༭��
	 * shouldSleepFirst �Ƿ�Ҫ�ȵȴ�500ms�ٲ���
	 * shouldSleepAfter ִ������Ƿ�Ҫ�ȴ�500ms�ٷ���,���Դ���editText��null��Ч
	 * Hides the soft keyboard
	 * 
	 * @param shouldSleepFirst whether to sleep a default pause first
	 * @param shouldSleepAfter whether to sleep a default pause after
	 */

	public void hideSoftKeyboard(EditText editText, boolean shouldSleepFirst, boolean shouldSleepAfter) {
		// ��ȡ��ǰactivity
		Activity activity = activityUtils.getCurrentActivity(shouldSleepFirst);
		// ��ȡ������ƹ���������
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// ������������̷���
		if(editText != null) {
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			return;
		}
		// ���û��ָ��editText,��ȡ��ǰ�������ڵ�View
		View focusedView = activity.getCurrentFocus();
		// �����ȡ�� View����EditText
		if(!(focusedView instanceof EditText)) {
			// ��ȡ��ǰҳ������� EditText
			EditText freshestEditText = viewFetcher.getFreshestView(viewFetcher.getCurrentViews(EditText.class));
			// �������ȡ��EditText��ô���ÿ��õ�
			if(freshestEditText != null){
				focusedView = freshestEditText;
			}
		}
		// ���������
		if(focusedView != null) {
			inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
		}
		// ��������˵ȴ�����ô�ȴ�500ms�󷵻�
		if(shouldSleepAfter){
			sleeper.sleep();
		}
	}
}