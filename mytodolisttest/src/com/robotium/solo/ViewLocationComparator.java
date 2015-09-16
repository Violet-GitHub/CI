package com.robotium.solo;

import android.view.View;
import java.util.Comparator;

/**
 * View�ؼ��ȽϹ���,Ĭ��ʹ��y����ֵ���Ƚϣ���view��������Ļ�е����浽��������
 * Orders {@link View}s by their location on-screen.
 * 
 */

class ViewLocationComparator implements Comparator<View> {
	// ��ŵ�һ��view��λ������
	private final int a[] = new int[2];
	// ��ŵڶ���view��λ������
	private final int b[] = new int[2];
	private final int axis1, axis2;
	// Ĭ�Ϲ��캯�����߶���������
	public ViewLocationComparator() {
		this(true);
	}

	/**
	 * �����������
	 * yAxisFirst true���ո߶����ȱȽϣ�false ���� x���꣬������������
	 * @param yAxisFirst Whether the y-axis should be compared before the x-axis.
	 */

	public ViewLocationComparator(boolean yAxisFirst) {
		this.axis1 = yAxisFirst ? 1 : 0;
		this.axis2 = yAxisFirst ? 0 : 1;
	}
	// ���չ��캯���趨�Ĺ��򣬱Ƚ�2��view��λ��
	public int compare(View lhs, View rhs) {
		// ��ȡ��һ��view��λ��������Ϣ
		lhs.getLocationOnScreen(a);
		// ��ȡ�ڶ���view��λ��������Ϣ
		rhs.getLocationOnScreen(b);
		// �������겻��ȣ��Ƚ����������С
		if (a[axis1] != b[axis1]) {
			return a[axis1] < b[axis1] ? -1 : 1;
		}
		// �Ƚϵڶ��������С
		if (a[axis2] < b[axis2]) {
			return -1;
		}
		// 2��������ȣ��򷵻�0����1���ȵڶ������򷵻�1
		return a[axis2] == b[axis2] ? 0 : 1;
	}
}