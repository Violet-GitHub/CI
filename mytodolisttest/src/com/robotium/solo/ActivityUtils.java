package com.robotium.solo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.Assert;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;


/**
 * ����Activity�����Ĺ�����
 * Contains activity related methods. Examples are:
 * getCurrentActivity(), getActivityMonitor(), setActivityOrientation(int orientation).
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class ActivityUtils {
	// Instrument �����¼�����ǿ������
	private final Instrumentation inst;
	// activitymonitor ���е�activity�仯�����Լ��
	private ActivityMonitor activityMonitor;
	// ��ͨ�� activity
	private Activity activity;
	// ��������ʱ��,����UIAutomator�Ĵ��룬����SystemClock.sleep()�ˣ��������������
	private final Sleeper sleeper;
	// ��־��ǩ��log ��־��������robotium�ı�ǩ.��ǿ�������ǵ�
	private final String LOG_TAG = "Robotium";
	// �̵ȴ�ʱ��100ms
	private final int MINISLEEP = 100;
	// ����activitymonitorѭ��ץȡ��ǰactivity�ĵȴ�50ms
	private static final int ACTIVITYSYNCTIME = 50;
	// activity��ջ�����ڴ�����п���״̬��activity,����WeakReference,�����GC����Ӱ��
	private Stack<WeakReference<Activity>> activityStack;
	// Activity�������ñ�����ʹ��WeakReference�������GC����Ӱ��
	private WeakReference<Activity> weakActivityReference;
	// ��ջ�洢activity������
	private Stack<String> activitiesStoredInActivityStack;
	// ��ʱ�������ڶ�ʱ��ȡ���µ�activity,��ʱʱ��������涨���50ms
	private Timer activitySyncTimer;
	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param inst the {@code Instrumentation} instance.  ��ȡinstrumentһ�㶼��ͨ��getIntrument()��ȡ�Ĵ��ݸ����캯��
	 * @param activity the start {@code Activity}         Ӧ��������activity,һ���Ǵ���mainActivity
	 * @param sleeper the {@code Sleeper} instance        Sleep������
	 */

	public ActivityUtils(Instrumentation inst, Activity activity, Sleeper sleeper) {
		this.inst = inst;
		this.activity = activity;
		this.sleeper = sleeper;
		createStackAndPushStartActivity();
		activitySyncTimer = new Timer();
		activitiesStoredInActivityStack = new Stack<String>();
		// ���� activity���
		setupActivityMonitor();
		setupActivityStackListener();
	}



	/**
	 * ����һ����ջ�����ڴ�Ŵ�����activity.��Ϊ activity�������µ��ϵľ��ں����ˣ�����ʹ�ö�ջ���Ƚ��������
	 * 
	 * Creates a new activity stack and pushes the start activity. 
	 */

	private void createStackAndPushStartActivity(){
		// ��ʼ��һ����ջ
		activityStack = new Stack<WeakReference<Activity>>();
		// ������캯�������activity��Ϊnull����ô�����ջ��Ϊ��ǰ���µ�activity 
		if (activity != null){
			WeakReference<Activity> weakReference = new WeakReference<Activity>(activity);
			activity = null;
			activityStack.push(weakReference);
		}
	}

	/**
	 * �������д��ڴ򿪻�����״̬��activity,һ������д����һ�� List<Activity>�Ϻ�
	 * Returns a {@code List} of all the opened/active activities.
	 * 
	 * @return a {@code List} of all the opened/active activities
	 */

	public ArrayList<Activity> getAllOpenedActivities()
	{
		// ����һ�� List ���ڷ���  activity����
		ArrayList<Activity> activities = new ArrayList<Activity>();
		// ����activityStack��ջ�е�����activity ���絽List��
		Iterator<WeakReference<Activity>> activityStackIterator = activityStack.iterator();
		// �ж��Ƿ���Լ�������
		while(activityStackIterator.hasNext()){
			// ��ȡ��ǰactivity,��ջָ��ָ���¸�activity����
			Activity  activity = activityStackIterator.next().get();
			// �ж�activity����ǿգ��ż��룬��������gc���¶����Ѿ������գ�����null�쳣
			if(activity!=null)
				activities.add(activity);
		}
		// �������еĵ�ǰ���activity
		return activities;
	}

	/**
	 * ͨ��instrument����һ��activityMonitor���ڼ��activity�Ĵ���
	 * This is were the activityMonitor is set up. The monitor will keep check
	 * for the currently active activity.
	 */

	private void setupActivityMonitor() {

		try {
			// Ϊ��addMonitor������Ҫ������һ��null����
			IntentFilter filter = null;
			// ��ȡһ��activityMonitor
			activityMonitor = inst.addMonitor(filter, null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͨ����ʱ���񲻶�ˢ�»�ȡ��ǰ���´�����activity,��ʱÿ50ms����һ�Σ���˴���һ���ĸ��ʻ�ȡ�Ĳ������µ�activity
	 * This is were the activityStack listener is set up. The listener will keep track of the
	 * opened activities and their positions.
	 */

	private void setupActivityStackListener() {
		// ����һ����ʱ����
		TimerTask activitySyncTimerTask = new TimerTask() {
			@Override
			public void run() {
				// ���activitymonitor�Ƿ��Ѵ���,����null�쳣
				if (activityMonitor != null){
					// ��ȡ��ǰ���µ�activity
					Activity activity = activityMonitor.getLastActivity();
					// ����ȡ�����Ƿ�Ϊnull
					if (activity != null){
						// �����activity�Ѿ��洢��activity��ջ�У��򲻽����ظ����
						if(!activitiesStoredInActivityStack.isEmpty() && activitiesStoredInActivityStack.peek().equals(activity.toString())){
							return;
						}	
						// �Ƴ����ܴ���ͬ�����󣬱����ջ����������
						if (activitiesStoredInActivityStack.remove(activity.toString())){
							removeActivityFromStack(activity);
						}
						// ȷ��activity�����ڴ��״̬���������ջ
						if (!activity.isFinishing()){
							addActivityToStack(activity);
						}
					}
				}
			}
		};
		// ������ʱ����ÿ50msִ��һ��
		activitySyncTimer.schedule(activitySyncTimerTask, 0, ACTIVITYSYNCTIME);
	}

	/**
	 * ��activity��ջ���Ƴ�һ��activity
	 * Removes a given activity from the activity stack
	 * 
	 * @param activity the activity to remove
	 */

	private void removeActivityFromStack(Activity activity){
		// ����������ջ
		Iterator<WeakReference<Activity>> activityStackIterator = activityStack.iterator();
		while(activityStackIterator.hasNext()){
			// ��ȡ��ǰλ�õ�activity
			Activity activityFromWeakReference = activityStackIterator.next().get();
			// ������ֵ�ǰ��ջ�д��� null�������Ƴ�֮
			if(activityFromWeakReference == null){
				activityStackIterator.remove();
			}
			// �Ҷ��˶�Ӧ��activity,���Ƴ�֮
			if(activity!=null && activityFromWeakReference!=null && activityFromWeakReference.equals(activity)){
				activityStackIterator.remove();
			}
		}
	}

	/**
	 * ��ȡActivityMonitor����һ�����Ҳûɶ��
	 * Returns the ActivityMonitor used by Robotium.
	 * 
	 * @return the ActivityMonitor used by Robotium
	 */

	public ActivityMonitor getActivityMonitor(){
		return activityMonitor;
	}

	/**
	 * ������Ļ���򣬺������
	 * Sets the Orientation (Landscape/Portrait) for the current activity.
	 * 
	 * @param orientation An orientation constant such as {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE} or {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
	 */

	public void setActivityOrientation(int orientation)
	{
		Activity activity = getCurrentActivity();
		activity.setRequestedOrientation(orientation);	
	}

	/**
	 * ��ȡ��ǰ��activity��true��ʶ��Ҫ�ȴ�500ms,false��ʶ����Ҫ�ȴ�500ms
	 * Returns the current {@code Activity}, after sleeping a default pause length.
	 *
	 * @param shouldSleepFirst whether to sleep a default pause first
	 * @return the current {@code Activity}
	 */

	public Activity getCurrentActivity(boolean shouldSleepFirst) {
		return getCurrentActivity(shouldSleepFirst, true);
	}

	/**
	 * ��ȡ��ǰactivity,���ҵȴ�500ms
	 * Returns the current {@code Activity}, after sleeping a default pause length.
	 *
	 * @return the current {@code Activity}
	 */

	public Activity getCurrentActivity() {
		return getCurrentActivity(true, true);
	}

	/**
	 * ��activity�����ջ��
	 * Adds an activity to the stack
	 * 
	 * @param activity the activity to add
	 */

	private void addActivityToStack(Activity activity){
		// activity�������ջ
		activitiesStoredInActivityStack.add(activity.toString());
		weakActivityReference = new WeakReference<Activity>(activity);
		activity = null;
		// activity�����ö�������ջ
		activityStack.push(weakActivityReference);
	}

	/**
	 * һֱ�ȴ���֪������ץȡ��һ������activity��δ�ҵ����activity�򲻶ϵ���ѭ�����и��ʵ���������ѭ��
	 * �������޸����һ����ʱʱ�䣬���������޷�ѭ��
	 * Waits for an activity to be started if one is not provided
	 * by the constructor.
	 */

	private final void waitForActivityIfNotAvailable(){
		// �����ǰ��ջ�е�activityΪ��,����ʼ��ʱ�����activityΪnull���ɵ��¸�״̬
		if(activityStack.isEmpty() || activityStack.peek().get() == null){
			// ���ϳ��Ի�ȡ��ǰactivity,ֱ����ȡ��һ������activity
			if (activityMonitor != null) {
				Activity activity = activityMonitor.getLastActivity();
				// �˴����ܵ�������ѭ��
				// activityMonitor��ʼ����Ϊ�õ���ǰactivity.Ӧ����û���´�ҳ�棬���ø÷�������ѭ����
				// ����һ��null��activity������ ��ʼ��֮��û���µ� activity�Ͳ���null,��ѭ����
				while (activity == null){
					// �ȴ�300ms
					sleeper.sleepMini();
					// ��ȡ��ǰactivity
					activity = activityMonitor.getLastActivity();
				}
				// �ǿն�������ջ
				addActivityToStack(activity);
			}
			else{
				// �ȴ�300ms
				sleeper.sleepMini();
				// ��ʼ��activityMonitor
				setupActivityMonitor();
				// ������ȡ���µ�activity
				waitForActivityIfNotAvailable();
			}
		}
	}

	/**
	 * ��ȡ��ǰ���µ�activity,shouldSleepFirstΪtrue,��ô�ȴ�500ms���ڻ�ȡ,
	 * waitForActivityΪtrue��ô���Ի�ȡ���µ�activity,Ϊfalse�򲻳��Ի�ȡ���µģ�ֱ�Ӵ�activity��ջ�л�ȡջ����activity����
	 * 
	 * Returns the current {@code Activity}.
	 *
	 * @param shouldSleepFirst whether to sleep a default pause first
	 * @param waitForActivity whether to wait for the activity
	 * @return the current {@code Activity}
	 */

	public Activity getCurrentActivity(boolean shouldSleepFirst, boolean waitForActivity) {
		// �Ƿ���Ҫ�ȴ�
		if(shouldSleepFirst){
			sleeper.sleep();
		}
		// �Ƿ���Ҫ��ȡ���µ� 
		if(waitForActivity){
			waitForActivityIfNotAvailable();
		}
		// ��ȡ��ջ�е�ջ��activity
		if(!activityStack.isEmpty()){
			activity=activityStack.peek().get();
		}
		return activity;
	}

	/**
	 * ��� activity��ջ�Ƿ�Ϊ��
	 * Check if activity stack is empty.
	 * 
	 * @return true if activity stack is empty
	 */
	
	public boolean isActivityStackEmpty() {
		return activityStack.isEmpty();
	}

	/**
	 * ͨ�����ϴ������ذ�ť���Իص�ָ�����ֵ�activity
	 * Returns to the given {@link Activity}.
	 *
	 * @param name the name of the {@code Activity} to return to, e.g. {@code "MyActivity"}
	 */

	public void goBackToActivity(String name)
	{
		// ��ȡ���д���activity
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();
		boolean found = false;	
		// �������д���activity,���������ָ����activity,��Ϊfalse,�ҵ�Ϊ true
		for(int i = 0; i < activitiesOpened.size(); i++){
			if(activitiesOpened.get(i).getClass().getSimpleName().equals(name)){
				found = true;
				break;
			}
		}
		// ����Ҷ���Ҫ���ص�activity��activity��ջ��.��ô���Ի�����activity
		if(found){
			// �жϵ�ǰactivity�Ƿ�Ϊ��Ҫ���صģ������ǲ��Ϸ��ͷ���ָ�ֱ���ҵ�
			while(!getCurrentActivity().getClass().getSimpleName().equals(name))
			{
				try{
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					// instrument ������ָ����ܵ��µ�exception
				}catch(SecurityException ignored){}	
			}
		}
		// û���ҵ����ӡ�ȹ���־.�����״�
		else{
			for (int i = 0; i < activitiesOpened.size(); i++){
				Log.d(LOG_TAG, "Activity priorly opened: "+ activitiesOpened.get(i).getClass().getSimpleName());
			}
			Assert.fail("No Activity named: '" + name + "' has been priorly opened");
		}
	}

	/**
	 * �ڵ�ǰactivity�а���id��ѯString
	 * Returns a localized string.
	 * 
	 * @param resId the resource ID for the string
	 * @return the localized string
	 */

	public String getString(int resId)
	{
		Activity activity = getCurrentActivity(false);
		return activity.getString(resId);
	}

	/**
	 * solo�������ڽ������ͷ������Դ
	 * Finalizes the solo object.
	 */  

	@Override
	public void finalize() throws Throwable {
		// ֹͣactivity��ض�ʱ����
		activitySyncTimer.cancel();
		try {
			// ����activityMonitor����
			// Remove the monitor added during startup
			if (activityMonitor != null) {
				inst.removeMonitor(activityMonitor);
				activityMonitor = null;
			}
		} catch (Exception ignored) {}
		super.finalize();
	}

	/**
	 * �ر����д���activity
	 * All activites that have been opened are finished.
	 */

	public void finishOpenedActivities(){
		// ֹͣactivity������ʱ����
		// Stops the activityStack listener
		activitySyncTimer.cancel();
		// ��ȡ���д���activity
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();
		// �������д���activity
		// Finish all opened activities
		for (int i = activitiesOpened.size()-1; i >= 0; i--) {
			sleeper.sleep(MINISLEEP);
			finishActivity(activitiesOpened.get(i));
		}
		// �ͷŶ���
		activitiesOpened = null;
		sleeper.sleep(MINISLEEP);
		// Finish the initial activity, pressing Back for good measure
		finishActivity(getCurrentActivity(true, false));
		this.activity = null;
		sleeper.sleepMini();
		// ���2��back��ť�˳�����
		try {
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			sleeper.sleep(MINISLEEP);
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		} catch (Throwable ignored) {
			// Guard against lack of INJECT_EVENT permission
		}
		// ��ն�ջ��Ϣ
		clearActivityStack();
	}

	/**
	 *��ն�ջ��Ϣ
	 * Clears the activity stack.
	 */

	private void clearActivityStack(){
		activityStack.clear();
		activitiesStoredInActivityStack.clear();
	}

	/**
	 * ����activity������������activity��������
	 * Finishes an activity.
	 * 
	 * @param activity the activity to finish
	 */

	private void finishActivity(Activity activity){
		if(activity != null) {
			try{
				activity.finish();
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}

}