package com.robotium.solo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import junit.framework.Assert;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.TextView;

/**
 * ������������࣬���ڴ�����Ļ����¼�
 * Contains various click methods. Examples are: clickOn(),
 * clickOnText(), clickOnScreen().
 *
 * @author Renas Reda, renas.reda@robotium.com
 *
 */

class Clicker {
	// ����������־��ӡ���������Robotium�Ķ���
	private final String LOG_TAG = "Robotium";
	// activity����������
	private final ActivityUtils activityUtils;
	// view���ҹ�����
	private final ViewFetcher viewFetcher;
	// Instrument�����ڷ��͸����¼�
	private final Instrumentation inst;
	// ������Ϣ���͹�����
	private final Sender sender;
	// �ȴ�������
	private final Sleeper sleeper;
	// ���������жϹ�����
	private final Waiter waiter;
	// WebView����������
	private final WebUtils webUtils;
	// ���������������
	private final DialogUtils dialogUtils;
	// 100ms
	private final int MINISLEEP = 100;
	// 200ms
	private final int TIMEOUT = 200;
	// 1.5s
	private final int WAIT_TIME = 1500;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param sender the {@code Sender} instance
	 * @param inst the {@code android.app.Instrumentation} instance
	 * @param sleeper the {@code Sleeper} instance
	 * @param waiter the {@code Waiter} instance
	 * @param webUtils the {@code WebUtils} instance
	 * @param dialogUtils the {@code DialogUtils} instance
	 */

	public Clicker(ActivityUtils activityUtils, ViewFetcher viewFetcher, Sender sender, Instrumentation inst, Sleeper sleeper, Waiter waiter, WebUtils webUtils, DialogUtils dialogUtils) {

		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		this.sender = sender;
		this.inst = inst;
		this.sleeper = sleeper;
		this.waiter = waiter;
		this.webUtils = webUtils;
		this.dialogUtils = dialogUtils;
	}

	/**
	 * �����Ļ�ϵ�һ���ض����꣬������ʧ�ܻ�����10��
	 * x    x����ֵ
	 * y    y����ֵ
	 * Clicks on a given coordinate on the screen.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */

	public void clickOnScreen(float x, float y) {
		// ���ñ��λ����ʶ����¼����ͻ�δ�ɹ�
		boolean successfull = false;
		// �������Լ�����
		int retry = 0;
		// �쳣��Ϣ
		SecurityException ex = null;
		// �����û���ͳɹ������Դ�������10�Σ���ô��������
		while(!successfull && retry < 10) {
			// �������¼�
			long downTime = SystemClock.uptimeMillis();
			long eventTime = SystemClock.uptimeMillis();
			MotionEvent event = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_DOWN, x, y, 0);
			MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_UP, x, y, 0);
			try{
				// ���͵���¼�
				inst.sendPointerSync(event);
				inst.sendPointerSync(event2);
				// �¼�����δ���쳣������Ϊ�ɹ�
				successfull = true;
				// �ȴ�100ms
				sleeper.sleep(MINISLEEP);
			}catch(SecurityException e){
				ex = e;
				// �رտ��ܵ����쳣�������Ӱ�죬��������̣���������
				dialogUtils.hideSoftKeyboard(null, false, true);
				retry++;
			}
		}
		// ����ﵽ10�λ�δ�ɹ����������ԣ���¼�쳣��־���˳�
		if(!successfull) {
			Assert.fail("Click at ("+x+", "+y+") can not be completed! ("+(ex != null ? ex.getClass().getName()+": "+ex.getMessage() : "null")+")");
		}
	}

	/**
	 * ����һ�������¼��������ó���ʱ��
	 * x    x����
	 * y    y����
	 * time ����ʱ�䣬��λms
	 * Long clicks a given coordinate on the screen.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param time the amount of time to long click
	 */

	public void clickLongOnScreen(float x, float y, int time) {
		// ���ñ��λ����ʶ����¼����ͻ�δ�ɹ�
		boolean successfull = false;
		// �������Լ�����
		int retry = 0;
		// �쳣��Ϣ
		SecurityException ex = null;
		// ���찴���¼�
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
		// ����¼�����ʧ�ܣ���������10�Σ����������
		while(!successfull && retry < 10) {
			try{
				inst.sendPointerSync(event);
				// �¼����ͳɹ������λ����Ϊtrue
				successfull = true;
			}catch(SecurityException e){
				ex = e;
				// �رտ��ܵ����쳣�������Ӱ�죬��������̣���������
				dialogUtils.hideSoftKeyboard(null, false, true);
				retry++;
			}
		}
		// ����ﵽ10�λ�δ�ɹ����������ԣ���¼�쳣��־���˳�
		if(!successfull) {
			Assert.fail("Long click at ("+x+", "+y+") can not be completed! ("+(ex != null ? ex.getClass().getName()+": "+ex.getMessage() : "null")+")");
		}
		// ����һ���ƶ��¼�,���ԭ�Ȱ������껬��1������
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x + 1.0f, y + 1.0f, 0);
		inst.sendPointerSync(event);
		// ��������˳����¼�����ʱ�����0����ȴ���Ӧ��ʱ��
		if(time > 0)
			sleeper.sleep(time);
		// ������õ�ֵС�ڵ���0,��ôʹ��Ĭ�ϳ���ʱ���2.5��
		else
			sleeper.sleep((int)(ViewConfiguration.getLongPressTimeout() * 2.5f));
		// �����ɿ��¼�
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
		inst.sendPointerSync(event);
		// �ȴ�500ms
		sleeper.sleep();
	}


	/**
	 * ���ָ����view
	 * Clicks on a given {@link View}.
	 *
	 * @param view the view that should be clicked
	 */

	public void clickOnScreen(View view) {
		// ����Ϊ�ǳ������
		clickOnScreen(view, false, 0);
	}

	/**
	 * ���������view,�������Ƿ񳤵���������ó���ʱ��
	 * view      ��Ҫ�����view
	 * longClick �Ƿ񳤰�
	 * time      ��Ҫ������ʱ��
	 * Private method used to click on a given view.
	 *
	 * @param view the view that should be clicked
	 * @param longClick true if the click should be a long click
	 * @param time the amount of time to long click
	 */

	public void clickOnScreen(View view, boolean longClick, int time) {
		// �������null�����������쳣��ʾ
		if(view == null)
			Assert.fail("View is null and can therefore not be clicked!");
		// ��ȡview��xy����
		float[] xyToClick = getClickCoordinates(view);
		// ��ȡx����
		float x = xyToClick[0];
		// ��ȡy����
		float y = xyToClick[1];
		// �����ȡ��xy�������0��ô���²��ң������ҵ������ɵ����view
		if(x == 0 || y == 0){
			// �ȴ�300ms
			sleeper.sleepMini();
			try {
				view = getIdenticalView(view);
			} catch (Exception ignored){}
			// ��������ҵ�����ô���»�ȡһ������
			if(view != null){
				xyToClick = getClickCoordinates(view);
				x = xyToClick[0];
				y = xyToClick[1];
			}
		}
		// ��������˳�������ô���ͳ������
		if (longClick)
			clickLongOnScreen(x, y, time);
		// ���͵������
		else
			clickOnScreen(x, y);
	}

	/**
	 * ���ո�����view,��ȡ��ǰҳ��չʾ��ͬ��view,����Ҳ����ͷ���null
	 * Returns an identical View to the one specified.
	 * 
	 * @param view the view to find
	 * @return identical view of the specified view
	 */
	
	private View getIdenticalView(View view) {
		View viewToReturn = null;
		// ������ͬ���͵�view
		List<? extends View> visibleViews = RobotiumUtils.removeInvisibleViews(viewFetcher.getCurrentViews(view.getClass()));
		// ������ͬid��view
		for(View v : visibleViews){
			if(v.getId() == view.getId()){
				// �ж�2��view�Ƿ���һ�µ�mParents
				if(isParentsEqual(v, view)){
					viewToReturn = v;
					break;
				}
			}
		}
		return viewToReturn;
	}
	
	/**
	 * �Ƚ�2�� view��mParents�����Ƿ�һ��
	 * Compares the parent views of the specified views.
	 * 
	 * @param firstView the first view
	 * @param secondView the second view
	 * @return true if parents of the specified views are equal
	 */
	
	private boolean isParentsEqual(View firstView, View secondView){
		// ���id�������Ͳ�һ�£���ôֱ�ӷ���false
		if(firstView.getId() != secondView.getId() || !firstView.getClass().isAssignableFrom(secondView.getClass())){
			return false;
		}
		// �ж�mParent�Ƿ�һ�£�һ�·���true,��һ�·���false
		if (firstView.getParent() != null && firstView.getParent() instanceof View && 
				secondView.getParent() != null && secondView.getParent() instanceof View) {

			return isParentsEqual((View) firstView.getParent(), (View) secondView.getParent());
		} else {
			return true;
		}
	}

	

	/**
	 * ��ȡView������м�λ������
	 * view ��Ҫ�����view
	 * Returns click coordinates for the specified view.
	 * 
	 * @param view the view to get click coordinates from
	 * @return click coordinates for a specified view
	 */

	private float[] getClickCoordinates(View view){
		// �洢view�ĸ߶ȺͿ��
		int[] xyLocation = new int[2];
		// �洢view��xy���꣬���½�����ֵ
		float[] xyToClick = new float[2];
		// ��ȡ���½�����ֵ
		view.getLocationOnScreen(xyLocation);
		// ��ȡ���
		final int viewWidth = view.getWidth();
		// ��ȡ�߶�
		final int viewHeight = view.getHeight();
		// �����м��x����
		final float x = xyLocation[0] + (viewWidth / 2.0f);
		// �����м��y����
		float y = xyLocation[1] + (viewHeight / 2.0f);

		xyToClick[0] = x;
		xyToClick[1] = y;

		return xyToClick;
	}


	/**
	 * ����ָ��text���ݵĵ�1��View,�ȴ�������֣����� ����index�Σ��ٵ���س���.ȷ��
	 * Long clicks on a specific {@link TextView} and then selects
	 * an item from the context menu that appears. Will automatically scroll when needed.
	 *
	 * @param text the text that should be clicked on. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param index the index of the menu item that should be pressed
	 */

	public void clickLongOnTextAndPress(String text, int index)
	{
		// �������ָ�� text��View
		clickOnText(text, true, 0, true, 0);
		// �ȴ��������
		dialogUtils.waitForDialogToOpen(Timeout.getSmallTimeout(), true);
		try{
			// �������� ���� �¼�
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		}catch(SecurityException e){
			Assert.fail("Can not press the context menu!");
		}
		// ����ָ������������ ����
		for(int i = 0; i < index; i++)
		{	// �ȴ�300ms
			sleeper.sleepMini();
			// �������� �����¼�
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		// ����ȷ�� �¼�
		inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
	}

	/**
	 * �򿪲˵�
	 * Opens the menu and waits for it to open.
	 */

	private void openMenu(){
		// �ȴ�300ms
		sleeper.sleepMini();
		// ���200ms��δ���ֲ˵�����
		if(!dialogUtils.waitForDialogToOpen(TIMEOUT, false)) {
			try{
				// ���Ͳ˵���ť
				sender.sendKeyCode(KeyEvent.KEYCODE_MENU);
				// �ȴ��˵�����
				dialogUtils.waitForDialogToOpen(WAIT_TIME, true);
			}catch(SecurityException e){
				Assert.fail("Can not open the menu!");
			}
		}
	}

	/**
	 * ����˵���ָ���� text
	 * Clicks on a menu item with a given text.
	 *
	 * @param text the menu text that should be clicked on. The parameter <strong>will</strong> be interpreted as a regular expression.
	 */

	public void clickOnMenuItem(String text)
	{	// �򿪲˵�
		openMenu();
		// ���ָ��text���ݵ���
		clickOnText(text, false, 1, true, 0);
	}

	/**
	 * ����˵���ָ��text���ݵ���������Ƿ����˵�δ���ּ��������Ӽ��˵�Ŀ¼
	 * text    ָ�����ı�����
	 * subMenu �Ƿ����Ӽ��˵�
	 * Clicks on a menu item with a given text.
	 *
	 * @param text the menu text that should be clicked on. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param subMenu true if the menu item could be located in a sub menu
	 */

	public void clickOnMenuItem(String text, boolean subMenu)
	{
		// �ȴ�300ms
		sleeper.sleepMini();

		TextView textMore = null;
		// ����xy��ߴ洢����
		int [] xy = new int[2];
		int x = 0;
		int y = 0;
		// ���˵��Ƿ��Ѵ򿪣�δ����򿪲˵�,��ʱ200ms
		if(!dialogUtils.waitForDialogToOpen(TIMEOUT, false)) {
			try{
				// ���ʹ򿪲˵��¼�
				sender.sendKeyCode(KeyEvent.KEYCODE_MENU);
				// �ȴ���,��ʱ1.5s
				dialogUtils.waitForDialogToOpen(WAIT_TIME, true);
			}catch(SecurityException e){
				Assert.fail("Can not open the menu!");
			}
		}
		// ���ָ�������ݲ˵��Ƿ����
		boolean textShown = waiter.waitForText(text, 1, WAIT_TIME, true) != null;
		// ����������Ӽ�Ŀ¼����ô���������Ӽ��˵�,����עָ�����ݣ�ֻ������������5,��ô�ҳ����ұߵĲ˵����
		if(subMenu && (viewFetcher.getCurrentViews(TextView.class).size() > 5) && !textShown){
			// ��������λ�õĲ˵�����
			for(TextView textView : viewFetcher.getCurrentViews(TextView.class)){
				x = xy[0];
				y = xy[1];
				textView.getLocationOnScreen(xy);

				if(xy[0] > x || xy[1] > y)
					textMore = textView;
			}
		}
		// ����ҵ�����ô���͵���¼�
		if(textMore != null)
			clickOnScreen(textMore);
		// �˵���δ�ҵ�������չʾ�������ؼ��У����Է��͵���¼��������û������api,�²��û���ͼ����������
		clickOnText(text, false, 1, true, 0);
	}

	/**
	 * ���actionbar ����id����
	 * Clicks on an ActionBar item with a given resource id
	 *
	 * @param resourceId the R.id of the ActionBar item
	 */

	public void clickOnActionBarItem(int resourceId){
		// ���͵���¼�
		inst.invokeMenuActionSync(activityUtils.getCurrentActivity(), resourceId, 0);
	}

	/**
	 * ��� ActionBar�� home��up
	 * Clicks on an ActionBar Home/Up button.
	 */

	public void clickOnActionBarHomeButton() {
		// ��ȡ��ǰ��activity
		Activity activity = activityUtils.getCurrentActivity();
		MenuItem homeMenuItem = null;

		try {
			// ͨ�����䣬����MenuItem����
			Class<?> cls = Class.forName("com.android.internal.view.menu.ActionMenuItem");
			Class<?> partypes[] = new Class[6];
			partypes[0] = Context.class;
			partypes[1] = Integer.TYPE;
			partypes[2] = Integer.TYPE;
			partypes[3] = Integer.TYPE;
			partypes[4] = Integer.TYPE;
			partypes[5] = CharSequence.class;
			Constructor<?> ct = cls.getConstructor(partypes);
			Object argList[] = new Object[6];
			argList[0] = activity;
			argList[1] = 0;
			argList[2] = android.R.id.home;
			argList[3] = 0;
			argList[4] = 0;
			argList[5] = "";
			// ����ActionBar��home
			homeMenuItem = (MenuItem) ct.newInstance(argList);
		} catch (Exception ex) {
			Log.d(LOG_TAG, "Can not find methods to invoke Home button!");
		}

		if (homeMenuItem != null) {
			try{
				// ����home�¼�
				activity.getWindow().getCallback().onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, homeMenuItem);
			}catch(Exception ignored) {}
		}
	}

	/**
	 * ���ָ�������ĵ�match��WebElement,���������Ƿ�ʹ��js���͵���¼�.���ý��㵽��Ӧ��WebElement
	 * by     					 ������������
	 * match   					������index
	 * scroll  					�Ƿ���Ҫ�϶�ˢ�²���
	 * useJavaScriptToClick     �Ƿ�ʹ��js���͵���¼�
	 * Clicks on a web element using the given By method.
	 *
	 * @param by the By object e.g. By.id("id");
	 * @param match if multiple objects match, this determines which one will be clicked
	 * @param scroll true if scrolling should be performed
	 * @param useJavaScriptToClick true if click should be perfomed through JavaScript
	 */

	public void clickOnWebElement(By by, int match, boolean scroll, boolean useJavaScriptToClick){
		WebElement webElement = null;
		// ���������js�����ô����js���
		if(useJavaScriptToClick){
			// ����ָ����webElement
			webElement = waiter.waitForWebElement(by, match, Timeout.getSmallTimeout(), false);
			// δ�ҵ�����ʾ�쳣
			if(webElement == null){
				Assert.fail("WebElement with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' is not found!");
			}
			// ����js���WebElement
			webUtils.executeJavaScript(by, true);
			return;
		}
		// ����ָ����WebElement
		WebElement webElementToClick = waiter.waitForWebElement(by, match, Timeout.getSmallTimeout(), scroll);
		// Ϊ�ҵ���ʾ�쳣
		if(webElementToClick == null){
			if(match > 1) {
				Assert.fail(match + " WebElements with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' are not found!");
			}
			else {
				Assert.fail("WebElement with " + webUtils.splitNameByUpperCase(by.getClass().getSimpleName()) + ": '" + by.getValue() + "' is not found!");
			}
		}
		// �ҵ�ͨ��instrument���͵���¼�
		clickOnScreen(webElementToClick.getLocationX(), webElementToClick.getLocationY());
	}


	/**
	 * ���ָ�������ݵ�TextView�ؼ�,�������Ƿ񳤰�������ʱ�䣬��Ҫ����������ĵڼ���
	 * regex      ���õ��ı�����
	 * longClick  �Ƿ񳤰�
	 * match      �ڼ���
	 * scroll     �Ƿ������϶�ˢ�£����б�֮��Ŀؼ����϶�����ˢ������
	 * time       ����ʱ��
	 * Clicks on a specific {@link TextView} displaying a given text.
	 *
	 * @param regex the text that should be clicked on. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param longClick {@code true} if the click should be a long click
	 * @param match the regex match that should be clicked on
	 * @param scroll true if scrolling should be performed
	 * @param time the amount of time to long click
	 */

	public void clickOnText(String regex, boolean longClick, int match, boolean scroll, int time) {
		// ��ȡָ��������TextView
		TextView textToClick = waiter.waitForText(regex, match, Timeout.getSmallTimeout(), scroll, true, false);
		// ����ҵ���Ӧ  TextView��������ص���¼�
		if (textToClick != null) {
			clickOnScreen(textToClick, longClick, time);
		}
		// ���û�ҵ�
		else {
			// ������match ����1����ô��ʾ�쳣��Ϣ,���˳�
			if(match > 1){
				Assert.fail(match + " matches of text string: '" + regex +  "' are not found!");
			}
			// ������õ�С�ڵ���1,��ӡ����ǰ���еĵ�ǰ����TextView��ؼ���Ϣ,���˳�
			else{
				ArrayList<TextView> allTextViews = RobotiumUtils.removeInvisibleViews(viewFetcher.getCurrentViews(TextView.class));
				allTextViews.addAll((Collection<? extends TextView>) webUtils.getTextViewsFromWebView());

				for (TextView textView : allTextViews) {
					Log.d(LOG_TAG, "'" + regex + "' not found. Have found: '" + textView.getText() + "'");
				}
				allTextViews = null;
				Assert.fail("Text string: '" + regex + "' is not found!");
			}
		}
	}


	/**
	 * ���ָ�����ͺ��ı����ݵ�TextView
	 * Clicks on a {@code View} of a specific class, with a given text.
	 *
	 * @param viewClass what kind of {@code View} to click, e.g. {@code Button.class} or {@code TextView.class}
	 * @param nameRegex the name of the view presented to the user. The parameter <strong>will</strong> be interpreted as a regular expression.
	 */

	public <T extends TextView> void clickOn(Class<T> viewClass, String nameRegex) {
		// ����ָ�����͵�view
		T viewToClick = (T) waiter.waitForText(viewClass, nameRegex, 0, Timeout.getSmallTimeout(), true, true, false);
		// �ҵ��ˣ����͵���¼�
		if (viewToClick != null) {
			clickOnScreen(viewToClick);
			// δ�ҵ�����ӡ��־����¼��ǰ���е�TextView,���˳�
		} else {
			ArrayList <T> allTextViews = RobotiumUtils.removeInvisibleViews(viewFetcher.getCurrentViews(viewClass));

			for (T view : allTextViews) {
				Log.d(LOG_TAG, "'" + nameRegex + "' not found. Have found: '" + view.getText() + "'");
			}
			Assert.fail(viewClass.getSimpleName() + " with text: '" + nameRegex + "' is not found!");
		}
	}

	/**
	 * ���ָ�����͵ĵ�index��View
	 * Clicks on a {@code View} of a specific class, with a certain index.
	 *
	 * @param viewClass what kind of {@code View} to click, e.g. {@code Button.class} or {@code ImageView.class}
	 * @param index the index of the {@code View} to be clicked, within {@code View}s of the specified class
	 */

	public <T extends View> void clickOn(Class<T> viewClass, int index) {
		// ���ָ��������view
		clickOnScreen(waiter.waitForAndGetView(index, viewClass));
	}


	/**
	 *	����ҵ��ĵ�1���б�ĵ�line�У������ش����е�����TextView���͵�View
	 * Clicks on a certain list line and returns the {@link TextView}s that
	 * the list line is showing. Will use the first list it finds.
	 *
	 * @param line the line that should be clicked
	 * @return a {@code List} of the {@code TextView}s located in the list line
	 */

	public ArrayList<TextView> clickInList(int line) {
		return clickInList(line, 0, false, 0);
	}

	/**
	 * ���ָ���ĵ�index���б�ĵ�line�У��������Ƿ񳤰�,�����ش����е�����TextView���͵�View
	 * line      ָ������
	 * index     ָ�����б�
	 * longClick �Ƿ񳤰�
	 * time      ����ʱ��
	 * Clicks on a certain list line on a specified List and
	 * returns the {@link TextView}s that the list line is showing.
	 *
	 * @param line the line that should be clicked
	 * @param index the index of the list. E.g. Index 1 if two lists are available
	 * @return an {@code ArrayList} of the {@code TextView}s located in the list line
	 */

	public ArrayList<TextView> clickInList(int line, int index, boolean longClick, int time) {
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + Timeout.getSmallTimeout();
		// ����index,�������0��ʼ����˼�1
		int lineIndex = line - 1;
	    // �쳣���������0
		if(lineIndex < 0)
			lineIndex = 0;
		// ��ȡָ���ĵ�index���б�
		ArrayList<View> views = new ArrayList<View>();
		final AbsListView absListView = waiter.waitForAndGetView(index, AbsListView.class);
		// δ�ҵ�����ʾ�쳣
		if(absListView == null)
			Assert.fail("ListView is null!");
		// ������õ�index�����б��е����ݣ���δ��ʱ����ô�������ԣ������б������ݲ������ӵ����
		while(lineIndex > absListView.getChildCount()){
			// ����Ƿ�ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// ��ʱ��ʾ�쳣
			if (timedOut){
				int numberOfLines = absListView.getChildCount();
				Assert.fail("Can not click on line number " + line + " as there are only " + numberOfLines + " lines available");
			}
			// �ȴ�500ms
			sleeper.sleep();
		}
		// �ҵ��б���ָ������
		View view = getViewOnListLine(absListView, lineIndex);
		// �ҵ�
		if(view != null){
			// ��ȡ����ָ��������View
			views = viewFetcher.getViews(view, true);
			// �޳����ɼ���
			views = RobotiumUtils.removeInvisibleViews(views);
			// ������Ӧ�� view
			clickOnScreen(view, longClick, time);
		}
		// ���˵����з�TextView���͵�
		return RobotiumUtils.filterViews(TextView.class, views);
	}

	/**
	 * ��ȡ�б�ָ���е�view
	 * absListView   �������б�
	 * lineIndex     ָ������
	 * Returns the view in the specified list line
	 * 
	 * @param absListView the ListView to use
	 * @param lineIndex the line index of the View
	 * @return the View located at a specified list line
	 */

	private View getViewOnListLine(AbsListView absListView, int lineIndex){
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + Timeout.getSmallTimeout();
		// ��ȡָ���е� View
		View view = absListView.getChildAt(lineIndex);
		// ��ȡ����,��δ��ʱ����������
		while(view == null){
			// ����Ƿ�ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// ��ʱ��ʾ�쳣
			if (timedOut){
				Assert.fail("View is null and can therefore not be clicked!");
			}
			// ��500ms
			sleeper.sleep();
			// ���Ի�ȡview
			view = absListView.getChildAt(lineIndex);
		}
		return view;
	}
}