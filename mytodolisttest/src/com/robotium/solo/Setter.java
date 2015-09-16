package com.robotium.solo;

import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TimePicker;


/**
 * ������ؼ�����������
 * Contains set methods. Examples are setDatePicker(),
 * setTimePicker().
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Setter{
	// �ر�Ϊ0
	private final int CLOSED = 0;
	// ��Ϊ1
	private final int OPENED = 1;
	// activity����������
	private final ActivityUtils activityUtils;

	/**
	 * ���캯��
	 * Constructs this object.
	 *
	 * @param activityUtils the {@code ActivityUtils} instance
	 */

	public Setter(ActivityUtils activityUtils) {
		this.activityUtils = activityUtils;
	}


	/**
	 * ���ÿؼ�����
	 * datePicker   ��Ҫ�������ڵĿؼ�
	 * year         ��
	 * monthOfYear  ��
	 * dayOfMonth   ��
	 * Sets the date in a given {@link DatePicker}.
	 *
	 * @param datePicker the {@code DatePicker} object.
	 * @param year the year e.g. 2011
	 * @param monthOfYear the month which is starting from zero e.g. 03
	 * @param dayOfMonth the day e.g. 10
	 */

	public void setDatePicker(final DatePicker datePicker, final int year, final int monthOfYear, final int dayOfMonth) {
		// �ǿ��ж�
		if(datePicker != null){
			// �ڵ�ǰactivity��Ui�߳���ִ��,ֱ�ӵ��û��������߳�Ȩ���쳣
			activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable()
			{
				public void run()
				{
					try{
						// ��������������
						datePicker.updateDate(year, monthOfYear, dayOfMonth);
					}catch (Exception ignored){}
				}
			});
		}
	}


	/**
	 * ����ʱ������
	 * timePicker   ��Ҫ�������Ե�TimePicker�ؼ�
	 * hour			ʱ
	 * minute	           ��
	 * Sets the time in a given {@link TimePicker}.
	 *
	 * @param timePicker the {@code TimePicker} object.
	 * @param hour the hour e.g. 15
	 * @param minute the minute e.g. 30
	 */

	public void setTimePicker(final TimePicker timePicker, final int hour, final int minute) {
		// �ǿռ��
		if(timePicker != null){
			// �ڵ�ǰactivity��Ui�߳���ִ��,ֱ�ӵ��û��������߳�Ȩ���쳣
			activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable()
			{
				public void run()
				{
					try{
						// ����ʱ
						timePicker.setCurrentHour(hour);
						// ���÷�
						timePicker.setCurrentMinute(minute);
					}catch (Exception ignored){}
				}
			});
		}
	}
	

	/**
	 * ���ý������ؼ�����
	 * progressBar   ��Ҫ���õĽ�����
	 * progress      ���õ�ֵ
	 * Sets the progress of a given {@link ProgressBar}. Examples are SeekBar and RatingBar.
	 * @param progressBar the {@code ProgressBar}
	 * @param progress the progress that the {@code ProgressBar} should be set to
	 */

	public void setProgressBar(final ProgressBar progressBar,final int progress) {
		// �ǿռ��
		if(progressBar != null){
			// �ڵ�ǰactivity��Ui�߳���ִ��,ֱ�ӵ��û��������߳�Ȩ���쳣
			activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable()
			{
				public void run()
				{
					try{
						// ���ý�������
						progressBar.setProgress(progress);
					}catch (Exception ignored){}
				}
			});
		}
	}


	/**
	 * ����ѡ�񿪹����ԣ��� �� 
	 * slidingDrawer   ��Ҫ���õ�ѡ�񿪹�
	 * status          Solo.CLOSED  Solo.OPENED
	 * Sets the status of a given SlidingDrawer. Examples are Solo.CLOSED and Solo.OPENED.
	 *
	 * @param slidingDrawer the {@link SlidingDrawer}
	 * @param status the status that the {@link SlidingDrawer} should be set to
	 */

	public void setSlidingDrawer(final SlidingDrawer slidingDrawer, final int status){
		// �ǿ��ж�
		if(slidingDrawer != null){
			// �ڵ�ǰactivity��Ui�߳���ִ��,ֱ�ӵ��û��������߳�Ȩ���쳣
			activityUtils.getCurrentActivity(false).runOnUiThread(new Runnable()
			{
				public void run()
				{
					try{
						// ���ո���ֵ���趨״̬
						switch (status) {
						case CLOSED:
							slidingDrawer.close();
							break;
						case OPENED:
							slidingDrawer.open();
							break;
						}
					}catch (Exception ignored){}
				}
			});
		}

	}
}