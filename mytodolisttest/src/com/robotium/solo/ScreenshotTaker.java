package com.robotium.solo;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.robotium.solo.Solo.Config;
import com.robotium.solo.Solo.Config.ScreenshotFileType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

/**
 * ��������������
 * Contains screenshot methods like: takeScreenshot(final View, final String name), startScreenshotSequence(final String name, final int quality, final int frameDelay, final int maxFrames), 
 * stopScreenshotSequence().
 * 
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class ScreenshotTaker {
	// �����ļ�������Robotium�ĸ�������
	private final Config config;
	// activity������
	private final ActivityUtils activityUtils;
	// ��־��ǣ���ʶ�ò�����Robotium��
	private final String LOG_TAG = "Robotium";
	// ������ͼ�߳�
	private ScreenshotSequenceThread screenshotSequenceThread = null;
	// ͼƬ�洢�����߳�
	private HandlerThread screenShotSaverThread = null;
	// ͼƬ���湤����
	private ScreenShotSaver screenShotSaver = null;
	// view���ҹ�����
	private final ViewFetcher viewFetcher;
	// ��ʱ�ȴ�������
	private final Sleeper sleeper;


	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param config the {@code Config} instance
	 * @param activityUtils the {@code ActivityUtils} instance
	 * @param viewFetcher the {@code ViewFetcher} instance
	 * @param sleeper the {@code Sleeper} instance
	 * 
	 */
	ScreenshotTaker(Config config, ActivityUtils activityUtils, ViewFetcher viewFetcher, Sleeper sleeper) {
		this.config = config;
		this.activityUtils = activityUtils;
		this.viewFetcher = viewFetcher;
		this.sleeper = sleeper;
	}

	/**
	 * ��ͼ����,Ҫȥ��дSDcardȨ�ޣ���ͼ�ļ���洢��sdcard,��Ҫ�޸Ĵ���·������ô����ͨ���޸�Config��screenshotSavePath���Ա༭
	 * Ĭ��·��Ϊ/sdcard/Robotium-Screenshots/
	 * name     ��ͼ�����ļ���
	 * quality  ��ͼ����0-100
	 * Takes a screenshot and saves it in the {@link Config} objects save path.  
	 * Requires write permission (android.permission.WRITE_EXTERNAL_STORAGE) in AndroidManifest.xml of the application under test.
	 * 
	 * @param view the view to take screenshot of   ��������Ѿ�û���ˣ���û��Ҫ�����ע����
	 * @param name the name to give the screenshot image
	 * @param quality the compression rate. From 0 (compress for lowest size) to 100 (compress for maximum quality).
	 */
	public void takeScreenshot(final String name, final int quality) {
		// ��ȡDecorView
		View decorView = getScreenshotView();
		// �޷���ȡDecorView,ֱ���˳�
		if(decorView == null) 
			return;
		// ��ʼ��ͼƬ�洢��Ҫ��һЩ����
		initScreenShotSaver();
		// �����ͼ�߳�
		ScreenshotRunnable runnable = new ScreenshotRunnable(decorView, name, quality);
		// ִ�н�ͼ�߳�
		activityUtils.getCurrentActivity(false).runOnUiThread(runnable);
	}

	/**
	 * ���ӽ�ͼ
	 * name    		��ͼ�����ͼƬ��.��׷��_0---maxFrames-1
	 * quality 		��ͼ����0-100
	 * frameDelay   ÿ�ν�ͼʱ����
	 * maxFrames    ��ͼ����
	 * Takes a screenshot sequence and saves the images with the name prefix in the {@link Config} objects save path.  
	 *
	 * The name prefix is appended with "_" + sequence_number for each image in the sequence,
	 * where numbering starts at 0.  
	 *
	 * Requires write permission (android.permission.WRITE_EXTERNAL_STORAGE) in the 
	 * AndroidManifest.xml of the application under test.
	 *
	 * Taking a screenshot will take on the order of 40-100 milliseconds of time on the 
	 * main UI thread.  Therefore it is possible to mess up the timing of tests if
	 * the frameDelay value is set too small.
	 *
	 * At present multiple simultaneous screenshot sequences are not supported.  
	 * This method will throw an exception if stopScreenshotSequence() has not been
	 * called to finish any prior sequences.
	 *
	 * @param name the name prefix to give the screenshot
	 * @param quality the compression rate. From 0 (compress for lowest size) to 100 (compress for maximum quality)
	 * @param frameDelay the time in milliseconds to wait between each frame
	 * @param maxFrames the maximum number of frames that will comprise this sequence
	 *
	 */
	public void startScreenshotSequence(final String name, final int quality, final int frameDelay, final int maxFrames) {
		// ��ʼ����ͼ�������
		initScreenShotSaver();
		// ��ֹͬʱִ�ж��������ͼ������������ͼ��ִ��ʱ�׳��쳣
		if(screenshotSequenceThread != null) {
			throw new RuntimeException("only one screenshot sequence is supported at a time");
		}
		// ����һ��������ͼ�߳�
		screenshotSequenceThread = new ScreenshotSequenceThread(name, quality, frameDelay, maxFrames);
		// ��ʼ������ͼ
		screenshotSequenceThread.start();
	}

	/**
	 * ֹͣ������ͼ
	 * Causes a screenshot sequence to end.
	 * 
	 * If this method is not called to end a sequence and a prior sequence is still in 
	 * progress, startScreenshotSequence() will throw an exception.
	 */
	public void stopScreenshotSequence() {
		// ��������ͼ�̷߳ǿ�ʱ��ֹͣ������ͼ
		if(screenshotSequenceThread != null) {
			// ֹͣ������ͼ
			screenshotSequenceThread.interrupt();
			// �ͷ��̶߳���
			screenshotSequenceThread = null;
		}
	}

	/**
	 * ��ȡ��ǰ�Ľ�����ʾview,����һЩRobotium���ƻ��Ĳ���
	 * Gets the proper view to use for a screenshot.  
	 */
	private View getScreenshotView() {
		// ��ȡ��ǰ����ʾ����view
		View decorView = viewFetcher.getRecentDecorView(viewFetcher.getWindowDecorViews());
		// ���ó�ʱʱ��
		final long endTime = SystemClock.uptimeMillis() + Timeout.getSmallTimeout();
		// ����޷���ȡdecorView,���������
		while (decorView == null) {	
			// ����Ƿ��Ѿ���ʱ
			final boolean timedOut = SystemClock.uptimeMillis() > endTime;
			// �Ѿ���ʱֱ���˳�
			if (timedOut){
				return null;
			}
			// �ȴ�300ms
			sleeper.sleepMini();
			// ���Ի�ȡ��ǰ��decorView
			decorView = viewFetcher.getRecentDecorView(viewFetcher.getWindowDecorViews());
		}
		// ��Rotium��Render�滻ԭ����Render
		wrapAllGLViews(decorView);

		return decorView;
	}

	/**
	 * �޸� View��Render,��Robotium�Զ�����滻
	 * Extract and wrap the all OpenGL ES Renderer.
	 */
	private void wrapAllGLViews(View decorView) {
		// ��ȡ��ǰdecorView�е�GLSurfaceView���͵�view
		ArrayList<GLSurfaceView> currentViews = viewFetcher.getCurrentViews(GLSurfaceView.class, decorView);
		// ��ס��ǰ�̣߳����Ⲣ����������
		final CountDownLatch latch = new CountDownLatch(currentViews.size());
		// ��������view�����滻render
		for (GLSurfaceView glView : currentViews) {
			// �����ȡ����
			Object renderContainer = new Reflect(glView).field("mGLThread")
					.type(GLSurfaceView.class).out(Object.class);
			// ��ȡԭʼ��renderer
			Renderer renderer = new Reflect(renderContainer).field("mRenderer").out(Renderer.class);
			// �����ȡʧ�ܣ�����ֱ�ӻ�ȡglView������
			if (renderer == null) {
				renderer = new Reflect(glView).field("mRenderer").out(Renderer.class);
				renderContainer = glView;
			}  
			// ����޷���ȡ����������ǰ��������һ��
			if (renderer == null) {
				//��������һ
				latch.countDown();
				// ��ת���¸�ѭ��
				continue;
			}
			// ����render���ͽ��в���,����Ѿ���Robotium�޸Ĺ���render,��ô������������Լ���
			if (renderer instanceof GLRenderWrapper) {
				// ����ת��Robotium��
				GLRenderWrapper wrapper = (GLRenderWrapper) renderer;
				// ���ý�ͼģʽ
				wrapper.setTakeScreenshot();
				// ���ò������Ƽ�����
				wrapper.setLatch(latch);
				// ���������robotium�޸Ĺ��ģ���ô�����¹���һ���������滻ԭ������
			} else {
				// ����һ��robotium�޸Ĺ���Render
				GLRenderWrapper wrapper = new GLRenderWrapper(glView, renderer, latch);
				// ͨ�������޸�����Ϊ���Ƶ�render
				new Reflect(renderContainer).field("mRenderer").in(wrapper);
			}
		}
		// �ȴ��������
		try {
			latch.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * ��ȡWebView��ͼ������
	 * Returns a bitmap of a given WebView.
	 *  
	 * @param webView the webView to save a bitmap from
	 * @return a bitmap of the given web view
	 * 
	 */

	private Bitmap getBitmapOfWebView(final WebView webView){
		// ��ȡWebViewͼ������
		Picture picture = webView.capturePicture();
		// ����Bitmap����
		Bitmap b = Bitmap.createBitmap( picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
		// ����Canvas
		Canvas c = new Canvas(b);
		// ��ͼƬ���Ƶ�canvas.���ǰ����ݸ㵽Bitmap�У���b��
		picture.draw(c);
		return b;
	}

	/**
	 * ��ȡView��BitMap��ʽ�ļ�����
	 * Returns a bitmap of a given View.
	 * 
	 * @param view the view to save a bitmap from
	 * @return a bitmap of the given view
	 * 
	 */

	private Bitmap getBitmapOfView(final View view){
		// ��ʼ�����壬���ԭ������
		view.destroyDrawingCache();
		view.buildDrawingCache(false);
		// ��ȡBitmap����
		Bitmap orig = view.getDrawingCache();
		Bitmap.Config config = null;
		// �����ȡ����Ϊnull,ֱ�ӷ���null
		if(orig == null) {
			return null;
		}
		// ��ȡ������Ϣ
		config = orig.getConfig();
		// ���ͼƬ�����޷���ȡ����Ĭ��ʹ��ARGB_8888
		if(config == null) {
			config = Bitmap.Config.ARGB_8888;
		}
		// ����BitMap����
		Bitmap b = orig.copy(config, false);
		// ��ջ�ͼ����
		view.destroyDrawingCache();
		return b; 
	}

	/**
	 * ���մ����ļ��������������ļ���
	 * Returns a proper filename depending on if name is given or not.
	 * 
	 * @param name the given name
	 * @return a proper filename depedning on if a name is given or not
	 * 
	 */

	private String getFileName(final String name){
		// �������ڸ�ʽ
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss");
		String fileName = null;
		// ���δ�������֣���ôĬ�Ϲ���һ��
		if(name == null){
			// �������ù���ͼƬ����jpg png
			if(config.screenshotFileType == ScreenshotFileType.JPEG){
				fileName = sdf.format( new Date()).toString()+ ".jpg";
			}
			else{
				fileName = sdf.format( new Date()).toString()+ ".png";	
			}
		}
		// ���Ѵ����ļ����֣���ôƴ���ļ����ͺ�׺
		else {
			// �������ù���ͼƬ����jpg png
			if(config.screenshotFileType == ScreenshotFileType.JPEG){
				fileName = name + ".jpg";
			}
			else {
				fileName = name + ".png";	
			}
		}
		return fileName;
	}

	/**
	 * ��ʼ��ͼƬ�洢�����Դ
	 * This method initializes the aysnc screenshot saving logic
	 */
	private void initScreenShotSaver() {
		// �����ǰ�洢�߳�δ��ʼ��������г�ʼ��
		if(screenShotSaverThread == null || screenShotSaver == null) {
			// ��ʼ��һ�������߳�
			screenShotSaverThread = new HandlerThread("ScreenShotSaver");
			// ��ʼ�����߳�
			screenShotSaverThread.start();
			// ��ʼ��һ���洢��
			screenShotSaver = new ScreenShotSaver(screenShotSaverThread);
		}
	}

	/** 
	 * ������ͼ�߳�
	 * _name       ��ͼ������,��ƴ����˳��0--_maxFrames-1
	 * _quality    ��ͼ����0-100
	 * _frameDelay ��ͼ���ʱ�䣬��λ ms
	 * _maxFrames  ��ͼ����
	 * This is the thread which causes a screenshot sequence to happen
	 * in parallel with testing.
	 */
	private class ScreenshotSequenceThread extends Thread {
		// ��ʼ������Ϊ0
		private int seqno = 0;
		// ������ļ���
		private String name;
		// ͼƬ����0-100
		private int quality;
		// ��ͼ��ʱ����λ ms
		private int frameDelay;
		// ��Ҫ��ͼ������
		private int maxFrames;

		private boolean keepRunning = true;
		// ���캯��
		public ScreenshotSequenceThread(String _name, int _quality, int _frameDelay, int _maxFrames) {
			name = _name;
			quality = _quality; 
			frameDelay = _frameDelay;
			maxFrames = _maxFrames;
		}

		public void run() {
			// ��ͼ����δ�ﵽָ��ֵ��������ͼ
			while(seqno < maxFrames) {
				// �߳̽�����ҵ���Ѿ�������˳�ѭ��
				if(!keepRunning || Thread.interrupted()) break;
				// ��ͼ
				doScreenshot();
				// ������+1
				seqno++;
				try {
					// �ȴ�ָ����ʱ��
					Thread.sleep(frameDelay);
				} catch (InterruptedException e) {
				}
			}
			// �ͷ��̶߳���
			screenshotSequenceThread = null;
		}
		// ��ͼ
		public void doScreenshot() {
			// ��ȡ��ǰ����ĻDecorView
			View v = getScreenshotView();
			// ����޷���ȡdecorView ��ֹ��ǰ�߳�
			if(v == null) keepRunning = false;
			// ƴ���ļ���
			String final_name = name+"_"+seqno;
			// ��ʼ����ͼ�߳�
			ScreenshotRunnable r = new ScreenshotRunnable(v, final_name, quality);
			// ��¼��־
			Log.d(LOG_TAG, "taking screenshot "+final_name);
			// ������ͼ�߳�
			activityUtils.getCurrentActivity(false).runOnUiThread(r);
		}
		// ͣ����ǰ�߳�
		public void interrupt() {
			// ���Ϊ����Ϊfalse,ֹͣ��ͼ
			keepRunning = false;
			super.interrupt();
		}
	}

	/**
	 * ץȡ��ǰ��Ļ�����͸���ӦͼƬ�������������ͼƬ����ͱ���
	 * Here we have a Runnable which is responsible for taking the actual screenshot,
	 * and then posting the bitmap to a Handler which will save it.
	 *
	 * This Runnable is run on the UI thread.
	 */
	private class ScreenshotRunnable implements Runnable {
		// decorView
		private View view;
		// �ļ���
		private String name;
		// ͼƬ����
		private int quality;
		// ���캯��
		public ScreenshotRunnable(final View _view, final String _name, final int _quality) {
			view = _view;
			name = _name;
			quality = _quality;
		}

		public void run() {
			// ���decorView���Ի�ȡ�������ͼ
			if(view !=null){
				Bitmap  b;
				// ���� View���ͽ���ͼƬ���ݻ�ȡ����
				if(view instanceof WebView){
					b = getBitmapOfWebView((WebView) view);
				}
				else{
					b = getBitmapOfView(view);
				}
				// ������Ի�ȡ��ͼƬ���ݣ��򱣴�ͼƬ
				if(b != null)
					screenShotSaver.saveBitmap(b, name, quality);
				// �޷���ȡͼƬ���ݣ���ӡ�����־
				else 
					Log.d(LOG_TAG, "NULL BITMAP!!");
			}
		}
	}

	/**
	 * ����ͼƬ��ͨ���첽�߳����
	 * This class is a Handler which deals with saving the screenshots on a separate thread.
	 *
	 * The screenshot logic by necessity has to run on the ui thread.  However, in practice
	 * it seems that saving a screenshot (with quality 100) takes approx twice as long
	 * as taking it in the first place. 
	 *
	 * Saving the screenshots in a separate thread like this will thus make the screenshot
	 * process approx 3x faster as far as the main thread is concerned.
	 *
	 */
	private class ScreenShotSaver extends Handler {
		// ���캯��
		public ScreenShotSaver(HandlerThread thread) {
			super(thread.getLooper());
		}

		/**
		 * ����ͼƬ,ͨ����Ϣ����
		 * bitmap  Ҫ�����ͼƬ
		 * name    ͼƬ��
		 * quality ͼƬ����0-100
		 * This method posts a Bitmap with meta-data to the Handler queue.
		 *
		 * @param bitmap the bitmap to save
		 * @param name the name of the file
		 * @param quality the compression rate. From 0 (compress for lowest size) to 100 (compress for maximum quality).
		 */
		public void saveBitmap(Bitmap bitmap, String name, int quality) {
			// ��ʼ������һ����Ϣ
			Message message = this.obtainMessage();
			// ��ʼ����Ϣ����
			message.arg1 = quality;
			message.obj = bitmap;
			message.getData().putString("name", name);
			// ������Ϣ���ȴ�����������
			this.sendMessage(message);
		}

		/**
		 * �����յ�����Ϣ
		 * Here we process the Handler queue and save the bitmaps.
		 *
		 * @param message A Message containing the bitmap to save, and some metadata.
		 */
		public void handleMessage(Message message) {
			// ��ȡͼƬ��
			String name = message.getData().getString("name");
			// ��ȡͼƬ����
			int quality = message.arg1;
			// ��ȡͼƬ����
			Bitmap b = (Bitmap)message.obj;
			// ����ͼƬ����
			if(b != null) {
				// ����ͼƬ��ָ���ļ�
				saveFile(name, b, quality);
				// �ͷ�ͼƬ����
				b.recycle();
			}
			// ���ͼƬ�����ݣ����ӡ��־��Ϣ
			else {
				Log.d(LOG_TAG, "NULL BITMAP!!");
			}
		}

		/**
		 * �������ļ�
		 * Saves a file.
		 * 
		 * @param name the name of the file
		 * @param b the bitmap to save
		 * @param quality the compression rate. From 0 (compress for lowest size) to 100 (compress for maximum quality).
		 * 
		 */
		private void saveFile(String name, Bitmap b, int quality){
			// д�ļ�����
			FileOutputStream fos = null;
			// ���������ļ���
			String fileName = getFileName(name);
			// ��ȡϵͳ���õ�Ŀ¼
			File directory = new File(config.screenshotSavePath);
			// ����Ŀ¼
			directory.mkdir();
			// ��ȡ�ļ�����
			File fileToSave = new File(directory,fileName);
			try {
				// ��ȡ�ļ���д����
				fos = new FileOutputStream(fileToSave);
				if(config.screenshotFileType == ScreenshotFileType.JPEG){
					// ͼƬ���ݰ���ָ����ʽѹ������д��ָ���ļ���������쳣����ӡ�쳣��־
					if (b.compress(Bitmap.CompressFormat.JPEG, quality, fos) == false){
						Log.d(LOG_TAG, "Compress/Write failed");
					}
				}
				else{
					// ͼƬ���ݰ���ָ����ʽѹ������д��ָ���ļ���������쳣����ӡ�쳣��־
					if (b.compress(Bitmap.CompressFormat.PNG, quality, fos) == false){
						Log.d(LOG_TAG, "Compress/Write failed");
					}
				}
				// �ر�д�ļ���
				fos.flush();
				fos.close();
			} catch (Exception e) {
				// �ճ���¼logcat��־������ӡ�쳣��ջ
				Log.d(LOG_TAG, "Can't save the screenshot! Requires write permission (android.permission.WRITE_EXTERNAL_STORAGE) in AndroidManifest.xml of the application under test.");
				e.printStackTrace();
			}
		}
	}
}