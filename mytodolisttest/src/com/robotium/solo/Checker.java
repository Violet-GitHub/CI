package com.robotium.solo;

import java.util.ArrayList;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Check��View��鹤���࣬�ṩ������Ϣ�����
 * Contains various check methods. Examples are: isButtonChecked(),
 * isSpinnerTextSelected.
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Checker {
	// view��ȡ������
	private final ViewFetcher viewFetcher;
	// wait�ȴ�������,���ڻ�ȡ���� View���ж�text�������Ƿ����
	private final Waiter waiter;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param viewFetcher the {@code ViewFetcher} instance
     * @param waiter the {@code Waiter} instance
	 */
	
	public Checker(ViewFetcher viewFetcher, Waiter waiter){
		this.viewFetcher = viewFetcher;
		this.waiter = waiter;
	}

	
	/**
	 * ��ȡָ�����͵ĵ�index��CompoundButton���󣬼���Ƿ�ѡ����.CompoundButton��ѡ�к�δѡ��2��״̬
	 * Checks if a {@link CompoundButton} with a given index is checked.
	 *
	 * @param expectedClass the expected class, e.g. {@code CheckBox.class} or {@code RadioButton.class}
	 * @param index of the {@code CompoundButton} to check. {@code 0} if only one is available
	 * @return {@code true} if {@code CompoundButton} is checked and {@code false} if it is not checked
	 */
	
	public <T extends CompoundButton> boolean isButtonChecked(Class<T> expectedClass, int index)
	{	
		// ����waiter�ķ�����ȡָ��������view
		return (waiter.waitForAndGetView(index, expectedClass).isChecked());
	}
	
	/**
	 * ��ȡ֪�����͵�ָ��text��CompoundButton���Ϳؼ���ȡ��һ��������Ƿ�ѡ��,���û���ҵ�ָ�������Ŀؼ�����ôҲ����false
	 * Checks if a {@link CompoundButton} with a given text is checked.
	 *
	 * @param expectedClass the expected class, e.g. {@code CheckBox.class} or {@code RadioButton.class}
	 * @param text the text that is expected to be checked
	 * @return {@code true} if {@code CompoundButton} is checked and {@code false} if it is not checked
	 */

	public <T extends CompoundButton> boolean isButtonChecked(Class<T> expectedClass, String text)
	{
		// ���ո�����������View,���϶�ˢ�²���
		T button = waiter.waitForText(expectedClass, text, 0, Timeout.getSmallTimeout(), true);
		// ����Ƿ��ҵ��ұ�ѡ��
		if(button != null && button.isChecked()){
			return true;
		}
		return false;
	}

	/**
	 * ����ָ��text�ĵ�һ��CheckedTextView���Ϳؼ�������Ƿ�ѡ�У�ѡ�з���true,δѡ�з���false.���δ�ҵ�Ҳ����false.
	 * Checks if a {@link CheckedTextView} with a given text is checked.
	 *
	 * @param checkedTextView the {@code CheckedTextView} object
	 * @param text the text that is expected to be checked
	 * @return {@code true} if {@code CheckedTextView} is checked and {@code false} if it is not checked
	 */

	public boolean isCheckedTextChecked(String text)
	{
		// ����ָ����������View
		CheckedTextView checkedTextView = waiter.waitForText(CheckedTextView.class, text, 0, Timeout.getSmallTimeout(), true);
		// ����Ƿ��ҵ��ұ�ѡ��
		if(checkedTextView != null && checkedTextView.isChecked()) {
			return true;
		}
		return false;
	}

	
	/**
	 * ����ָ��text������Spinner���Ϳؼ�������Ƿ��б�ѡ�У���ѡ�з���true,δѡ�з���false.���δ�ҵ�Ҳ����false.
	 * Checks if a given text is selected in any {@link Spinner} located on the current screen.
	 * 
	 * @param text the text that is expected to be selected
	 * @return {@code true} if the given text is selected in any {@code Spinner} and false if it is not
	 */
	
	public boolean isSpinnerTextSelected(String text)
	{
		// ˢ��һ��ҳ��
		waiter.waitForAndGetView(0, Spinner.class);
		// ����ָ��text�� Spinner
		ArrayList<Spinner> spinnerList = viewFetcher.getCurrentViews(Spinner.class);
		// ������������Ƿ��з���������
		for(int i = 0; i < spinnerList.size(); i++){
			if(isSpinnerTextSelected(i, text))
					return true;
		}
		return false;
	}
	
	/**
	 * ����ָ��text�ĵ�spinnerIndex��Spinner���Ϳؼ�������Ƿ��б�ѡ�У���ѡ�з���true,δѡ�з���false.���δ�ҵ�Ҳ����false.
	 * ��÷�����δ��null�жϣ�����е���nullpoint�쳣�Ŀ���
	 * Checks if a given text is selected in a given {@link Spinner} 
	 * @param spinnerIndex the index of the spinner to check. 0 if only one spinner is available
	 * @param text the text that is expected to be selected
	 * @return true if the given text is selected in the given {@code Spinner} and false if it is not
	 */
	
	public boolean isSpinnerTextSelected(int spinnerIndex, String text)
	{
		// ��ȡָ����Spinner
		Spinner spinner = waiter.waitForAndGetView(spinnerIndex, Spinner.class);
		// δ����ȡ���Ƿ���null,����Spinner���ͣ��ɵ���nullpoint�쳣
		// ��ȡSpinner��ǰ��ѡ�е�text
		TextView textView = (TextView) spinner.getChildAt(0);
		// ����Ƿ�Ϊָ����
		if(textView.getText().equals(text))
			return true;
		else
			return false;
	}
}