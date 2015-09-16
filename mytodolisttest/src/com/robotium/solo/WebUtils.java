package com.robotium.solo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.robotium.solo.Solo.Config;

import android.app.Instrumentation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * Contains web related methods. Examples are:
 * enterTextIntoWebElement(), getWebTexts(), getWebElements().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class WebUtils {
	// View����������
	private ViewFetcher viewFetcher;
	// Instrument,���ڸ����¼�����
	private Instrumentation inst;
	// Activity����������
	private ActivityUtils activityUtils;
	// Robotium���Ƶ�WebClient
	RobotiumWebClient robotiumWebCLient;
	// WebElement���칤�߷���
	WebElementCreator webElementCreator;
	// ԭ��WebChromeClient ����������ҪRobotium�޸ĵ�ʹ��ԭ����ִ��
	WebChromeClient originalWebChromeClient = null;
	// �����ļ�
	private Config config;


	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param config the {@code Config} instance
	 * @param instrumentation the {@code Instrumentation} instance
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 */

	public WebUtils(Config config, Instrumentation instrumentation, ActivityUtils activityUtils, ViewFetcher viewFetcher, Sleeper sleeper){
		this.config = config;
		this.inst = instrumentation;
		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		webElementCreator = new WebElementCreator(sleeper);
		robotiumWebCLient = new RobotiumWebClient(instrumentation, webElementCreator);
	}

	/**
	 * ����RoBotiumWeb.js��ȡ���е�Text��WebElement,ʹ��NodeFilter.SHOW_TEXT����
	 * Returns {@code TextView} objects based on web elements shown in the present WebViews
	 * 
	 * @param onlyFromVisibleWebViews true if only from visible WebViews
	 * @return an {@code ArrayList} of {@code TextViews}s created from the present {@code WebView}s 
	 */

	public ArrayList<TextView> getTextViewsFromWebView(){
		// true��ʶִ����ɣ�false��ʶδִ�гɹ�
		boolean javaScriptWasExecuted = executeJavaScriptFunction("allTexts();");	
		// WebElementת����TextView
		return createAndReturnTextViewsFromWebElements(javaScriptWasExecuted);	
	}

	/**
	 * WebElementת���� TextView��javaScriptWasExecuted Ϊtrue��ִ��ת����false��ִ��ת��
	 * 
	 * Creates and returns TextView objects based on WebElements
	 * 
	 * @return an ArrayList with TextViews
	 */

	private ArrayList <TextView> createAndReturnTextViewsFromWebElements(boolean javaScriptWasExecuted){
		ArrayList<TextView> webElementsAsTextViews = new ArrayList<TextView>();
		// js�ű�ִ�гɹ�����������л�ȡ����WebElement��Ϣ����ת����TextView����
		if(javaScriptWasExecuted){
			// �������е�WebElement
			for(WebElement webElement : webElementCreator.getWebElementsFromWebViews()){
				// �ɼ��ؼ�ת����TextView����
				if(isWebElementSufficientlyShown(webElement)){
					// ת����TextView����
					RobotiumTextView textView = new RobotiumTextView(inst.getContext(), webElement.getText(), webElement.getLocationX(), webElement.getLocationY());
					// ��ӵ������б�
					webElementsAsTextViews.add(textView);
				}
			}	
		}
		return webElementsAsTextViews;		
	}

	/**
	 * ��ȡ��ǰWebView�е�����WebElements
	 * Returns an ArrayList of WebElements currently shown in the active WebView.
	 * 
	 * @return an {@code ArrayList} of the {@link WebElement} objects currently shown in the active WebView
	 */

	public ArrayList<WebElement> getCurrentWebElements(){
		// ִ�л�ȡ������ WebElement��JavaScript�ű�
		boolean javaScriptWasExecuted = executeJavaScriptFunction("allWebElements();");
		// ���˵��ǿɼ�WebElement,��������ʣ���
		return getSufficientlyShownWebElements(javaScriptWasExecuted);
	}

	/**
	 * ��ȡBy����ָ�����Ե�����WebElement
	 * Returns an ArrayList of WebElements of the specified By object currently shown in the active WebView.
	 * 
	 * @param by the By object. Examples are By.id("id") and By.name("name")
	 * @return an {@code ArrayList} of the {@link WebElement} objects currently shown in the active WebView 
	 */

	public ArrayList<WebElement> getCurrentWebElements(final By by){
		// ��ȡBy���Զ�Ӧ������WebElement
		boolean javaScriptWasExecuted = executeJavaScript(by, false);
		// ���ж�Ŀǰ��ûʹ��,2��·����ͬҵ���߼�
		if(config.useJavaScriptToClickWebElements){
			if(!javaScriptWasExecuted){
				return new ArrayList<WebElement>();
			}
			return webElementCreator.getWebElementsFromWebViews();
		}
		// ���˵��ǿɼ�WebElement,��������ʣ���
		return getSufficientlyShownWebElements(javaScriptWasExecuted);
	}

	/**
	 * ���˵��ǿɼ�WebElement,��������ʣ���
	 * Returns the sufficiently shown WebElements
	 * 
	 * @return the sufficiently shown WebElements
	 */

	private ArrayList<WebElement> getSufficientlyShownWebElements(boolean javaScriptWasExecuted){
		ArrayList<WebElement> currentWebElements = new ArrayList<WebElement>();
		// ��� JavaScript�Ƿ�ִ�гɹ�
		if(javaScriptWasExecuted){
			for(WebElement webElement : webElementCreator.getWebElementsFromWebViews()){
				if(isWebElementSufficientlyShown(webElement)){
					currentWebElements.add(webElement);
				}
			}
		}
		return currentWebElements;
	}

	/**
	 * ����JavaScriptִ�л���,�����ع���õ�JavaScript
	 * Prepares for start of JavaScript execution
	 * 
	 * @return the JavaScript as a String
	 */

	private String prepareForStartOfJavascriptExecution(){
		// ��ʼ��WebElement�洢����
		webElementCreator.prepareForStart();
		// ��ȡ��ǰ�汾Android��Ӧ��WebChromeClient
		WebChromeClient currentWebChromeClient = getCurrentWebChromeClient();
		// ����ԭ�е�WebChromeClient
		if(currentWebChromeClient != null && !currentWebChromeClient.getClass().isAssignableFrom(RobotiumWebClient.class)){
			originalWebChromeClient = getCurrentWebChromeClient();	
		}
		// ��ʼ�� Robotium���ư汾��WebChromeClient
		robotiumWebCLient.enableJavascriptAndSetRobotiumWebClient(viewFetcher.getCurrentViews(WebView.class), originalWebChromeClient);
		// ���ض�ȡ����RobotiumWeb.js�е�����
		return getJavaScriptAsString();
	}
	
	/**
	 * ��ȡ��ǰ��Android�汾��Ӧ��WebChromeClient
	 * Returns the current WebChromeClient through reflection
	 * 
	 * @return the current WebChromeClient
	 * 
	 */

	private WebChromeClient getCurrentWebChromeClient(){
		WebChromeClient currentWebChromeClient = null;
		// ��ȡ��ǰ���µ�WebView
		Object currentWebView = viewFetcher.getFreshestView(viewFetcher.getCurrentViews(WebView.class));
		// �߰汾���÷����ȡ
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			try{
				currentWebView = new Reflect(currentWebView).field("mProvider").out(Object.class);
			}catch(IllegalArgumentException ignored) {}
		}

		try{
			// �����ȡ��ض���
			Object mCallbackProxy = new Reflect(currentWebView).field("mCallbackProxy").out(Object.class);
			// ��ȡ���Բ�ת����WebChromeClient����
			currentWebChromeClient = new Reflect(mCallbackProxy).field("mWebChromeClient").out(WebChromeClient.class);
		}catch(Exception ignored){}

		return currentWebChromeClient;
	}

	/**
	 * ��ָ��������WebElement�����ı�
	 * Enters text into a web element using the given By method
	 * 
	 * @param by the By object e.g. By.id("id");
	 * @param text the text to enter
	 */

	public void enterTextIntoWebElement(final By by, final String text){
		// ���� Id����WebElement��������
		if(by instanceof By.Id){
			executeJavaScriptFunction("enterTextById(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� Xpath����WebElement��������
		else if(by instanceof By.Xpath){
			executeJavaScriptFunction("enterTextByXpath(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� CssSelector����WebElement��������
		else if(by instanceof By.CssSelector){
			executeJavaScriptFunction("enterTextByCssSelector(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� Name����WebElement��������
		else if(by instanceof By.Name){
			executeJavaScriptFunction("enterTextByName(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� ClassName����WebElement��������
		else if(by instanceof By.ClassName){
			executeJavaScriptFunction("enterTextByClassName(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� Text����WebElement��������
		else if(by instanceof By.Text){
			executeJavaScriptFunction("enterTextByTextContent(\""+by.getValue()+"\", \""+text+"\");");
		}
		// ���� TagName����WebElement��������
		else if(by instanceof By.TagName){
			executeJavaScriptFunction("enterTextByTagName(\""+by.getValue()+"\", \""+text+"\");");
		}
	}

	/**
	 * ����JavaScript.����by���Ͷ���Ӧ�� WebElement���в���
	 * shouldClick Ϊtrue��ʶ�����Ӧ��WebElement,�����ȡ��Ӧ��Element��Ϣ
	 * ����true��ʶִ�гɣ�falseִ���쳣
	 * Executes JavaScript determined by the given By object
	 * 
	 * @param by the By object e.g. By.id("id");
	 * @param shouldClick true if click should be performed
	 * @return true if JavaScript function was executed
	 */

	public boolean executeJavaScript(final By by, boolean shouldClick){
		// ƴ�Ӱ���Idִ�е�JavaScript�ű�
		if(by instanceof By.Id){
			return executeJavaScriptFunction("id(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���Xpathִ�е�JavaScript�ű�
		else if(by instanceof By.Xpath){
			return executeJavaScriptFunction("xpath(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���CssSelectorִ�е�JavaScript�ű�
		else if(by instanceof By.CssSelector){
			return executeJavaScriptFunction("cssSelector(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���Nameִ�е�JavaScript�ű�
		else if(by instanceof By.Name){
			return executeJavaScriptFunction("name(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���ClassNameִ�е�JavaScript�ű�
		else if(by instanceof By.ClassName){
			return executeJavaScriptFunction("className(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���Textִ�е�JavaScript�ű�
		else if(by instanceof By.Text){
			return executeJavaScriptFunction("textContent(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		// ƴ�Ӱ���TagNameִ�е�JavaScript�ű�
		else if(by instanceof By.TagName){
			return executeJavaScriptFunction("tagName(\""+by.getValue()+"\", \"" + String.valueOf(shouldClick) + "\");");
		}
		return false;
	}

	/**
	 * ��WebView��ִ��ָ����Javascript.ִ�гɹ�����true,���򷵻�false
	 * Executes the given JavaScript function
	 * 
	 * @param function the function as a String
	 * @return true if JavaScript function was executed
	 */

	private boolean executeJavaScriptFunction(final String function){
		// ��ȡ��ǰʱ�����µ�WebView
		final WebView webView = viewFetcher.getFreshestView(viewFetcher.getCurrentViews(WebView.class));
		// ��null���
		if(webView == null){
			return false;
		}
		// ��ȡJavaScript��Դ�ļ�����RoboTiumWeb.js�е�����
		final String javaScript = prepareForStartOfJavascriptExecution();
		// WebView�м������JavaScript
		activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable() {
			public void run() {
				if(webView != null){
					webView.loadUrl("javascript:" + javaScript + function);
				}
			}
		});
		return true;
	}

	/**
	 * ��鵱ǰWebElement�Ƿ�ɼ�
	 * Returns true if the view is sufficiently shown
	 *
	 * @param view the view to check
	 * @return true if the view is sufficiently shown
	 */

	public final boolean isWebElementSufficientlyShown(WebElement webElement){
		// ��ȡ��ǰ���µ� WebView
		final WebView webView = viewFetcher.getFreshestView(viewFetcher.getCurrentViews(WebView.class));
		// �洢WebView XY������Ϣ
		final int[] xyWebView = new int[2];

		if(webView != null && webElement != null){
			// ��ȡWebView XY������Ϣ
			webView.getLocationOnScreen(xyWebView);
			//  WebElement��WebView�⣬�򲻿ɼ�
			if(xyWebView[1] + webView.getHeight() > webElement.getLocationY())
				return true;
		}
		return false;
	}
	
	/**
	 * ���մ�д��ĸ�ָ��ַ��������ַ���֮����ӿո� ,��ת����Сд
	 * Splits a name by upper case.
	 * 
	 * @param name the name to split
	 * @return a String with the split name
	 * 
	 */

	public String splitNameByUpperCase(String name) {
		String [] texts = name.split("(?=\\p{Upper})");
		StringBuilder stringToReturn = new StringBuilder();

		for(String string : texts){

			if(stringToReturn.length() > 0) {
				stringToReturn.append(" " + string.toLowerCase());
			}
			else {
				stringToReturn.append(string.toLowerCase());
			}
		}
		return stringToReturn.toString();
	}

	/**
	 * ����Robotium.js�ļ�
	 * ��������ʽ��Ϣ,���\n���з�
	 * Returns the JavaScript file RobotiumWeb.js as a String
	 *  
	 * @return the JavaScript file RobotiumWeb.js as a {@code String} 
	 */

	private String getJavaScriptAsString() {
		InputStream fis = getClass().getResourceAsStream("RobotiumWeb.js");
		StringBuffer javaScript = new StringBuffer();

		try {
			BufferedReader input =  new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while (( line = input.readLine()) != null){
				javaScript.append(line);
				javaScript.append("\n");
			}
			input.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return javaScript.toString();
	}
}