package com.robotium.solo;

import java.util.List;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * Robotium WebView����������,��չWebChromeClient
 * Robotium��Ҫ����WebView,�����Ҫ�ٳ�WebView�� JSִ�У���дonJsPrompt,��ȡWebView�����Ԫ��
 * WebChromeClient used to get information on web elements by injections of JavaScript. 
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class RobotiumWebClient extends WebChromeClient{
	// ���ڹ���WebElement�Ĺ�����
	WebElementCreator webElementCreator;
	// Instrument,���ڷ��͸����¼�
	private Instrumentation inst;
	// robotium��չ��client
	private WebChromeClient robotiumWebClient;
	// ԭ����client
	private WebChromeClient originalWebChromeClient = null;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param instrumentation the {@code Instrumentation} instance
	 * @param webElementCreator the {@code WebElementCreator} instance
	 */

	public RobotiumWebClient(Instrumentation inst, WebElementCreator webElementCreator){
		this.inst = inst;
		this.webElementCreator = webElementCreator;
		robotiumWebClient = this;
	}

	/**
	 * ����WebView��ִ��javaScript,����WebView������Ҫ��JavaScript���.
	 * Enables JavaScript in the given {@code WebViews} objects.
	 * 
	 * @param webViews the {@code WebView} objects to enable JavaScript in
	 */

	public void enableJavascriptAndSetRobotiumWebClient(List<WebView> webViews, WebChromeClient originalWebChromeClient){
		// ����ԭ�е�ChromeClient.������Ҫԭ������ʱʹ��
		this.originalWebChromeClient = originalWebChromeClient;

		for(final WebView webView : webViews){

			if(webView != null){ 
				inst.runOnMainSync(new Runnable() {
					public void run() {
						// ���ÿ�ִ��js
						webView.getSettings().setJavaScriptEnabled(true);
						// ����ʹ�� Robotium���Ƶ�WebClient
						webView.setWebChromeClient(robotiumWebClient);

					}
				});
			}
		}
	}

	/**
	 * ��дjsִ�д�����,robotiumʹ�õ�ͨ��js��promptҲ��������Ԫ����Ϣ,�����д�ķ���
	 * Overrides onJsPrompt in order to create {@code WebElement} objects based on the web elements attributes prompted by the injections of JavaScript
	 */

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,	String defaultValue, JsPromptResult r) {
		// ����robotiumִ�е�js�������⴦��,����jsִ�з�����Ϣ����������ص�WebElement��Ϣ
		if(message != null && (message.contains(";,") || message.contains("robotium-finished"))){
			// ִ����������ý������
			if(message.equals("robotium-finished")){
				webElementCreator.setFinished(true);
			}
			else{
				webElementCreator.createWebElementAndAddInList(message, view);
			}
			// ֱ��ȷ�ϵ�������Ӱ��ҳ��
			r.confirm();
			return true;
		}
		// ��robotium���е�js,ʹ��Ĭ���߼�����
		else {
			if(originalWebChromeClient != null) {
				return originalWebChromeClient.onJsPrompt(view, url, message, defaultValue, r); 
			}
			return true;
		}

	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public Bitmap getDefaultVideoPoster() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.getDefaultVideoPoster();
		} 
		return null;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public View getVideoLoadingProgressView() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.getVideoLoadingProgressView();
		} 
		return null;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.getVisitedHistory(callback);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onCloseWindow(WebView window) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onCloseWindow(window);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
		}
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {       
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onConsoleMessage(consoleMessage);
		} 
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		} 
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
			long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onGeolocationPermissionsHidePrompt() {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onGeolocationPermissionsHidePrompt();
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onHideCustomView() {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onHideCustomView();
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsAlert(view, url, message, result);
		} 
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient.onJsBeforeUnload(view, url, message, result)) {
			return originalWebChromeClient.onJsBeforeUnload(view, url, message, result);
		}
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsConfirm(view, url, message, result);
		} 
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public boolean onJsTimeout() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsTimeout();
		} 
		return true;
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (originalWebChromeClient != null) {            
			originalWebChromeClient.onProgressChanged(view, newProgress);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedIcon(view, icon);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onReceivedTitle(WebView view, String title) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedTitle(view, title);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
		} 
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onRequestFocus(WebView view) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onRequestFocus(view);
		}
	}
	/**
	 * ��д����������ԭ����
	 */
	@Override
	public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onShowCustomView(view, callback);
		} 
	}
}