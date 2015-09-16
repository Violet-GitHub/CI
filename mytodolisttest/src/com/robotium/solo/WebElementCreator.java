package com.robotium.solo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import android.os.SystemClock;
import android.webkit.WebView;

/**
 * ��WebElement��Ϣ������WebElement����
 * Contains TextView related methods. Examples are:
 * getTextViewsFromWebViews(), createTextViewAndAddInList().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class WebElementCreator {
	// �洢WebElement
	private List<WebElement> webElements;
	// ��ʱ������
	private Sleeper sleeper;
	// ��ʶ����,���ڱ�ʶWebView���ݽ����Ƿ��Ѿ����
	private boolean isFinished = false;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param sleeper the {@code Sleeper} instance
	 * 
	 */

	public WebElementCreator(Sleeper sleeper){
		this.sleeper = sleeper;
		// ����һ���洢ʵ����ʹ��copyOnweite���Ա�֤����listʱ���¹���һ���µģ���ԭ�в����Ӱ��
		webElements = new CopyOnWriteArrayList<WebElement>();
	}

	/**
	 * ��ʼ��
	 * Prepares for start of creating {@code TextView} objects based on web elements 
	 */

	public void prepareForStart(){
		// ����Ϊfalse
		setFinished(false);
		// ����Ѵ洢��WebElement
		webElements.clear();
	}

	/**
	 * ��ȡ��ǰ WebView�е�WebElement
	 * Returns an {@code ArrayList} of {@code TextView} objects based on the web elements shown
	 * 
	 * @return an {@code ArrayList} of {@code TextView} objects based on the web elements shown
	 */

	public ArrayList<WebElement> getWebElementsFromWebViews(){
		// �ȴ�WebViewԪ�ر�����
		waitForWebElementsToBeCreated();
		// copyһ�ݶ��󷵻�
		return new ArrayList<WebElement>(webElements);
	}

	/**
	 * ��ȡWebView���ݽ���״̬
	 * true   ���������
	 * false  ������δ���
	 * Returns true if all {@code TextView} objects based on web elements have been created
	 * 
	 * @return true if all {@code TextView} objects based on web elements have been created
	 */

	public boolean isFinished(){
		return isFinished;
	}


	/**
	 * ����WebView�����Ƿ����״̬
	 * true   ����ɽ���
	 * false  ����δ���
	 * Set to true if all {@code TextView} objects have been created
	 * 
	 * @param isFinished true if all {@code TextView} objects have been created
	 */

	public void setFinished(boolean isFinished){
		this.isFinished = isFinished;
	}

	/**
	 * ����ָ����Ϣ����ȡWebView�е�WebElement�����뵽webElements��
	 * Creates a {@ WebElement} object from the given text and {@code WebView}
	 * 
	 * @param webData the data of the web element 
	 * @param webView the {@code WebView} the text is shown in
	 */

	public void createWebElementAndAddInList(String webData, WebView webView){
		// ��ȡWebElement
		WebElement webElement = createWebElementAndSetLocation(webData, webView);
		// �ǿ������WebElement�б�
		if((webElement!=null)) 
			webElements.add(webElement);
	}

	/**
	 * ����WebElement��������
	 * webElement ��Ҫ���õ�WebElement
	 * webView    WebElement���ڵ�WebView
	 * 
	 * Sets the location of a {@code WebElement} 
	 * 
	 * @param webElement the {@code TextView} object to set location 
	 * @param webView the {@code WebView} the text is shown in
	 * @param x the x location to set
	 * @param y the y location to set
	 * @param width the width to set
	 * @param height the height to set
	 */

	private void setLocation(WebElement webElement, WebView webView, int x, int y, int width, int height ){
		// ��ȡҳ��������Ϣ
		float scale = webView.getScale();
		// ������Ļ����
		int[] locationOfWebViewXY = new int[2];
		// ��ȡWebView��Ӧ�ֻ���Ļ�е�����
		webView.getLocationOnScreen(locationOfWebViewXY);
		// ������Ե����x����,ȡWebElement�м�λ��
		int locationX = (int) (locationOfWebViewXY[0] + (x + (Math.floor(width / 2))) * scale);
		// ����ɲ����� y����,ȡWebElement�м�λ��
		int locationY = (int) (locationOfWebViewXY[1] + (y + (Math.floor(height / 2))) * scale);

		webElement.setLocationX(locationX);
		webElement.setLocationY(locationY);
	}

	/**
	 * ���ո�����Ϣ��ȡWebView�ж�Ӧ��Ԫ��
	 * Creates a {@code WebView} object 
	 * 
	 * @param information the data of the web element
	 * @param webView the web view the text is shown in
	 * 
	 * @return a {@code WebElement} object with a given text and location
	 */

	private WebElement createWebElementAndSetLocation(String information, WebView webView){
		// �������ԣ�����;,����
		String[] data = information.split(";,");
		String[] elements = null;
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		Hashtable<String, String> attributes = new Hashtable<String, String>();
		try{
			// ������Ӧ��x����
			x = Math.round(Float.valueOf(data[5]));
			// ������Ӧ��y����
			y = Math.round(Float.valueOf(data[6]));
			// ���������Ϣ
			width = Math.round(Float.valueOf(data[7]));
			// �����߶���Ϣ
			height = Math.round(Float.valueOf(data[8]));	
			// ����ʣ������
			elements = data[9].split("\\#\\$");
		}catch(Exception ignored){}
		// ����Ϊkey value��ʽ,ʹ��::�ָ�
		if(elements != null) {
			for (int index = 0; index < elements.length; index++){
				String[] element = elements[index].split("::");
				// ����ֻ��key�����ԣ�keyҲ��Ϊvalueʹ��
				if (element.length > 1) {
					attributes.put(element[0], element[1]);
				} else {
					attributes.put(element[0], element[0]);
				}
			}
		}

		WebElement webElement = null;

		try{
			// ����WebElement����
			webElement = new WebElement(data[0], data[1], data[2], data[3], data[4], attributes);
			// ����λ����Ϣ
			setLocation(webElement, webView, x, y, width, height);
		}catch(Exception ignored) {}

		return webElement;
	}

	/**
	 * ���WebView���ݽ����Ƿ����,Ĭ�ϳ�ʱ5s,
	 * ������ɷ���true,δ��ɷ���false
	 * Waits for {@code WebElement} objects to be created
	 * 
	 * @return true if successfully created before timout
	 */

	private boolean waitForWebElementsToBeCreated(){
		// 5s��ʱ
		final long endTime = SystemClock.uptimeMillis() + 5000;
		// ����Ƿ�ʱ
		while(SystemClock.uptimeMillis() < endTime){
			// �ѽ�����ɣ�����true
			if(isFinished){
				return true;
			}
			// �ȴ�300ms
			sleeper.sleepMini();
		}
		return false;
	}

}