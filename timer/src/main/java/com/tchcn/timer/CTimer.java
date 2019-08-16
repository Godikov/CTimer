package com.tchcn.timer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 倒计时工具类。
 * Created by humu on 2019/5/15.
 */

public class CTimer {

    private static final Map<Integer, TimerHandler> handlerMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> timerMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> intervalMap = new ConcurrentHashMap<>();
    private static final Map<Integer, CTimerListener> listenerMap = new ConcurrentHashMap<>();
    private static final List<Integer> pauseList = Collections.synchronizedList(new ArrayList<Integer>()); //正在暂停的倒计时

    private CTimer() {
    }

    /**
     * 开始倒计时，默认倒计时间隔为1s，即每一秒回调一次onCount
     *
     * @param tag            倒计时标记
     * @param totalTime      倒计时总时长，单位s
     * @param cTimerListener 倒计时监听
     */
    public static void start(final int tag, int totalTime, final CTimerListener cTimerListener) {
        if (!timerMap.containsKey(tag)) {
            final TimerHandler handler = TimerHandler.getInstance();
            synchronized (handlerMap) {
                handlerMap.put(tag, handler);
            }
            final Message message = handler.obtainMessage(tag);
            if (totalTime > 0) {
                if (cTimerListener != null) {
                    cTimerListener.onCountDownStart(tag);
                }
                handler.sendMessage(message);
                synchronized (timerMap) {
                    timerMap.put(tag, totalTime);
                }
                synchronized (listenerMap) {
                    listenerMap.put(tag, cTimerListener);
                }
            }
        }
    }

    /**
     * 开始倒计时
     *
     * @param tag            倒计时标记
     * @param totalTime      倒计时总时长，单位s
     * @param intervalTime   倒计时间隔时间，即几秒回调一次onCount，单位s
     * @param cTimerListener 倒计时监听
     */
    public static void start(final int tag, int totalTime, int intervalTime, final CTimerListener cTimerListener) {
        if (!timerMap.containsKey(tag)) {
            final TimerHandler handler = TimerHandler.getInstance();
            synchronized (handlerMap) {
                handlerMap.put(tag, handler);
            }
            final Message message = handler.obtainMessage(tag);
            if (totalTime > 0) {
                if (cTimerListener != null) {
                    cTimerListener.onCountDownStart(tag);
                }
                handler.sendMessage(message);
                synchronized (timerMap) {
                    timerMap.put(tag, totalTime);
                }
                synchronized (intervalMap) {
                    intervalMap.put(tag, intervalTime);
                }
                synchronized (listenerMap) {
                    listenerMap.put(tag, cTimerListener);
                }
            }
        }
    }

    /**
     * 暂停倒计时
     *
     * @param tag 倒计时标记
     */
    public synchronized static void pause(int tag) {
        if (!timerMap.containsKey(tag)) {
            return;
        }
        if (pauseList.contains(tag)) {
            return;
        }
        synchronized (pauseList) {
            pauseList.add(tag);
        }
    }

    /**
     * 继续倒计时
     *
     * @param tag 被暂停的倒计时标记
     */
    public static void resume(final int tag) {
        if (!timerMap.containsKey(tag)) {
            return;
        }
        if (!pauseList.contains(tag)) {
            return;
        }
        pauseList.remove((Integer) tag);
        if (listenerMap.containsKey(tag)) {
            final CTimerListener cTimerListener = listenerMap.get(tag);
            if (cTimerListener != null) {
                cTimerListener.onCountRestart(tag);
            }
        }
        final TimerHandler handler = TimerHandler.getInstance();
        final Message message = handler.obtainMessage(tag);
        handler.sendMessage(message);
    }

    /**
     * 停止倒计时
     * 会回调CTimerListener的onStop方法,不会回调CTimerListener的onCountOver方法
     *
     * @param tag 倒计时标记
     */
    public static void stop(final int tag) {
        if (handlerMap.containsKey(tag)) {
            if (listenerMap.containsKey(tag)) {
                final CTimerListener cTimerListener = listenerMap.get(tag);
                if (cTimerListener != null) {
                    cTimerListener.onStop(tag);
                }
            }
            final TimerHandler handler = handlerMap.get(tag);
            if (handler != null && handler.hasMessages(tag)) {
                handler.removeMessages(tag);
            }
            removeTag(tag);
        }
    }

    /**
     * 停止所有倒计时
     */
    public synchronized static void stopAll() {
        if (handlerMap.size() > 0) {
            for (int tag : handlerMap.keySet()) {
                stop(tag);
            }
        }
    }

    /**
     * 倒计时处理
     */
    private static class TimerHandler extends Handler {

        private volatile static TimerHandler timerHandler;

        private TimerHandler() {

        }

        public static TimerHandler getInstance() {
            if (timerHandler == null) {
                synchronized (TimerHandler.class) {
                    if (timerHandler == null) {
                        timerHandler = new TimerHandler();
                    }
                }
            }
            return timerHandler;
        }

        @Override
        public synchronized void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("CTimer","====="+Thread.currentThread().getName());
            final int what = msg.what;
            if (listenerMap.containsKey(what)) {
                final CTimerListener cTimerListener = listenerMap.get(what);
                if (cTimerListener != null) {
                    if (timerMap.containsKey(what)) {
                        Integer time = timerMap.get(what);
                        if (time <= 0) {
                            removeTag(what);
                            cTimerListener.onCountOver(what);
                        } else {
                            if (!pauseList.contains(what)) {
                                cTimerListener.onCount(what, time);
                                time--;
                                timerMap.put(what, time);
                                final Message message = obtainMessage(what);
                                int intervalTime = 1;
                                if (intervalMap.containsKey(what)) {
                                    intervalTime = intervalMap.get(what);
                                }
                                sendMessageDelayed(message, intervalTime * 1000);
                            } else {
                                cTimerListener.onCount(what, time);
                                cTimerListener.onCountPause(what, time);
                                time--;
                                timerMap.put(what, time);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 倒计时结束，移除该倒计时
     *
     * @param tag
     */
    private static void removeTag(int tag) {
        if (timerMap.containsKey(tag)) {
            synchronized (timerMap) {
                timerMap.remove(tag);
            }
        }
        if (intervalMap.containsKey(tag)) {
            synchronized (intervalMap) {
                intervalMap.remove(tag);
            }
        }
        if (listenerMap.containsKey(tag)) {
            synchronized (listenerMap) {
                listenerMap.remove(tag);
            }
        }
        if (handlerMap.containsKey(tag)) {
            synchronized (handlerMap) {
                handlerMap.remove(tag);
            }
        }
        if (pauseList.contains(tag)) {
            synchronized (pauseList) {
                pauseList.remove((Integer) tag);
            }
        }
    }

    /**
     * 倒计时监听
     */
    public abstract static class CTimerListener {

        /**
         * 开始倒计时
         *
         * @param tag 倒计时标记
         */
        public void onCountDownStart(int tag) {
        }

        /**
         * 倒计时中
         *
         * @param tag     倒计时标记
         * @param timeNow 当前时间(s)
         */
        public abstract void onCount(int tag, int timeNow);

        /**
         * 倒计时暂停
         *
         * @param tag     倒计时标记
         * @param timeNow 暂停时的时间(s)
         */
        public void onCountPause(int tag, int timeNow) {
        }

        /**
         * 暂停后重新开始倒计时前回调
         *
         * @param tag 倒计时标记
         */
        public void onCountRestart(int tag) {
        }

        /**
         * 倒计时结束
         *
         * @param tag 倒计时标记
         */
        public abstract void onCountOver(int tag);

        /**
         * 手动结束倒计时后回调
         *
         * @param tag 倒计时标记
         */
        public void onStop(int tag) {
        }

    }

}
