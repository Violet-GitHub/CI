package com.robotium.solo;

import android.app.Instrumentation;
import android.graphics.PointF;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
// ��Ļ���������
class Tapper
{
	// Instrument �����¼�����
    private final Instrumentation _instrument;
    // 1s
    public static final int GESTURE_DURATION_MS = 1000;
    // 10ms
    public static final int EVENT_TIME_INTERVAL_MS = 10;
    // ���캯��
    public Tapper(Instrumentation inst)
    {
        this._instrument = inst;
    }
    // ������Ļ����¼�
    // numTaps  �������,���븺ֵ����ѭ����...
    // points   �������㣬1-2�� 
    //          1һ�ε�һ���㣬2һ�ε��2����
	public void generateTapGesture(int numTaps, PointF... points)
    {
		// MotionEvent�¼�
        MotionEvent event;
        // ���쿪ʼ����ʱ��
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        // ��ȡ��������
        // pointer 1
        float x1 = points[0].x;
        float y1 = points[0].y;
        // �����������2��,��ô����2������,����Ĭ�ϵڶ��������Ϊ0
        float x2 = 0;
        float y2 = 0;
        if (points.length == 2)
        {
            // pointer 2
            x2 = points[1].x;
            y2 = points[1].y;
        }
        // ��������㼯��
        PointerCoords[] pointerCoords = new PointerCoords[points.length];
        PointerCoords pc1 = new PointerCoords();
        pc1.x = x1;
        pc1.y = y1;
        pc1.pressure = 1;
        pc1.size = 1;
        pointerCoords[0] = pc1;
        PointerCoords pc2 = new PointerCoords();
        if (points.length == 2)
        {
            pc2.x = x2;
            pc2.y = y2;
            pc2.pressure = 1;
            pc2.size = 1;
            pointerCoords[1] = pc2;
        }

        PointerProperties[] pointerProperties = new PointerProperties[points.length];
        PointerProperties pp1 = new PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        pointerProperties[0] = pp1;
        PointerProperties pp2 = new PointerProperties();
        if (points.length == 2)
        {
            pp2.id = 1;
            pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;
            pointerProperties[1] = pp2;
        }
        // ���͹�����¼�
        int i = 0;
        // ����ָ�������ĵ��
        while (i != numTaps)
        {	// ���͵�һ�������¼�
            event = MotionEvent.obtain(downTime, eventTime,
                    MotionEvent.ACTION_DOWN, points.length, pointerProperties,
                    pointerCoords, 0, 0, 1, 1, 0, 0,
                    InputDevice.SOURCE_TOUCHSCREEN, 0);
            _instrument.sendPointerSync(event);
            // ����������2��.��ô���͵ڶ����¼�
            if (points.length == 2)
            {
                event = MotionEvent
                        .obtain(downTime,
                                eventTime,
                                MotionEvent.ACTION_POINTER_DOWN
                                        + (pp2.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT),
                                points.length, pointerProperties,
                                pointerCoords, 0, 0, 1, 1, 0, 0,
                                InputDevice.SOURCE_TOUCHSCREEN, 0);
                _instrument.sendPointerSync(event);

                eventTime += EVENT_TIME_INTERVAL_MS;
                event = MotionEvent
                        .obtain(downTime,
                                eventTime,
                                MotionEvent.ACTION_POINTER_UP
                                        + (pp2.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT),
                                points.length, pointerProperties,
                                pointerCoords, 0, 0, 1, 1, 0, 0,
                                InputDevice.SOURCE_TOUCHSCREEN, 0);
                _instrument.sendPointerSync(event);
            }
            // �����ɿ��¼�
            eventTime += EVENT_TIME_INTERVAL_MS;
            event = MotionEvent.obtain(downTime, eventTime,
                    MotionEvent.ACTION_UP, points.length, pointerProperties,
                    pointerCoords, 0, 0, 1, 1, 0, 0,
                    InputDevice.SOURCE_TOUCHSCREEN, 0);
            _instrument.sendPointerSync(event);
            // ����+1
            i++;
        }
    }
}