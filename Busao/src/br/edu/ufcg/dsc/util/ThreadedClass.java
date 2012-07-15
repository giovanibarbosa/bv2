package br.edu.ufcg.dsc.util;

import android.os.Handler;
import android.util.Log;

public class ThreadedClass implements Runnable {
	Thread m_thread = null;
	private volatile boolean m_run = true;
	Handler m_refParentHandler = null;

	public ThreadedClass(Handler refParentHandler) {
		m_refParentHandler = refParentHandler;
	}

	public void Start() {
		m_thread = new Thread(null, this, "testing");
		m_thread.start();
	}

	public void run() {
		while (m_run) {
			// Now try to send a message to the parent Activity, prompting
			// it to launch an alert dialog in its own thread.
			if (m_refParentHandler != null) {
				Log.e("Thread",
						"Will attempt to send message to parent Activity now...");
				m_refParentHandler.sendEmptyMessage(0);
			}
			Stop();
		}
	}

	public void Stop() {
		m_run = false;
		if (m_thread != null) {
			try {
				m_thread.join();
			} catch (InterruptedException e) {
			}
		}
		m_thread = null;
	}
}
