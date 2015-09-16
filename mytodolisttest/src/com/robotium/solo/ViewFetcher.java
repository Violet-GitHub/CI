package com.robotium.solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * View��ȡ���������࣬�ṩ����������ȡview�ķ���
 * Contains view methods. Examples are getViews(),
 * getCurrentTextViews(), getCurrentImageViews().
 *
 * @author Renas Reda, renas.reda@robotium.com
 *
 */

class ViewFetcher {
	// activity������
	private final ActivityUtils activityUtils;
	// �洢windowManager�����������
	private String windowManagerString;

	/**
	 * ���캯������ʼ��ViewFetcher����
	 * Constructs this object.
	 *
	 * @param activityUtils the {@code ActivityUtils} instance
	 *
	 */

	public ViewFetcher(ActivityUtils activityUtils) {
		this.activityUtils = activityUtils;
		// ���ϵͳ�汾����ʼ��WindowManager�����������
		setWindowManagerString();
	}


	/**
	 * ��ȡview���mParent����
	 * Returns the absolute top parent {@code View} in for a given {@code View}.
	 *
	 * @param view the {@code View} whose top parent is requested
	 * @return the top parent {@code View}
	 */

	public View getTopParent(View view) {
		// �����ȡ��mParent�ǿգ�����mParent��View��ʵ������ô��������ֱ��Ϊ�ջ��߷�View��ʵ��
		if (view.getParent() != null
				&& view.getParent() instanceof android.view.View) {
			return getTopParent((View) view.getParent());
		} else {
			return view;
		}
	}


	/**
	 * �����б���߹�������mParent����,��View����������
	 * һ��ΪAbsListView ScrollView WebView
	 * ��ҪΪ��ȷ���ؼ�����
	 * Returns the scroll or list parent view
	 *
	 * @param view the view who's parent should be returned
	 * @return the parent scroll view, list view or null
	 */

	public View getScrollOrListParent(View view) {
		// view���Ǽ̳��� AbsListView ScrollView WebView ���������
	    if (!(view instanceof android.widget.AbsListView) && !(view instanceof android.widget.ScrollView) && !(view instanceof WebView)) {
	        try{
	            return getScrollOrListParent((View) view.getParent());
	        }catch(Exception e){
	            return null;
	        }
	    } else {
	        return view;
	    }
	}

	/**
	 * ��ȡ��ǰ�����ϵ����з�װ����View����
	 * onlySufficientlyVisible Ϊtrue��������еĲ��ɼ�����Ϊfalse�򲻿ɼ�����Ҳ����
	 * Returns views from the shown DecorViews.
	 *
	 * @param onlySufficientlyVisible if only sufficiently visible views should be returned
	 * @return all the views contained in the DecorViews
	 */

	public ArrayList<View> getAllViews(boolean onlySufficientlyVisible) {
		// ��ȡ��ǰ�����Ӧ��mViews����
		final View[] views = getWindowDecorViews();
		// ���� View���飬һ����List<View>
		final ArrayList<View> allViews = new ArrayList<View>();
		// views�����й��˵�DecorView
		final View[] nonDecorViews = getNonDecorViews(views);
		View view = null;
		// ��ȡ���з�DecorView������View����
		if(nonDecorViews != null){
			for(int i = 0; i < nonDecorViews.length; i++){
				view = nonDecorViews[i];
				try {
					// ������ȡ���е� View
					addChildren(allViews, (ViewGroup)view, onlySufficientlyVisible);
				} catch (Exception ignored) {}
				if(view != null) allViews.add(view);
			}
		}
		// ��ȡ���е�DecorView������View
		if (views != null && views.length > 0) {
			// ��ȡ���ѡ�е�View
			view = getRecentDecorView(views);
			try {
				// ������ȡ���е�View
				addChildren(allViews, (ViewGroup)view, onlySufficientlyVisible);
			} catch (Exception ignored) {}

			if(view != null) allViews.add(view);
		}

		return allViews;
	}

	/**
	 * ���˳�views�е� DecorView����
	 * Returns the most recent DecorView
	 *
	 * @param views the views to check
	 * @return the most recent DecorView
	 */

	 public final View getRecentDecorView(View[] views) {
		 if(views == null)
			 return null;
		 
		 final View[] decorViews = new View[views.length];
		 int i = 0;
		 View view;

		 for (int j = 0; j < views.length; j++) {
			 view = views[j];
			 // ��ȡ DecorView����
			 if (view != null && view.getClass().getName()
					 .equals("com.android.internal.policy.impl.PhoneWindow$DecorView")) {
				 decorViews[i] = view;
				 i++;
			 }
		 }
		 return getRecentContainer(decorViews);
	 }

	/**
	 * ��ȡ��ǰ�Ľ���View
	 * Returns the most recent view container
	 *
	 * @param views the views to check
	 * @return the most recent view container
	 */

	 private final View getRecentContainer(View[] views) {
		 View container = null;
		 long drawingTime = 0;
		 View view;

		 for(int i = 0; i < views.length; i++){
			 view = views[i];
			 // ���տؼ��Ƿ�ѡ�кͻ���ʱ���ж��Ƿ����µ�
			 if (view != null && view.isShown() && view.hasWindowFocus() && view.getDrawingTime() > drawingTime) {
				 // ������ʱ����ֵ
				 container = view;
				 // ������ʱ����ֵ
				 drawingTime = view.getDrawingTime();
			 }
		 }
		 return container;
	 }

	 /**
	  * �������еķ�װ����View
	  * Returns all views that are non DecorViews
	  *
	  * @param views the views to check
	  * @return the non DecorViews
	  */

	 private final View[] getNonDecorViews(View[] views) {
		 View[] decorViews = null;

		 if(views != null) {
			 decorViews = new View[views.length];

			 int i = 0;
			 View view;

			 for (int j = 0; j < views.length; j++) {
				 view = views[j];
				 // ��������DecorView������뷵������
				 if (view != null && !(view.getClass().getName()
						 .equals("com.android.internal.policy.impl.PhoneWindow$DecorView"))) {
					 decorViews[i] = view;
					 i++;
				 }
			 }
		 }
		 return decorViews;
	 }



	/**
	 * ��ȡ����View�е����а�����View����parent
	 * parent Ϊ����Ĭ�Ϸ��ص�ǰ�������е�View
	 * onlySufficientlyVisible Ϊtrue�򷵻����пɱ�Clicker�����View,Ϊfalse�򲻽��й���ȫ������
	 * Extracts all {@code View}s located in the currently active {@code Activity}, recursively.
	 *
	 * @param parent the {@code View} whose children should be returned, or {@code null} for all
	 * @param onlySufficientlyVisible if only sufficiently visible views should be returned
	 * @return all {@code View}s located in the currently active {@code Activity}, never {@code null}
	 */

	public ArrayList<View> getViews(View parent, boolean onlySufficientlyVisible) {
		final ArrayList<View> views = new ArrayList<View>();
		final View parentToUse;
		// �����viewΪ�գ����յ�ǰ�������
		if (parent == null){
			return getAllViews(onlySufficientlyVisible);
		}else{
			parentToUse = parent;
			// �Ȱ��Լ������
			views.add(parentToUse);
			// ���������ViewGroup���͵ģ���ô�������е�View
			if (parentToUse instanceof ViewGroup) {
				addChildren(views, (ViewGroup) parentToUse, onlySufficientlyVisible);
			}
		}
		return views;
	}

	/**
	 * ����ViewGroup�е�����View
	 * onlySufficientlyVisible Ϊtrue�򷵻����е�ʹ�� Clicker���Ե����view,Ϊfalse�򷵻����б�������View
	 * Adds all children of {@code viewGroup} (recursively) into {@code views}.
	 *
	 * @param views an {@code ArrayList} of {@code View}s
	 * @param viewGroup the {@code ViewGroup} to extract children from
	 * @param onlySufficientlyVisible if only sufficiently visible views should be returned
	 */

	private void addChildren(ArrayList<View> views, ViewGroup viewGroup, boolean onlySufficientlyVisible) {
		if(viewGroup != null){
			// ����ViewGroup
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				final View child = viewGroup.getChildAt(i);
				// �������Clicker�ɵ������
				if(onlySufficientlyVisible && isViewSufficientlyShown(child))
					views.add(child);
				// ����עview�Ƿ����ͨ��Clicker�����ȫ����ȡ
				else if(!onlySufficientlyVisible)
					views.add(child);
				// �������ViewGroup���е�������
				if (child instanceof ViewGroup) {
					addChildren(views, (ViewGroup) child, onlySufficientlyVisible);
				}
			}
		}
	}

	/**
	 * ���View�ǿɼ��ģ���ô����true,���򷵻�false
	 * �������������б������Կɼ�������ڵ��ڿؼ������1/2���ж�
	 * �� Click�����ǵ��View��������λ�ã���˸�λ�ò��ɼ��ᵼ���޷����
	 * Returns true if the view is sufficiently shown
	 *
	 * @param view the view to check
	 * @return true if the view is sufficiently shown
	 */

	public final boolean isViewSufficientlyShown(View view){
		// �洢View��xy����
		final int[] xyView = new int[2];
		// �洢View������xy����
		final int[] xyParent = new int[2];

		if(view == null)
			return false;
		// ��ȡView�ĸ߶ȣ����ո߶��ж��Ƿ�ɼ�
		final float viewHeight = view.getHeight();
		// ��ȡ View�ĸ�����
		final View parent = getScrollOrListParent(view);
		// ��ȡ view��XY����
		view.getLocationOnScreen(xyView);
		// �����������������ô������0
		if(parent == null){
			xyParent[1] = 0;
		}
		// ���������������ȡ��������xy����
		else{
			parent.getLocationOnScreen(xyParent);
		}
		// ���view�������пɼ�����С�������������һ�㣬��ô�ж�Ϊ���ɼ�����Ϊ�߶ȵ����޺������ж�
		if(xyView[1] + (viewHeight/2.0f) > getScrollListWindowHeight(view))
			return false;

		else if(xyView[1] + (viewHeight/2.0f) < xyParent[1])
			return false;

		return true;
	}

	/**
	 * ��ȡ�ɻ������������б������ĸ߶�����
	 * Returns the height of the scroll or list view parent
	 * @param view the view who's parents height should be returned
	 * @return the height of the scroll or list view parent
	 */

	@SuppressWarnings("deprecation")
	public float getScrollListWindowHeight(View view) {
		final int[] xyParent = new int[2];
		// ��ȡ��������������
		View parent = getScrollOrListParent(view);
		final float windowHeight;
		// �����������������ôֱ�ӻ�ȡ��ǰActivity�ĸ߶�
		if(parent == null){
			windowHeight = activityUtils.getCurrentActivity(false).getWindowManager()
			.getDefaultDisplay().getHeight();
		}
		// ����߶�Ϊ��������+��ǰ�����ĸ߶�
		else{
			parent.getLocationOnScreen(xyParent);
			windowHeight = xyParent[1] + parent.getHeight();
		}
		// �ͷŶ���
		parent = null;
		return windowHeight;
	}


	/**
	 * ���ո����Ĺ������ͻ�ȡ���и����͵�View
	 * classToFilterBy ������
	 * Returns an {@code ArrayList} of {@code View}s of the specified {@code Class} located in the current
	 * {@code Activity}.
	 *
	 * @param classToFilterBy return all instances of this class, e.g. {@code Button.class} or {@code GridView.class}
	 * @return an {@code ArrayList} of {@code View}s of the specified {@code Class} located in the current {@code Activity}
	 */

	public <T extends View> ArrayList<T> getCurrentViews(Class<T> classToFilterBy) {
		return getCurrentViews(classToFilterBy, null);
	}

	/**
	 * ���ո������͵�class,����View�ж�Ӧ��View
	 * Returns an {@code ArrayList} of {@code View}s of the specified {@code Class} located under the specified {@code parent}.
	 *
	 * @param classToFilterBy return all instances of this class, e.g. {@code Button.class} or {@code GridView.class}
	 * @param parent the parent {@code View} for where to start the traversal
	 * @return an {@code ArrayList} of {@code View}s of the specified {@code Class} located under the specified {@code parent}
	 */

	public <T extends View> ArrayList<T> getCurrentViews(Class<T> classToFilterBy, View parent) {
		ArrayList<T> filteredViews = new ArrayList<T>();
		List<View> allViews = getViews(parent, true);
		for(View view : allViews){
			// ����class����������,��������ת��
			if (view != null && classToFilterBy.isAssignableFrom(view.getClass())) {
				filteredViews.add(classToFilterBy.cast(view));
			}
		}
		// �ͷŶ���
		allViews = null;
		return filteredViews;
	}

	
	/**
	 * ���ظ���views�е����¿ɼ�View
	 * Tries to guess which view is the most likely to be interesting. Returns
	 * the most recently drawn view, which presumably will be the one that the
	 * user was most recently interacting with.
	 *
	 * @param views A list of potentially interesting views, likely a collection
	 *            of views from a set of types, such as [{@link Button},
	 *            {@link TextView}] or [{@link ScrollView}, {@link ListView}]
	 * @param index the index of the view
	 * @return most recently drawn view, or null if no views were passed 
	 */

	public final <T extends View> T getFreshestView(ArrayList<T> views){
		// ��ʱ�����洢xy����
		final int[] locationOnScreen = new int[2];
		T viewToReturn = null;
		long drawingTime = 0;
		if(views == null){
			return null;
		}
		for(T view : views){
			// ��ȡxy����
			view.getLocationOnScreen(locationOnScreen);

			if (locationOnScreen[0] < 0 ) 
				continue;
			// �����ҳ����µ�
			if(view.getDrawingTime() > drawingTime && view.getHeight() > 0){
				drawingTime = view.getDrawingTime();
				viewToReturn = view;
			}
		}
		views = null;
		return viewToReturn;
	}
	// WindowManager�����ṩ������app����view�����ȡ����
	private static Class<?> windowManager;
	static{
		try {
			String windowManagerClassName;
			// ����Android�汾���ж϶�Ӧ������
			if (android.os.Build.VERSION.SDK_INT >= 17) {
				windowManagerClassName = "android.view.WindowManagerGlobal";
			} else {
				windowManagerClassName = "android.view.WindowManagerImpl"; 
			}
			// ͨ�������ȡ�����
 			windowManager = Class.forName(windowManagerClassName);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��ǰ���������װ������
	 * Returns the WindorDecorViews shown on the screen.
	 * 
	 * @return the WindorDecorViews shown on the screen
	 */

	@SuppressWarnings("unchecked")
	public View[] getWindowDecorViews()
	{

		Field viewsField;
		Field instanceField;
		try {
			// �����ȡmViews����
			viewsField = windowManager.getDeclaredField("mViews");
			// �����ȡWindowMager����
			instanceField = windowManager.getDeclaredField(windowManagerString);
			// �޸������������ĳɿɷ���
			viewsField.setAccessible(true);
			// �޸������������ĳɿɷ���
			instanceField.setAccessible(true);
			// �����ȡwindowManager����
			Object instance = instanceField.get(null);
			View[] result;
			if (android.os.Build.VERSION.SDK_INT >= 19) {
				// ��ȡmViews��������,��View[]
				result = ((ArrayList<View>) viewsField.get(instance)).toArray(new View[0]);
			} else {
				// ��ȡmViews��������,��View[]
				result = (View[]) viewsField.get(instance);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �жϵ�ǰAndroid�汾��Ӧ��WindowManager�����ֶ���
	 * Sets the window manager string.
	 */
	private void setWindowManagerString(){

		if (android.os.Build.VERSION.SDK_INT >= 17) {
			windowManagerString = "sDefaultWindowManager";
			
		} else if(android.os.Build.VERSION.SDK_INT >= 13) {
			windowManagerString = "sWindowManager";

		} else {
			windowManagerString = "mWindowManager";
		}
	}


}