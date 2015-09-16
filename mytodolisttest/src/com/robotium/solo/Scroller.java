package com.robotium.solo;

import java.util.ArrayList;

import com.robotium.solo.Solo.Config;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * ����������������
 * Contains scroll methods. Examples are scrollDown(), scrollUpList(),
 * scrollToSide().
 *
 * @author Renas Reda, renas.reda@robotium.com
 *
 */

class Scroller {
	// ����
	public static final int DOWN = 0;
	// ����
	public static final int UP = 1;
	// ���� ö��
	public enum Side {LEFT, RIGHT}
	// �Ƿ�����϶�
	private boolean canScroll = false;
	// Instrument����
	private final Instrumentation inst;
	// Activity������
	private final ActivityUtils activityUtils;
	// View��ȡ������
	private final ViewFetcher viewFetcher;
	// ��ʱ������
	private final Sleeper sleeper;
	// Robotium����������
	private final Config config;


	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param inst the {@code Instrumentation} instance
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param sleeper the {@code Sleeper} instance
	 */

	public Scroller(Config config, Instrumentation inst, ActivityUtils activityUtils, ViewFetcher viewFetcher, Sleeper sleeper) {
		this.config = config;
		this.inst = inst;
		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		this.sleeper = sleeper;
	}


	/**
	 * ��ס�����϶���ָ��λ��
	 * fromX ��ʼX����
	 * toX   �յ�X����
	 * fromY ��ʼY����
	 * toY   �յ�Y����
	 * stepCount ������ֳɼ���
	 * Simulate touching a specific location and dragging to a new location.
	 *
	 * This method was copied from {@code TouchUtils.java} in the Android Open Source Project, and modified here.
	 *
	 * @param fromX X coordinate of the initial touch, in screen coordinates
	 * @param toX Xcoordinate of the drag destination, in screen coordinates
	 * @param fromY X coordinate of the initial touch, in screen coordinates
	 * @param toY Y coordinate of the drag destination, in screen coordinates
	 * @param stepCount How many move steps to include in the drag
	 */

	public void drag(float fromX, float toX, float fromY, float toY,
			int stepCount) {
		// ��ȡ��ǰϵͳʱ�䣬����MontionEventʹ��
		long downTime = SystemClock.uptimeMillis();
		// ��ȡ��ǰϵͳʱ�䣬����MontionEventʹ��
		long eventTime = SystemClock.uptimeMillis();
		float y = fromY;
		float x = fromX;
		// ����ÿ������Y������
		float yStep = (toY - fromY) / stepCount;
		// ����ÿ������X������
		float xStep = (toX - fromX) / stepCount;
		// ����MotionEvent,�Ȱ�ס
		MotionEvent event = MotionEvent.obtain(downTime, eventTime,MotionEvent.ACTION_DOWN, fromX, fromY, 0);
		try {
			// ͨ��Instrument���Ͱ�ס�¼�
			inst.sendPointerSync(event);
			// ץȡ���ܳ��ֵ��쳣
		} catch (SecurityException ignored) {}
		// �������õĲ���������Move�¼�
		for (int i = 0; i < stepCount; ++i) {
			y += yStep;
			x += xStep;
			eventTime = SystemClock.uptimeMillis();
			// ���� MOVE�¼�
			event = MotionEvent.obtain(downTime, eventTime,MotionEvent.ACTION_MOVE, x, y, 0);
			try {
				// ͨ��Instrument���Ͱ�ס�¼�
				inst.sendPointerSync(event);
				// ץȡ���ܳ��ֵ��쳣
			} catch (SecurityException ignored) {}
		}
		// ��ȡϵͳ��ǰʱ��
		eventTime = SystemClock.uptimeMillis();
		// �����ɿ��¼�
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP,toX, toY, 0);
		try {
			inst.sendPointerSync(event);
		} catch (SecurityException ignored) {}
	}


	/**
	 * �����趨�ķ����϶�������,�Ѿ����ڶ����ģ������϶���������Ч
	 * view  ����������View�ؼ�
	 * 
	 * Scrolls a ScrollView.
	 * direction �϶����� 0 ��������������,1��������������
	 * @param direction the direction to be scrolled
	 * @return {@code true} if scrolling occurred, false if it did not
	 */

	private boolean scrollScrollView(final ScrollView view, int direction){
		// null ��飬�Ƚϴ���null���������쳣
		if(view == null){
			return false;
		}
		// ��ȡ�ؼ��ĸ߶�
		int height = view.getHeight();
		// �߶ȼ�Сһ������
		height--;
		int scrollTo = -1;
		// �������������óɹ������ĸ߶�,��������
		if (direction == DOWN) {
			scrollTo = height;
		}
		// �������������óɸ�ֵ,�����ײ�
		else if (direction == UP) {
			scrollTo = -height;
		}
		// ��ȡ��ǰ�����ĸ߶�λ��
		int originalY = view.getScrollY();
		final int scrollAmount = scrollTo;
		inst.runOnMainSync(new Runnable(){
			public void run(){
				view.scrollBy(0, scrollAmount);
			}
		});
		// ����������δ�仯����ʶ�����϶�����ʧ��.�Ѿ����ڶ����ˣ�������Ч��
		if (originalY == view.getScrollY()) {
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * �����������ײ����߶������Ѿ����ڶ��������ø÷����϶���������������ѭ��
	 * Scrolls a ScrollView to top or bottom.
	 *
	 * @param direction the direction to be scrolled
	 */

	private void scrollScrollViewAllTheWay(final ScrollView view, final int direction) {
		while(scrollScrollView(view, direction));
	}

	/**
	 * �϶����������ߵײ�,0�϶�������,1�϶����ײ�
	 * Scrolls up or down.
	 *
	 * @param direction the direction in which to scroll
	 * @return {@code true} if more scrolling can be done
	 */

	public boolean scroll(int direction) {
		return scroll(direction, false);
	}

	/**
	 * �϶�������
	 * Scrolls down.
	 *
	 * @return {@code true} if more scrolling can be done
	 */

	public boolean scrollDown() {
		// ������������˽�ֹ�϶�����ô�����϶��ؼ�
		if(!config.shouldScroll) {
			return false;
		}
		// �϶�������
		return scroll(Scroller.DOWN);
	}

	/**
	 * �϶���ǰҳ��Ŀ��϶��ؼ�
	 * direction  0�϶�������,1�϶����ײ�
	 * Scrolls up and down.
	 *
	 * @param direction the direction in which to scroll
	 * @param allTheWay <code>true</code> if the view should be scrolled to the beginning or end,
	 *                  <code>false</code> to scroll one page up or down.
	 * @return {@code true} if more scrolling can be done
	 */

	public boolean scroll(int direction, boolean allTheWay) {
		// ��ȡ���е�Clicker�ɲ���Views
		final ArrayList<View> viewList = RobotiumUtils.
				removeInvisibleViews(viewFetcher.getAllViews(true));
		// ��ȡ���п����϶�������views
		@SuppressWarnings("unchecked")
		ArrayList<View> views = RobotiumUtils.filterViewsToSet(new Class[] { ListView.class,
				ScrollView.class, GridView.class, WebView.class}, viewList);
		// ��ȡ���п���view�е����µģ�����ǰ�û�ѡ�еĿ��϶��ؼ�
		View view = viewFetcher.getFreshestView(views);
		// ����޿��϶��ؼ����򷵻�
		if (view == null)
		{
			return false;
		}
		// ��һ���б�ؼ�����ʹ���б�ؼ���������
		if (view instanceof AbsListView) {
			return scrollList((AbsListView)view, direction, allTheWay);
		}
		// �����һ�����϶��ؼ������տ��϶��ؼ���������
		if (view instanceof ScrollView) {
			if (allTheWay) {
				scrollScrollViewAllTheWay((ScrollView) view, direction);
				return false;
			} else {
				return scrollScrollView((ScrollView)view, direction);
			}
		}
		// �����һ��WebView�ؼ�������WebView��������
		if(view instanceof WebView){
			return scrollWebView((WebView)view, direction, allTheWay);
		}
		// �������ؼ����ͣ�����false
		return false;
	}
	
	/**
	 * WebView �ؼ� �϶�����.
	 * Scrolls a WebView.
	 * 
	 * webView   �����WebView
	 * direction ��������0�϶���������1�϶����ײ�
	 * allTheWay  true��ʶ�϶����ײ��򶥲���false��ʶ���϶�
	 * �¼����ͳɹ�����true ʧ�ܷ���false
	 * @param webView the WebView to scroll
	 * @param direction the direction to scroll
	 * @param allTheWay {@code true} to scroll the view all the way up or down, {@code false} to scroll one page up or down                          or down.
	 * @return {@code true} if more scrolling can be done
	 */
	
	public boolean scrollWebView(final WebView webView, int direction, final boolean allTheWay){

		if (direction == DOWN) {
			// ����Instrument�����϶��¼�
			inst.runOnMainSync(new Runnable(){
				public void run(){
					// �϶����ײ�
					canScroll =  webView.pageDown(allTheWay);
				}
			});
		}
		if(direction == UP){
			// ����Instrument�����϶��¼�
			inst.runOnMainSync(new Runnable(){
				public void run(){
					// �϶����ײ�
					canScroll =  webView.pageUp(allTheWay);
				}
			});
		}
		// �����¼������Ƿ�ɹ�
		return canScroll;
	}

	/**
	 * �϶�һ���б�
	 * Scrolls a list.
	 * absListView AbsListView���͵ģ����б���ؼ�
	 * direction   �϶�����0�����1��ײ�
	 * @param absListView the list to be scrolled
	 * @param direction the direction to be scrolled
	 * @param allTheWay {@code true} to scroll the view all the way up or down, {@code false} to scroll one page up or down
	 * @return {@code true} if more scrolling can be done
	 */

	public <T extends AbsListView> boolean scrollList(T absListView, int direction, boolean allTheWay) {
		// ��nullУ��
		if(absListView == null){
			return false;
		}
		// �϶����ײ�
		if (direction == DOWN) {
			// �����ֱ���϶����ײ���ģʽ
			if (allTheWay) {
				// �϶������ŵ���,����������������ڿ�����������˵��ô˷��������÷���false
				scrollListToLine(absListView, absListView.getCount()-1);
				return false;
			}
			// ���������ȿɼ�������ʱ.�϶����ɼ������ײ�������false.
			if (absListView.getLastVisiblePosition() >= absListView.getCount()-1) {
				scrollListToLine(absListView, absListView.getLastVisiblePosition());
				return false;
			}
			// ������һ��ʱ���϶������������
			if(absListView.getFirstVisiblePosition() != absListView.getLastVisiblePosition())
				scrollListToLine(absListView, absListView.getLastVisiblePosition());

			else
				// ���ɼ���ֻ��һ��ʱ���϶�������һ��
				scrollListToLine(absListView, absListView.getFirstVisiblePosition()+1);
			// �϶�������
		} else if (direction == UP) {
			// �ɼ���������1��ʱ��ֱ�ӻ�����0��
			if (allTheWay || absListView.getFirstVisiblePosition() < 2) {
				scrollListToLine(absListView, 0);
				return false;
			}
			// ������ʾ������.û��Ҫ���ó�final,�ֲ���������ʹ��
			final int lines = absListView.getLastVisiblePosition() - absListView.getFirstVisiblePosition();
			// ����δ��ʾ��ʣ������ȫ����ʾ�������
			int lineToScrollTo = absListView.getFirstVisiblePosition() - lines;
			// ������ÿ�����ʾ�����뵱ǰ�ײ�λ��һ��,���ƶ�����ǰλ��
			if(lineToScrollTo == absListView.getLastVisiblePosition())
				lineToScrollTo--;
			// �������λ��Ϊ��ֵ����ôֱ�ӻ�������
			if(lineToScrollTo < 0)
				lineToScrollTo = 0;

			scrollListToLine(absListView, lineToScrollTo);
		}
		sleeper.sleep();
		return true;
	}


	/**
	 * �϶��б����ݵ�ָ������
	 * line ��Ӧ���к�
	 * Scroll the list to a given line
	 *
	 * @param view the {@link AbsListView} to scroll
	 * @param line the line to scroll to
	 */

	public <T extends AbsListView> void scrollListToLine(final T view, final int line){
		// ��nullУ��
		if(view == null)
			Assert.fail("AbsListView is null!");

		final int lineToMoveTo;
		// �����gridview���͵ģ������⣬�������+1
		if(view instanceof GridView)
			lineToMoveTo = line+1;
		else
			lineToMoveTo = line;
		// �����϶��¼�
		inst.runOnMainSync(new Runnable(){
			public void run(){
				view.setSelection(lineToMoveTo);
			}
		});
	}


	/**
	 * �����϶�,�϶�Ĭ�ϲ�ֳ�40������
	 * side           ָ���϶�����
	 * scrollPosition �϶��ٷֱ�0-1.
	 * Scrolls horizontally.
	 *
	 * @param side the side to which to scroll; {@link Side#RIGHT} or {@link Side#LEFT}
	 * @param scrollPosition the position to scroll to, from 0 to 1 where 1 is all the way. Example is: 0.55.
	 */

	@SuppressWarnings("deprecation")
	public void scrollToSide(Side side, float scrollPosition) {
		// ��ȡ��Ļ�߶�
		int screenHeight = activityUtils.getCurrentActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		// ��ȡ��Ļ���
		int screenWidth = activityUtils.getCurrentActivity(false).getWindowManager().getDefaultDisplay()
				.getWidth();
		// ���տ�ȼ����ܾ���
		float x = screenWidth * scrollPosition;
		// �϶�ѡ����Ļ���м�
		float y = screenHeight / 2.0f;
		//�����϶�
		if (side == Side.LEFT)
			drag(0, x, y, y, 40);
		// �����϶�
		else if (side == Side.RIGHT)
			drag(x, 0, y, y, 40);
	}

	/**
	 * �Ը����ؼ���������������϶�����.Ĭ���϶������ֳ�40��
	 * view  ��Ҫ�϶������Ŀؼ�
	 * side  �϶�����
	 * scrollPosition �϶����룬������Ļ��Ȱٷֱȼ��㣬ֵΪ0-1
	 * Scrolls view horizontally.
	 *
	 * @param view the view to scroll
	 * @param side the side to which to scroll; {@link Side#RIGHT} or {@link Side#LEFT}
	 * @param scrollPosition the position to scroll to, from 0 to 1 where 1 is all the way. Example is: 0.55.
	 */

	public void scrollViewToSide(View view, Side side, float scrollPosition) {
		// ��ʱ�������洢�ؼ����ֻ���Ļ�е��������
		int[] corners = new int[2];
		// ��ȡ�������
		view.getLocationOnScreen(corners);
		// ��ȡ�߶��������
		int viewHeight = view.getHeight();
		// ��ȡ����������
		int viewWidth = view.getWidth();
		// �����϶���ʼx����
		float x = corners[0] + viewWidth * scrollPosition;
		// �����϶���ʼy����
		float y = corners[1] + viewHeight / 2.0f;
		// �����϶�
		if (side == Side.LEFT)
			drag(corners[0], x, y, y, 40);
		// �����϶�
		else if (side == Side.RIGHT)
			drag(x, corners[0], y, y, 40);
	}

}