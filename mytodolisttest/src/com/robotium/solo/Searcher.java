package com.robotium.solo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


/**
 * �ؼ����������࣬���԰��ո��ֹؼ���Ϣ���Ҷ�Ӧ�ؼ�
 * Contains various search methods. Examples are: searchForEditTextWithTimeout(),
 * searchForTextWithTimeout(), searchForButtonWithTimeout().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Searcher {
	// View ����������
	private final ViewFetcher viewFetcher;
	// WebView����������
	private final WebUtils webUtils;
	// �����ؼ�����������
	private final Scroller scroller;
	// ��ʱ����������
	private final Sleeper sleeper;
	// ��־��ӡ��ǩ����ʶ���� Robotium
	private final String LOG_TAG = "Robotium";
	// ���ڴ洢����ָ��text�����views,������ݰ�����ʾ���ݣ�������ʾ��Ϣ���Ѻ���ʾ��Ϣ
	Set<TextView> uniqueTextViews;
	// WebElement����
	List<WebElement> webElements;
	// ͳ�Ʒ��ظ�View����
	private int numberOfUniqueViews;
	// Ĭ�ϳ�ʱ5s
	private final int TIMEOUT = 5000;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param webUtils the {@code WebUtils} instance
	 * @param scroller the {@code Scroller} instance
	 * @param sleeper the {@code Sleeper} instance.
	 */

	public Searcher(ViewFetcher viewFetcher, WebUtils webUtils, Scroller scroller, Sleeper sleeper) {
		this.viewFetcher = viewFetcher;
		this.webUtils = webUtils;
		this.scroller = scroller;
		this.sleeper = sleeper;
		webElements = new ArrayList<WebElement>();
		uniqueTextViews = new HashSet<TextView>();
	}


	/**
	 * ���ո����Ĳ�������������Ƿ��ҵ���������Ԫ�أ��ҵ�����true,δ�ҵ�����false
	 * viewClass       					ϣ����Ԫ������
	 * regex           					text����
	 * expectedMinimumNumberOfMatches   �������ϸ�����Ԫ��������������ȷ���true,����ȷ���false
	 * scroll                           �Ƿ���Ҫ�϶����ң����б�δ��ʾȫ�����ݣ��϶�����ˢ������
	 * onlyVisible                      trueֻ���ҿɼ��ģ�false����ȫ����
	 * Searches for a {@code View} with the given regex string and returns {@code true} if the
	 * searched {@code Button} is found a given number of times. Will automatically scroll when needed.
	 *
	 * @param viewClass what kind of {@code View} to search for, e.g. {@code Button.class} or {@code TextView.class}
	 * @param regex the text to search for. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param expectedMinimumNumberOfMatches the minimum number of matches expected to be found. {@code 0} matches means that one or more
	 * matches are expected to be found
	 * @param scroll whether scrolling should be performed
	 * @param onlyVisible {@code true} if only texts visible on the screen should be searched
	 * 
	 * @return {@code true} if a {@code View} of the specified class with the given text is found a given number of
	 * times, and {@code false} if it is not found
	 */

	public boolean searchWithTimeoutFor(Class<? extends TextView> viewClass, String regex, int expectedMinimumNumberOfMatches, boolean scroll, boolean onlyVisible) {
		// �趨��ʱʱ��,��ǰʱ�����5s
		final long endTime = SystemClock.uptimeMillis() + TIMEOUT;
		// ��ʼ����ʱ����Ϊnull
		TextView foundAnyMatchingView = null;
		// �����û����ָ��ʱ�仹Ϊ�ҵ����������
		while (SystemClock.uptimeMillis() < endTime) {
			// ��500ms
			sleeper.sleep();
			// ���ո������������ò�ѯ����,��ʱ����Ϊ0
			foundAnyMatchingView = searchFor(viewClass, regex, expectedMinimumNumberOfMatches, 0, scroll, onlyVisible);
			// �ҵ���ֱ�ӷ���
			if (foundAnyMatchingView !=null){
				return true;
			}
		}
		return false;
	}


	/**
	 * ���ո�������������TextView���͵�View
	 * viewClass          				���õ�class����
	 * regex              				text������ʽ
	 * expectedMinimumNumberOfMatches   ������View����,�������������������ô����null,����һ�·��ص�expectedMinimumNumberOfMatches��
	 * timeout                          ��ʱʱ��
	 * scroll							�Ƿ���Ҫ�϶����ң����б�δ��ʾȫ�����ݣ��϶�����ˢ������
	 * onlyVisible                      trueֻ���ҿɼ��ģ�false����ȫ����
	 * Searches for a {@code View} with the given regex string and returns {@code true} if the
	 * searched {@code View} is found a given number of times.
	 *
	 * @param viewClass what kind of {@code View} to search for, e.g. {@code Button.class} or {@code TextView.class}
	 * @param regex the text to search for. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param expectedMinimumNumberOfMatches the minimum number of matches expected to be found. {@code 0} matches means that one or more
	 * matches are expected to be found.
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll whether scrolling should be performed
	 * @param onlyVisible {@code true} if only texts visible on the screen should be searched
	 * 
	 * @return {@code true} if a view of the specified class with the given text is found a given number of times.
	 * {@code false} if it is not found.
	 */

	public <T extends TextView> T searchFor(final Class<T> viewClass, final String regex, int expectedMinimumNumberOfMatches, final long timeout, final boolean scroll, final boolean onlyVisible) {
		// ������õ�������ƥ����С��1�Σ���Ĭ������Ϊ1��
		if(expectedMinimumNumberOfMatches < 1) {
			expectedMinimumNumberOfMatches = 1;
		}
		// ����������߳��е��õļ�����
		final Callable<Collection<T>> viewFetcherCallback = new Callable<Collection<T>>() {
			@SuppressWarnings("unchecked")
			public Collection<T> call() throws Exception {
				// �ȴ�500ms
				sleeper.sleep();
				// ��ȡ��ǰ��Ļ������views,����ΪviewClass��ָ����
				ArrayList<T> viewsToReturn = viewFetcher.getCurrentViews(viewClass);
				// ���������ֻ���ҿɼ�view�е����ݣ���ô���˵����зǿɼ���
				if(onlyVisible){
					viewsToReturn = RobotiumUtils.removeInvisibleViews(viewsToReturn);
				}
				// ����Ƿ���TextView���͵�,����ǲ���TextView���͵ģ��ҵ�ǰ��Ļ���ݰ���WebView.��ôҲ��WebView�е����TextView��Ԫ��ȫ�����뷵���б�
				if(viewClass.isAssignableFrom(TextView.class)) {
					viewsToReturn.addAll((Collection<? extends T>) webUtils.getTextViewsFromWebView());
				}
				// �����ҵ���views
				return viewsToReturn;
			}
		};

		try {
			// �������view
			return searchFor(viewFetcherCallback, regex, expectedMinimumNumberOfMatches, timeout, scroll);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ��Ӧclass���͵�View�����Ƿ�<=index
	 * 
	 * unqiueViews     ������views
	 * viewClass       ָ����view����
	 * index           ָ����������0��ʼ����
	 * Searches for a view class.
	 * 
	 * @param uniqueViews the set of unique views
	 * @param viewClass the view class to search for
	 * @param index the index of the view class
	 * @return true if view class if found a given number of times
	 */

	public <T extends View> boolean searchFor(Set<T> uniqueViews, Class<T> viewClass, final int index) {
		// ��ȡ��ǰ��������и��� class���͵Ŀɼ�View
		ArrayList<T> allViews = RobotiumUtils.removeInvisibleViews(viewFetcher.getCurrentViews(viewClass));
		// ���ز��ظ���view����������allViews���뵽uniqueViews������
		int uniqueViewsFound = (getNumberOfUniqueViews(uniqueViews, allViews));
		// indexλ�����������У�����true,���Ի�ȡ
		if(uniqueViewsFound > 0 && index < uniqueViewsFound) {
			return true;
		}
		// �������������,������ִ�в���
		if(uniqueViewsFound > 0 && index == 0) {
			return true;
		}
		// indexֵ���ڵ�������������false,Խ����
		return false;
	}

	/**
	 * ���view�Ƿ��ڵ�ǰ��Ļ��.�Ƿ���true.���ڷ���false
	 * Searches for a given view.
	 * 
	 * @param view the view to search
	 * @param scroll true if scrolling should be performed
	 * @return true if view is found
	 */

	public <T extends View> boolean searchFor(View view) {
		// ��ȡ��ǰ��Ļ�����еĿɼ�view
		ArrayList<View> views = viewFetcher.getAllViews(true);
		for(View v : views){
			// �ж�view�Ƿ��ڵ�ǰ��Ļ��,�Ƿ���true
			if(v.equals(view)){
				return true;
			}
		}
		// �����ڷ���false
		return false;
	}

	/**
	 * ���յ��÷��������class���ͣ�text��Ҫƥ������򣬵ڼ���ƥ���Ԫ��,���ҷ��������� View,�ҵ���Ԫ�������������Ĳ�һ�£���ô����null,���������ģ���ô�����б��е����һ��Ԫ��
	 * regex     					     �ַ�������
	 * expectedMinimumNumberOfMatches ���ҵڼ���
	 * timeout                        ���ҳ�ʱʱ��
	 * scroll                         �Ƿ���Ҫ���������б��еĲ��ɼ�Ԫ�أ�����һ�¾Ϳɼ��ˣ�������true�Ϳ����ҵ�    
	 * Searches for a {@code View} with the given regex string and returns {@code true} if the
	 * searched {@code View} is found a given number of times. Will not scroll, because the caller needs to find new
	 * {@code View}s to evaluate after scrolling, and call this method again.
	 *
	 * @param viewFetcherCallback callback which should return an updated collection of views to search
	 * @param regex the text to search for. The parameter <strong>will</strong> be interpreted as a regular expression.
	 * @param expectedMinimumNumberOfMatches the minimum number of matches expected to be found. {@code 0} matches means that one or more
	 * matches are expected to be found.
	 * @param timeout the amount of time in milliseconds to wait
	 * @param scroll whether scrolling should be performed
	 * 
	 * @return {@code true} if a view of the specified class with the given text is found a given number of times.
	 * {@code false} if it is not found.
	 *
	 * @throws Exception not really, it's just the signature of {@code Callable}
	 */

	public <T extends TextView> T searchFor(Callable<Collection<T>> viewFetcherCallback, String regex, int expectedMinimumNumberOfMatches, long timeout, boolean scroll) throws Exception {
		// ���ó�ʱʱ���
		final long endTime = SystemClock.uptimeMillis() + timeout;	
		Collection<T> views;

		while (true) {
			// ����Ƿ��ѹ��趨�ĳ�ʱ��
			final boolean timedOut = timeout > 0 && SystemClock.uptimeMillis() > endTime;
			// �Ѿ���ʱ��ֱ���˳���ѯ������ӡ�����־��¼
			if(timedOut){
				logMatchesFound(regex);
				return null;
			}
			// ��ȡ�����������˺������Views
			views = viewFetcherCallback.call();

			for(T view : views){
				// ����Ƿ��ҵ�������������������ҵ�������������Ԫ�أ���ô��ջ��棬�����ҵ��Ķ�ӦView
				if (RobotiumUtils.getNumberOfMatches(regex, view, uniqueTextViews) == expectedMinimumNumberOfMatches) {
					uniqueTextViews.clear();
					return view;
				}
			}
			// ��������˿��϶������ǵ�ǰ�������϶�����ô��¼�쳣��־������null,��Config�������Ƿ���϶���Ĭ��Ϊtrue
			if(scroll && !scroller.scrollDown()){
				logMatchesFound(regex);
				return null; 
			}
			// ���δ���ÿ��϶�����¼�쳣��־������null
			if(!scroll){
				logMatchesFound(regex);
				return null; 
			}
		}
	}

	/**
	 * ���ո�����By���������� WebView�е�WebElement,minimumNumberOfMatches ָ����Ҫ���صڼ���
	 * Searches for a web element.
	 * 
	 * @param by the By object e.g. By.id("id");
	 * @param minimumNumberOfMatches the minimum number of matches that are expected to be shown. {@code 0} means any number of matches
	 * @return the web element or null if not found
	 */

	public WebElement searchForWebElement(final By by, int minimumNumberOfMatches){
		// �����������С��1,��ôĬ������Ϊ1
		if(minimumNumberOfMatches < 1){
			minimumNumberOfMatches = 1;
		}
		// ʹ��by��Ϊ��������.��ȡ��ǰ������WebElement
		List<WebElement> viewsFromScreen = webUtils.getCurrentWebElements(by);
		// viewsFromScreen�е�Ԫ�غϲ���webElement�У�����ȥ��,text��xy����һ����Ϊ�ظ��ж�����
		addViewsToList (webElements, viewsFromScreen);
		// ����ָ����WebElement
		return getViewFromList(webElements, minimumNumberOfMatches);
	}

	/**
	 * �б�ϲ�,webElementsOnScreen���뵽allWebElements��
	 * ʹ�� text��xy����λ����Ϊ2���б���Ԫ���Ƿ��ظ��ж�����
	 * Adds views to a given list.
	 * 
	 * @param allWebElements the list of all views
	 * @param webTextViewsOnScreen the list of views shown on screen
	 */

	private void addViewsToList(List<WebElement> allWebElements, List<WebElement> webElementsOnScreen){
		// ����allWebElements��Ԫ�ص�xy����
		int[] xyViewFromSet = new int[2];
		// ����webZElementOnScreenԪ�ص�λ��xy����
		int[] xyViewFromScreen = new int[2];
		//  ����
		for(WebElement textFromScreen : webElementsOnScreen){
			boolean foundView = false;
			// ��ȡ��Ļxy����
			textFromScreen.getLocationOnScreen(xyViewFromScreen);

			for(WebElement textFromList : allWebElements){
				// ��ȡ��Ļ��Ӧ��xy����
				textFromList.getLocationOnScreen(xyViewFromSet);
				//  allWebElements���Ѵ��ڵ����ظ�����,����text��xy������Ϊ�Ƿ���ȵ�����
				if(textFromScreen.getText().equals(textFromList.getText()) && xyViewFromScreen[0] == xyViewFromSet[0] && xyViewFromScreen[1] == xyViewFromSet[1]) {
					foundView = true;
				}
			}
			// �������ظ��ж������ļ���allWebElements�б�
			if(!foundView){
				allWebElements.add(textFromScreen);
			}
		}

	}

	/**
	 * ��ȡ�б���ָ��λ�õ�Ԫ�أ�����Խ���򷵻�null
	 * Returns a text view with a given match.
	 * 
	 * @param webElements the list of views
	 * @param match the match of the view to return
	 * @return the view with a given match
	 */

	private WebElement getViewFromList(List<WebElement> webElements, int match){

		WebElement webElementToReturn = null;
		// ����Ƿ񳬹��б���WebElement����,�����򷵻�null
		if(webElements.size() >= match){

			try{
				// ��ȡ��Ӧλ��Ԫ��
				webElementToReturn = webElements.get(--match);
			}catch(Exception ignored){}
		}
		//�ҵ�Ԫ������ջ���
		if(webElementToReturn != null)
			webElements.clear();
		// ����Ԫ��
		return webElementToReturn;
	}

	/**
	 * ��views�е�View ���뵽uniqueViews�����У�����ȥ�أ�����unqiueViews�е�View������
	 * Returns the number of unique views. 
	 * 
	 * @param uniqueViews the set of unique views
	 * @param views the list of all views
	 * @return number of unique views
	 */

	public <T extends View> int getNumberOfUniqueViews(Set<T>uniqueViews, ArrayList<T> views){
		// ��view����set��,set���ͱ�֤�˲�������ظ���view
		for(int i = 0; i < views.size(); i++){
			uniqueViews.add(views.get(i));
		}
		// ��ȡuniqueViews�е�View����
		numberOfUniqueViews = uniqueViews.size();
		return numberOfUniqueViews;
	}

	/**
	 * ��ȡunqiueViews�е�View����
	 * Returns the number of unique views.
	 * 
	 * @return the number of unique views
	 */

	public int getNumberOfUniqueViews(){
		return numberOfUniqueViews;
	}

	/**
	 * ��ӡ����ʧ����־.����ջ�������
	 * Logs a (searchFor failed) message.
	 *  
	 * @param regex the search string to log
	 */

	public void logMatchesFound(String regex){
		// ��ӡ��ǰTextView��������������
		if (uniqueTextViews.size() > 0) {
			Log.d(LOG_TAG, " There are only " + uniqueTextViews.size() + " matches of '" + regex + "'");
		}
		// ��ӡ��ǰWebView�е�Element��������������
		else if(webElements.size() > 0){
			Log.d(LOG_TAG, " There are only " + webElements.size() + " matches of '" + regex + "'");
		}
		// ����������
		uniqueTextViews.clear();
		webElements.clear();
	}
}