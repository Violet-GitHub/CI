package com.robotium.solo;

import junit.framework.Assert;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


/**
 * ����ָ��������ȡView����������һЩ��Ϣ
 * Contains various get methods. Examples are: getView(int id),
 * getView(Class<T> classToFilterBy, int index).
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Getter {
	// Instrument,���ڷ����¼�
	private final Instrumentation instrumentation;
	// activity������
	private final ActivityUtils activityUtils;
	// View�ȴ�������
	private final Waiter waiter;
	// 1s
	private final int TIMEOUT = 1000;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param inst the {@code Instrumentation} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param waiter the {@code Waiter} instance
	 */

	public Getter(Instrumentation instrumentation, ActivityUtils activityUtils, Waiter waiter){
		this.instrumentation = instrumentation;
		this.activityUtils = activityUtils;
		this.waiter = waiter;
	}


	/**
	 * ��ȡָ�����͵ĵ�index��View���Ҳ�������null
	 * Returns a {@code View} with a certain index, from the list of current {@code View}s of the specified type.
	 *
	 * @param classToFilterBy which {@code View}s to choose from
	 * @param index choose among all instances of this type, e.g. {@code Button.class} or {@code EditText.class}
	 * @return a {@code View} with a certain index, from the list of current {@code View}s of the specified type
	 */

	public <T extends View> T getView(Class<T> classToFilterBy, int index) {
		//��ȡָ��class���͵ĵ�index��View,Ĭ�ϳ�ʱ10s,10s��δ�ҵ�����null 
		return waiter.waitForAndGetView(index, classToFilterBy);
	}

	/**
	 * ��ȡָ��class���ͺ�text�ĵ�1��view,�Ҳ�����ʾ�쳣.�������Ƿ�ֻ���ҿɼ���
	 * onlyVisible true ֻ�ҿɼ���,false �������е� 
	 * Returns a {@code View} that shows a given text, from the list of current {@code View}s of the specified type.
	 *
	 * @param classToFilterBy which {@code View}s to choose from
	 * @param text the text that the view shows
	 * @param onlyVisible {@code true} if only visible texts on the screen should be returned
	 * @return a {@code View} showing a given text, from the list of current {@code View}s of the specified type
	 */

	public <T extends TextView> T getView(Class<T> classToFilterBy, String text, boolean onlyVisible) {
		// ��ȡָ��class���ͺ�text�ĵ�1��view,Ĭ�϶̳�ʱ
		T viewToReturn = (T) waiter.waitForText(classToFilterBy, text, 0, Timeout.getSmallTimeout(), false, onlyVisible, false);
		// δ�ҵ���ʾ�쳣
		if(viewToReturn == null)
			Assert.fail(classToFilterBy.getSimpleName() + " with text: '" + text + "' is not found!");

		return viewToReturn;
	}

	/**
	 * ����ָ����Դid����ȡ��ǰactivity�е� String
	 * Returns a localized string
	 * 
	 * @param id the resource ID for the string
	 * @return the localized string
	 */

	public String getString(int id)
	{
		// ��ȡ��ǰactivity
		Activity activity = activityUtils.getCurrentActivity(false);
		// ����id��Ӧ�Ե�string
		return activity.getString(id);
	}

	/**
	 * ����ָ����Դid����ȡ��ǰactivity��String.
     *
	 * Returns a localized string
	 * 
	 * @param id the resource ID for the string
	 * @return the localized string
	 */

	public String getString(String id)
	{
		// ��String���͵ı�ʶ�����ɶ�Ӧ��Int��Id�ٴӵ�ǰactivity���Ҷ�Ӧ��String
		// ��ȡ Context
		Context targetContext = instrumentation.getTargetContext(); 
		// ��ȡӦ����
		String packageName = targetContext.getPackageName(); 
		// ����String����id��ѯ��Ӧ��int id,���ڵ�ǰӦ���в飬�Ҳ���������android�в�
		int viewId = targetContext.getResources().getIdentifier(id, "string", packageName);
		if(viewId == 0){
			viewId = targetContext.getResources().getIdentifier(id, "string", "android");
		}
		// ����ָ����Դid����ȡ��ǰactivity�е� String
		return getString(viewId);		
	}
	
	/**
	 * ��ȡָ��id�ĵ�index�� View�������õ�indexС��1����ô���ص�ǰactivity��idΪ 0��view.
	 * �����ó�ʱʱ��,�����ʱʱ������Ϊ0,��Ĭ�ϸĳ�10s,����Ϊ��ֵ��ֱ�ӷ���null
	 *  
	 * Returns a {@code View} with a given id.
	 * 
	 * @param id the R.id of the {@code View} to be returned
	 * @param index the index of the {@link View}. {@code 0} if only one is available
	 * @param timeout the timeout in milliseconds
	 * @return a {@code View} with a given id
	 */

	public View getView(int id, int index, int timeout){
		// ��ȡ��ǰactivity
		final Activity activity = activityUtils.getCurrentActivity(false);
		View viewToReturn = null;
		// ����index С��1��Ĭ�Ϸ��ص�ǰactivity�е�idΪ0��view
		if(index < 1){
			index = 0;
			viewToReturn = activity.findViewById(id);
		}
		// ����ҵ��ˣ��򷵻أ����������Ƕ�������if���У��Ϻ����
		if (viewToReturn != null) {
			return viewToReturn;
		}
		// ��ȡָ��id��ָ��������view���֣������ó�ʱʱ��,�����ʱʱ������Ϊ0,��Ĭ�ϸĳ�10s,����Ϊ��ֵ��ֱ�ӷ���null 
		return waiter.waitForView(id, index, timeout);
	}

	/**
	 * ��ȡָ��id�ĵ�index�� View�������õ�indexС��1����ô���ص�ǰactivity��idΪ 0��view.Ĭ�ϳ�ʱ10s
	 * Returns a {@code View} with a given id.
	 * 
	 * @param id the R.id of the {@code View} to be returned
	 * @param index the index of the {@link View}. {@code 0} if only one is available
	 * @return a {@code View} with a given id
	 */

	public View getView(int id, int index){
		return getView(id, index, 0);
	}

	/**
	 * ��ȡָ��id�ĵ�index�� View�������õ�indexС��1����ô���ص�ǰactivity��idΪ 0��view.Ĭ�ϳ�ʱ1s
	 * ��String���͵ı�ʶ�����ɶ�Ӧ��Int��Id�ٴӵ�ǰactivity���Ҷ�Ӧ��View
	 * 
	 * Returns a {@code View} with a given id.
	 * 
	 * @param id the id of the {@link View} to return
	 * @param index the index of the {@link View}. {@code 0} if only one is available
	 * @return a {@code View} with a given id
	 */

	public View getView(String id, int index){
		// ��String���͵ı�ʶ�����ɶ�Ӧ��Int��Id 
		View viewToReturn = null;
		// ��ȡӦ��������
		Context targetContext = instrumentation.getTargetContext(); 
		// ��ȡӦ����
		String packageName = targetContext.getPackageName(); 
		// ����String����id��ѯ��Ӧ��int id,���ڵ�ǰӦ���в�
		int viewId = targetContext.getResources().getIdentifier(id, "id", packageName);
		// ��ѯ��Ӧ��view
		if(viewId != 0){
			viewToReturn = getView(viewId, index, TIMEOUT); 
		}
		// ���δ�ҵ�����id������android��Ӧ�ļ�������
		if(viewToReturn == null){
			int androidViewId = targetContext.getResources().getIdentifier(id, "id", "android");
			// ������Ի�ȡ��Ӧ��id ��������
			if(androidViewId != 0){
				viewToReturn = getView(androidViewId, index, TIMEOUT);
			}
		}
		// �ҵ���ֱ�ӷ���
		if(viewToReturn != null){
			return viewToReturn;
		}
		// δ�ҵ�������id Ϊ0��������
		return getView(viewId, index); 
	}
}