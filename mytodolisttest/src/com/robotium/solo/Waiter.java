package com.robotium.solo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import android.app.Activity;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


/**
 * View��ȡ�����࣬���ڵȴ�������Ϣ����
 * Contains various wait methods. Examples are: waitForText(),
 * waitForView().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Waiter {
	// activity����������
	private final ActivityUtils activityUtils;
	// View���ҹ�����
	private final ViewFetcher viewFetcher;
	// �ؼ����ҹ�����
	private final Searcher searcher;
	// ������ռ����������
	private final Scroller scroller;
	// ��ʱ�ȴ�������
	private final Sleeper sleeper;
	// ��ʱ50ms
	private final int MINISLEEP = 50;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param searcher the {@code Searcher} instance
	 * @param scroller the {@code Scroller} instance
	 * @param sleeper the {@code Sleeper} instance
	 */

	public Waiter(ActivityUtils activityUtils, ViewFetcher viewFetcher, Searcher searcher, Scroller scroller, Sleeper sleeper){
		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		this.searcher = searcher;
		this.scroller = scroller;
		this.sleeper = sleeper;
	}

	/**
	 * �ȴ�ָ�����ֵ�activity����,Ĭ�ϳ�ʱʱ��10s
	 * ��ʱ��δ���ַ���false,10s�ڳ����򷵻�true
	 * Waits for the given {@link Activity}.
	 *
	 * @param name the name of the {@code Activity} to wait for e.g. {@code "MyActivity"}
	 * @return {@code true} if {@code Activity} appears before the timeout and {@code false} if it does not
	 *
	 */

	public boolean waitForActivity(String name){
		// �ȴ�ָ�����ֵ�Activity���֣���ʱʱ��Ĭ��Ϊ10s
		return waitForActivity(name, Timeout.getSmallTimeout());
	}

	/**
	 * �ȴ��������ֵ�activity��ָ��ʱ���ڳ���,���δ���ַ���false,���ַ���true
	 * Waits for the given {@link Activity}.
	 *
	 * @param name the name of the {@code Activity} to wait for e.g. {@code "MyActivity"}
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if {@code Activity} appears before the timeout and {@code false} if it does not
	 *
	 */

	public boolean waitForActivity(String name, int timeout){
		// ��ȡ��ǰ���µ�activity
		Activity currentActivity = activityUtils.getCurrentActivity(false, false);
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;
		// �������� activityδ���֣�δ����ʱʱ��㣬�����ˢ���ж�
		while(SystemClock.uptimeMillis() < endTime){
			// �жϵ�ǰ activity�Ƿ�Ϊָ�����ֵģ��ҵ����˳����ң�����true,δ�ҵ������ˢ�²���
			if(currentActivity != null && currentActivity.getClass().getSimpleName().equals(name)) {
				return true;
			}
			// �ȴ�50ms
			sleeper.sleep(MINISLEEP);
			// ������ȡ
			currentActivity = activityUtils.getCurrentActivity(false, false);
		}
		// ������ʱ�㣬����false
		return false;
	}
	
	/**
	 * �ȴ�ָ��class���͵�activity���֣�Ĭ�ϳ�ʱ10s,����10s��δ���ַ���false,���ַ���true
	 * Waits for the given {@link Activity}.
	 *
	 * @param activityClass the class of the {@code Activity} to wait for
	 * @return {@code true} if {@code Activity} appears before the timeout and {@code false} if it does not
	 *
	 */

	public boolean waitForActivity(Class<? extends Activity> activityClass){
		// ���ó�ʱ10s,�ȴ�activity����
		return waitForActivity(activityClass, Timeout.getSmallTimeout());
	}

	/**
	 * �ȴ� ָ��class���͵�activity����
	 * timeout Ϊ��ʱʱ�䣬�ڳ�ʱʱ���ڳ��ַ���true,δ���ַ���false
	 * Waits for the given {@link Activity}.
	 *
	 * @param activityClass the class of the {@code Activity} to wait for
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if {@code Activity} appears before the timeout and {@code false} if it does not
	 *
	 */

	public boolean waitForActivity(Class<? extends Activity> activityClass, int timeout){
		// ��ȡ��ǰ����activity
		Activity currentActivity = activityUtils.getCurrentActivity(false, false);
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;
		// δ�ҵ�����δ���ﳬʱʱ��㣬��������
		while(SystemClock.uptimeMillis() < endTime){
			// ����Ƿ�Ϊָ��class���͵ģ��ҵ����˳����ҷ���true
			if(currentActivity != null && currentActivity.getClass().equals(activityClass)) {
				return true;
			}
			// �ȴ�50ms
			sleeper.sleep(MINISLEEP);
			// ˢ�µ�ǰ���µ�activity,��������
			currentActivity = activityUtils.getCurrentActivity(false, false);
		}
		// ��ʱʱ�����δ�ҵ�������false
		return false;
	}

	/**
	 * �ȴ�ָ�����͵� view�Ƿ����,���ַ���true,δ���ַ���false.�÷���δ����ʱ�������׵�����ѭ��������ʹ�ô���ʱ������
	 * viewClass  view�� class����
	 * index      ���������͵�����
	 * sleep      true �ȴ�500ms�����,false  ��������
	 * scroll     true ���ڿɻ����ؼ���δ�ҵ�ʱ������ˢ�����ݣ�false  ������
	 * Waits for a view to be shown.
	 * 
	 * @param viewClass the {@code View} class to wait for
	 * @param index the index of the view that is expected to be shown
	 * @param sleep true if should sleep
	 * @param scroll {@code true} if scrolling should be performed
	 * @return {@code true} if view is shown and {@code false} if it is not shown before the timeout
	 */

	public <T extends View> boolean waitForView(final Class<T> viewClass, final int index, boolean sleep, boolean scroll){
		// ��ʱviews����
		Set<T> uniqueViews = new HashSet<T>();
		boolean foundMatchingView;
		// ��������� scroll Ϊtrue ֱ�ӵ��øķ������޷��ҵ������׽�����ѭ��
		while(true){
			// true,�ȴ�500ms.false ���ȴ�
			if(sleep)
				sleeper.sleep();
			// ���ò�ѯ�����Ƿ���Լ�����,δ����Ϊ false������Ϊtrue
			foundMatchingView = searcher.searchFor(uniqueViews, viewClass, index);
			// ��������,����true
			if(foundMatchingView)
				return true;
			// ��������Ҫ�����������ò��ɻ��� ����false
			if(scroll && !scroller.scrollDown())
				return false;
			// ������ɻ���������false
			if(!scroll)
				return false;
		}
	}

	/**
	 * ����ʱ�����ģ��ȴ� view���ͳ��ַ���
	 * Waits for a view to be shown.
	 * 
	 * @param viewClass the {@code View} class to wait for
	 * @param index the index of the view that is expected to be shown. 
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @return {@code true} if view is shown and {@code false} if it is not shown before the timeout
	 */

	public <T extends View> boolean waitForView(final Class<T> viewClass, final int index, final int timeout, final boolean scroll){
		// ��ʱviews����
		Set<T> uniqueViews = new HashSet<T>();
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;
		boolean foundMatchingView;
		// δ�ҵ�ָ��������views,��δ��ʱ��������
		while (SystemClock.uptimeMillis() < endTime) {
			// �ȴ�500ms
			sleeper.sleep();
			// ��������Ƿ����㣬����Ϊtrue,������Ϊfalse
			foundMatchingView =  searcher.searchFor(uniqueViews, viewClass, index);
			// �����������˳���飬����true
			if(foundMatchingView)
				return true;
			// ��������˿��϶�����ôˢ�¿��϶��ؼ�
			if(scroll) 
				scroller.scrollDown();
		}
		// ���������㣬����false
		return false;
	}



	/**
	 * �ȴ�һ��class�����е���һclass���͵�view����,��ʱΪ10s.
	 * 10s��������ɷ���true,Ϊ��ɷ��� false
	 * scrollMethod  true ����scroller.scroll(Scroller.DOWN), false ���� scroller.scrollDown()
	 * Waits for two views to be shown.
	 *
	 * @param scrollMethod {@code true} if it's a method used for scrolling
	 * @param classes the classes to wait for 
	 * @return {@code true} if any of the views are shown and {@code false} if none of the views are shown before the timeout
	 */

	public <T extends View> boolean  waitForViews(boolean scrollMethod, Class<? extends T>... classes) {
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + Timeout.getSmallTimeout();
		// ����δ���㣬δ�ﵽ��ʱʱ�䵽���������
		while (SystemClock.uptimeMillis() < endTime) {
			// ����Ƿ������а�������һclass���ͳ���,�������˳���飬����true
			for (Class<? extends T> classToWaitFor : classes) {
				if (waitForView(classToWaitFor, 0, false, false)) {
					return true;
				}
			}
			// �������õ��ö�Ӧ�ķ���
			if(scrollMethod){
				scroller.scroll(Scroller.DOWN);
			}
			else {
				scroller.scrollDown();
			}
			// �ȴ�500ms
			sleeper.sleep();
		}
		// ����δ���㣬����false
		return false;
	}

	/**
	 * �ȴ�ָ����view���֣�Ĭ�ϳ�ʱ20s
	 * Waits for a given view. Default timeout is 20 seconds.
	 * 
	 * @param view the view to wait for
	 * @return {@code true} if view is shown and {@code false} if it is not shown before the timeout
	 */

	public boolean waitForView(View view){
		// �ȴ�ָ����view����,��ʱ����20s,���϶���view����Ⱦ
		return waitForView(view, Timeout.getLargeTimeout(), true, true);
	}

	/**
	 * �ȴ�һ��ָ����view���֣������ó�ʱʱ��
	 * timeout  ��ʱʱ�䣬��λ ms
	 * Waits for a given view. 
	 * 
	 * @param view the view to wait for
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if view is shown and {@code false} if it is not shown before the timeout
	 */

	public boolean waitForView(View view, int timeout){
		// �ȴ�ָ����view����,���õĳ�ʱʱ��,���϶���������Ƿ�shown
		return waitForView(view, timeout, true, false);
	}

	/**
	 * �ȴ�ָ����view����
	 * view         ָ����view
	 * timeout      ��ʱʱ��,��λ ms
	 * scroll       true��Ҫ�϶�ˢ�¿��϶��ؼ�,false ���϶�
 	 * checkIsShown true ����view.isShown()����Ƿ�Ϊtrue ,false  ������
	 * Waits for a given view.
	 * 
	 * @param view the view to wait for
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @param checkIsShown {@code true} if view.isShown() should be used
	 * @return {@code true} if view is shown and {@code false} if it is not shown before the timeout
	 */

	public boolean waitForView(View view, int timeout, boolean scroll, boolean checkIsShown){
		// ����Ϊnullֱ�ӷ���false
		if(view == null)
			return false;
		// ���ó�ʱʱ���
		long endTime = SystemClock.uptimeMillis() + timeout;
		// δ����ʱʱ��㣬����δ���㣬�������
		while (SystemClock.uptimeMillis() < endTime) {
			// �ȴ�500ms
			sleeper.sleep();
			// ���view�Ƿ�����ڵ�ǰ��Ļ
			final boolean foundAnyMatchingView = searcher.searchFor(view);
			// ���ַ���true
			if (foundAnyMatchingView){
				return true;
			}
			// ��δ���ּ�������Ƿ����ñ�ˢ���ˣ�ͨ��isShown() Ϊtrue ˵������ʱû��Ⱦ���������������ˢ�³�����
			else if(checkIsShown && view != null && view.isShown()){
				return true;
			}
			// �������϶���������϶�����ˢ�¿��϶��ؼ�
			if(scroll) 
				scroller.scrollDown();
		}
		// ����δ���㷵��false
		return false;
	}
	
	/**
	 * ��ȡָ��id��ָ��������view���֣������ó�ʱʱ��,�����ʱʱ������Ϊ0,��Ĭ�ϸĳ�10s,����Ϊ��ֵ��ֱ�ӷ���null
	 * Waits for a certain view.
	 * 
	 * @param view the id of the view to wait for
	 * @param index the index of the {@link View}. {@code 0} if only one is available
	 * @param timeout the timeout in milliseconds
	 * @return the specified View
	 */

	public View waitForView(int id, int index, int timeout){
		// �����ʱʱ������Ϊ0,��Ĭ���޸�Ϊ10s
		if(timeout == 0){
			timeout = Timeout.getSmallTimeout();
		}
		// ָ��id,��������ʱ�����϶�
		return waitForView(id, index, timeout, false);
	}

	/**
	 * ��ȡָ��idָ��index��view����.�����ó�ʱ���Ƿ���϶�ˢ��
	 * Waits for a certain view.
	 * 
	 * @param view the id of the view to wait for
	 * @param index the index of the {@link View}. {@code 0} if only one is available
	 * @return the specified View
	 */

	public View waitForView(int id, int index, int timeout, boolean scroll){
		// ��ʱviews����
		Set<View> uniqueViewsMatchingId = new HashSet<View>();
		// ���ó�ʱʱ���
		long endTime = SystemClock.uptimeMillis() + timeout;
		// ����δ���㣬δ�ﵽ��ʱʱ��㣬�������
		while (SystemClock.uptimeMillis() <= endTime) {
			// �ȴ�500ms
			sleeper.sleep();
			// ������ǰ���е�view
			for (View view : viewFetcher.getAllViews(false)) {
				// ���id,������������views����
				Integer idOfView = Integer.valueOf(view.getId());
				
				if (idOfView.equals(id)) {
					uniqueViewsMatchingId.add(view);
					// ���ҵ������index,���ص�ǰ��view
					if(uniqueViewsMatchingId.size() > index) {
						return view;
					}
				}
			}
			// ����������϶��������϶�����ˢ�¿ؼ�����
			if(scroll) 
				scroller.scrollDown();
		}
		// δ��������������false
		return null;
	}

	/**
	 * ���ո�����By�������������������ĵ�minimumNumberOfMatches��WebElement,�����ó�ʱʱ����Ƿ���Ҫ�϶�������ˢ��WebView����
	 * Waits for a web element.
	 * 
	 * @param by the By object. Examples are By.id("id") and By.name("name")
	 * @param minimumNumberOfMatches the minimum number of matches that are expected to be shown. {@code 0} means any number of matches
	 * @param timeout the the amount of time in milliseconds to wait 
	 * @param scroll {@code true} if scrolling should be performed 
	 */

	public WebElement waitForWebElement(final By by, int minimumNumberOfMatches, int timeout, boolean scroll){
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;

		while (true) {	
			// ����Ƿ��ѳ�ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// �ѳ�ʱ��¼�쳣��־������null
			if (timedOut){
				searcher.logMatchesFound(by.getValue());
				return null;
			}
			// �ȴ�500ms
			sleeper.sleep();
			// ��ȡ����������WebElement
			WebElement webElementToReturn = searcher.searchForWebElement(by, minimumNumberOfMatches); 
			// �õ���Ӧ��WebElement�򷵻�
			if(webElementToReturn != null)
				return webElementToReturn;
			// �����˿��϶������϶�ˢ��WebView�ɼ�����
			if(scroll) {
				scroller.scrollDown();
			}
		}
	}


	/**
	 * �����Զ�����ж��������ȴ�,�����ó�ʱʱ��
	 * Waits for a condition to be satisfied.
	 * 
	 * @param condition the condition to wait for
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if condition is satisfied and {@code false} if it is not satisfied before the timeout
	 */
	public boolean waitForCondition(Condition condition, int timeout){
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;

		while (true) {
			// ����Ƿ��ѳ�ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// �ѳ�ʱ��ֱ�ӷ���false
			if (timedOut){
				return false;
			}
			// �ȴ�500ms
			sleeper.sleep();
			// �������㷵��true ,Ϊ������������
			if (condition.isSatisfied()){
				return true;
			}
		}
	}

	/**
	 * ��ȡָ��text��TextView����Ԫ�س���.Ĭ�ϳ�ʱ20s,��ʱʱ����δ���ַ���null,�����򷵻ض�Ӧ��TextView
	 * Waits for a text to be shown. Default timeout is 20 seconds.
	 *
	 * @param text the text that needs to be shown, specified as a regular expression
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */

	public TextView waitForText(String text) {
		// ָ��text,�ҵ��ĵ�һ������ʱ20����Ҫ�϶�
		return waitForText(text, 0, Timeout.getLargeTimeout(), true);
	}

	/**
	 * ��ȡָ�� text�ĵ�expectedMinimumNumberOfMatches�� TextView���ֲ����ظ�TextView,�����ó�ʱʱ�䣬������ʱ����δ���ַ���null
	 * Waits for a text to be shown.
	 *
	 * @param text the text that needs to be shown, specified as a regular expression
	 * @param expectedMinimumNumberOfMatches the minimum number of matches of text that must be shown. {@code 0} means any number of matches
	 * @param timeout the amount of time in milliseconds to wait
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */

	public TextView waitForText(String text, int expectedMinimumNumberOfMatches, long timeout)
	{	
		// ָ��text.��expectedMinimumNumberOfMatches������ʱʱ�䣬���϶�
		return waitForText(text, expectedMinimumNumberOfMatches, timeout, true);
	}

	/**
	 * ��ȡָ��text�ĵ�expectedMinimumNumberOfMatches��TextView�������ó�ʱ���Ƿ���Ҫ����.��������򷵻ظ�TextView,δ�����򷵻�null
	 * Waits for a text to be shown.
	 *
	 * @param text the text that needs to be shown, specified as a regular expression
	 * @param expectedMinimumNumberOfMatches the minimum number of matches of text that must be shown. {@code 0} means any number of matches
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */

	public TextView waitForText(String text, int expectedMinimumNumberOfMatches, long timeout, boolean scroll) {
		// ��������ΪTextView text index ��ʱʱ�䣬scroll ȫ��Ԫ�� 
		return waitForText(TextView.class, text, expectedMinimumNumberOfMatches, timeout, scroll, false, true);	
	}
	
	/**
	 * ��ȡָ��class���ͣ�ָ��text���ݵĵ�expectedMinimumNumberOfMatches�� TextView
	 * �����ó�ʱʱ�䣬�Ƿ�����϶�
	 * Waits for a text to be shown.
	 *
	 * @param classToFilterBy the class to filter by
	 * @param text the text that needs to be shown, specified as a regular expression
	 * @param expectedMinimumNumberOfMatches the minimum number of matches of text that must be shown. {@code 0} means any number of matches
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */

	public <T extends TextView> T waitForText(Class<T> classToFilterBy, String text, int expectedMinimumNumberOfMatches, long timeout, boolean scroll) {
		// Ĭ�����ó�ʱ��ִ������˳�
		return waitForText(classToFilterBy, text, expectedMinimumNumberOfMatches, timeout, scroll, false, true);	
	}

	/**
	 * ��ȡָ��text�ĵ�expectedMinimumNumberOfMatches��TextView,��ָ����ʱʱ�䣬�Ƿ���Ҫ�϶�
	 * �Ƿ���˷ǿɼ�view,�Ƿ���Ҫ��ʱ�������˳�
	 * Waits for a text to be shown.
	 *
	 * @param text the text that needs to be shown, specified as a regular expression.
	 * @param expectedMinimumNumberOfMatches the minimum number of matches of text that must be shown. {@code 0} means any number of matches
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @param onlyVisible {@code true} if only visible text views should be waited for
	 * @param hardStoppage {@code true} if search is to be stopped when timeout expires
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */
	
	public TextView waitForText(String text, int expectedMinimumNumberOfMatches, long timeout, boolean scroll, boolean onlyVisible, boolean hardStoppage) {
		return waitForText(TextView.class, text, expectedMinimumNumberOfMatches, timeout, scroll, onlyVisible, hardStoppage);
	}

	/**
	 * ��ȡָ��clas���ͺ�text�ĵ�expectedMinimumNumberOfMatches��view.
	 * classToFilterBy                  ָ����class����
	 * text                             ָ����text����
	 * expectedMinimumNumberOfMatches   view��index
	 * timeout                          ��ʱʱ�䣬��λ ms
	 * scroll                           true���ڿ��϶��ؼ��϶�ˢ�£�false ���϶�ˢ��
	 * onlyVisible                      true ���˵��ǿɼ���,false  ��������
	 * hardStoppage                     true �����в�����ɺ󷵻�,false ��ʱ��ǿ��ֹͣ��ز�������������
	 * Waits for a text to be shown.
	 *
	 * @param classToFilterBy the class to filter by
	 * @param text the text that needs to be shown, specified as a regular expression.
	 * @param expectedMinimumNumberOfMatches the minimum number of matches of text that must be shown. {@code 0} means any number of matches
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll {@code true} if scrolling should be performed
	 * @param onlyVisible {@code true} if only visible text views should be waited for
	 * @param hardStoppage {@code true} if search is to be stopped when timeout expires
	 * @return {@code true} if text is found and {@code false} if it is not found before the timeout
	 */

	public <T extends TextView> T waitForText(Class<T> classToFilterBy, String text, int expectedMinimumNumberOfMatches, long timeout, boolean scroll, boolean onlyVisible, boolean hardStoppage) {
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;

		while (true) {
			// ����Ƿ�ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// ��ʱ�򷵻�null
			if (timedOut){
				return null;
			}
			// �ȴ�500ms
			sleeper.sleep();
			// true  searcher����������ѭ����ֱ����ʱ�˳���false  searcher�����в�ѭ��ִ��ֻ��һ���ж�
			if(!hardStoppage)
				timeout = 0;

			final T textViewToReturn = searcher.searchFor(classToFilterBy, text, expectedMinimumNumberOfMatches, timeout, scroll, onlyVisible);

			if (textViewToReturn != null ){
				return textViewToReturn;
			}
		}
	}

	/**
	 * ��ȡָ��class���͵ĵ�index��View,Ĭ�ϳ�ʱ10s,10s��δ�ҵ�����null
	 * Waits for and returns a View.
	 * 
	 * @param index the index of the view
	 * @param classToFilterby the class to filter
	 * @return the specified View
	 */

	public <T extends View> T waitForAndGetView(int index, Class<T> classToFilterBy){
		// ���ó�ʱʱ��㣬��ǰʱ��+10s
		long endTime = SystemClock.uptimeMillis() + Timeout.getSmallTimeout();
		// δ��ʱ����ָ������������δ������������
		while (SystemClock.uptimeMillis() <= endTime && !waitForView(classToFilterBy, index, true, true));
		// ��ȡ�ҵ���view����
		int numberOfUniqueViews = searcher.getNumberOfUniqueViews();
		// ����ָ���� class���ͻ�ȡ���еĿɼ�view
		ArrayList<T> views = RobotiumUtils.removeInvisibleViews(viewFetcher.getCurrentViews(classToFilterBy));
		// ��ǰ��ȡ��views ��������Ψһ������,index������
		if(views.size() < numberOfUniqueViews){
			int newIndex = index - (numberOfUniqueViews - views.size());
			if(newIndex >= 0)
				index = newIndex;
		}

		T view = null;
		try{
			// ��ȡ��Ӧ��view
			view = views.get(index);
		}catch (IndexOutOfBoundsException exception) {
			// ��ȡ�쳣��¼�쳣��־
			int match = index + 1;
			if(match > 1) {
				Assert.fail(match + " " + classToFilterBy.getSimpleName() +"s" + " are not found!");
			}
			else {
				Assert.fail(classToFilterBy.getSimpleName() + " is not found!");
			}
		}
		// �ͷŶ���
		views = null;
		return view;
	}

	/**
	 * �ȴ�ָ��tag id��Fragment.�����ó�ʱʱ��
	 * ���Ȳ���android.support.v4.app.Fragment
	 * Ϊ�ҵ��ٲ��� android.app.Fragment
	 * ��δ�ҵ�����null
	 * Waits for a Fragment with a given tag or id to appear.
	 * 
	 * @param tag the name of the tag or null if no tag	
	 * @param id the id of the tag
	 * @param timeout the amount of time in milliseconds to wait
	 * @return true if fragment appears and false if it does not appear before the timeout
	 */

	public boolean waitForFragment(String tag, int id, int timeout){
		// ���ó�ʱʱ��
		long endTime = SystemClock.uptimeMillis() + timeout;
		while (SystemClock.uptimeMillis() <= endTime) {
			// ���� android.support.v4.app.Fragment ���ҵ����� android.support.v4.app.Fragment ,δ�ҵ��������� android.app.Fragment
			if(getSupportFragment(tag, id) != null)
				return true;
			// ���� android.app.Fragment
			if(getFragment(tag, id) != null)
				return true;
		}
		return false;
	}

	/**
	 * ��ȡָ��tag��id��Ӧ��android.support.v4.app.Fragment
	 * Returns a SupportFragment with a given tag or id.
	 * 
	 * @param tag the tag of the SupportFragment or null if no tag
	 * @param id the id of the SupportFragment
	 * @return a SupportFragment with a given tag or id
	 */

	private Fragment getSupportFragment(String tag, int id){
		FragmentActivity fragmentActivity = null;

		try{
			// ��ȡ��ǰ activityת���� FragmentActivity����
			fragmentActivity = (FragmentActivity) activityUtils.getCurrentActivity(false);
		}catch (ClassCastException ignored) {}
		// ��ȡ������Ӧ�� Fragment
		if(fragmentActivity != null){
			try{
				if(tag == null)
					return fragmentActivity.getSupportFragmentManager().findFragmentById(id);
				else
					return fragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
			}catch (NoSuchMethodError ignored) {}
		}
		// δ�ҵ���Ӧ��,����null
		return null;
	}

	/**
	 * ָ������־��Ϣ�Ƿ���ָ����ʱʱ���ڴ�ӡ
	 * logMessage   �������ֵ���־��Ϣ
	 * timeout      ��ʱʱ�䣬��λ ms
	 * Waits for a log message to appear.
	 * Requires read logs permission (android.permission.READ_LOGS) in AndroidManifest.xml of the application under test.
	 * 
	 * @param logMessage the log message to wait for
	 * @param timeout the amount of time in milliseconds to wait
	 * @return true if log message appears and false if it does not appear before the timeout
	 */

	public boolean waitForLogMessage(String logMessage, int timeout){
		StringBuilder stringBuilder = new StringBuilder();
		// ���ó�ʱʱ���
		long endTime = SystemClock.uptimeMillis() + timeout;
		while (SystemClock.uptimeMillis() <= endTime) {
			// ��ȡlogcat���ݼ��ָ�������Ƿ����
			if(getLog(stringBuilder).lastIndexOf(logMessage) != -1){
				return true;
			}
			// �ȴ�500ms
			sleeper.sleep();
		}
		// ָ��ʱ����δ�ҵ�������false
		return false;
	}

	/**
	 * ��ȡ��ǰlogcat 
	 * Returns the log in the given stringBuilder. 
	 * 
	 * @param stringBuilder the StringBuilder object to return the log in
	 * @return the log
	 */

	private StringBuilder getLog(StringBuilder stringBuilder) {
		Process p = null;
		BufferedReader reader = null;
		String line = null;  

		try {
            // read output from logcat ִ��logcat -d ��ȡһ��logcat
			p = Runtime.getRuntime().exec("logcat -d");
			reader = new BufferedReader(  
					new InputStreamReader(p.getInputStream())); 

			stringBuilder.setLength(0);
			while ((line = reader.readLine()) != null) {  
				stringBuilder.append(line); 
			}
            reader.close();
            
            // read error from logcat,�������ִ���Ƿ񱨴���
            StringBuilder errorLog = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(
                    p.getErrorStream()));
            errorLog.append("logcat returns error: ");
            while ((line = reader.readLine()) != null) {
                errorLog.append(line);
            }
            reader.close();

            // Exception would be thrown if we get exitValue without waiting for the process
            // to finish
            p.waitFor();

            // if exit value of logcat is non-zero, it means error
            if (p.exitValue() != 0) {
                destroy(p, reader);

                throw new Exception(errorLog.toString());
            }

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		// ִ��������ٽ��̹ر�reader
		destroy(p, reader);
		return stringBuilder;
	}

	/**
	 * ���logcat����
	 * Clears the log.
	 */

	public void clearLog(){
		Process p = null;
		try {
			// ����logcat -c ���logcat����
			p = Runtime.getRuntime().exec("logcat -c");
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * ���ٽ��̣����ر�reader
	 * p      ��Ҫ���ٵĽ���
	 * reader ��Ҫ�رյ�BufferedReader
	 * Destroys the process and closes the BufferedReader.
	 * 
	 * @param p the process to destroy
	 * @param reader the BufferedReader to close
	 */

	private void destroy(Process p, BufferedReader reader){
		p.destroy();
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ָ��tag id�� android.app.Fragment,δ�ҵ��򷵻�null
	 * Returns a Fragment with a given tag or id.
	 * 
	 * @param tag the tag of the Fragment or null if no tag
	 * @param id the id of the Fragment
	 * @return a SupportFragment with a given tag or id
	 */

	private android.app.Fragment getFragment(String tag, int id){

		try{
			if(tag == null)
				return activityUtils.getCurrentActivity().getFragmentManager().findFragmentById(id);
			else
				return activityUtils.getCurrentActivity().getFragmentManager().findFragmentByTag(tag);
		}catch (NoSuchMethodError ignored) {}

		return null;
	}
}