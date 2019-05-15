# CTimer
倒计时工具类。功能：倒计时、多倒计时、暂停指定倒计时、继续倒计时、结束指定倒计时、结束所有倒计时以及倒计时期间的各种回调，让倒计时变得极易掌控，写起来更简单。

  在日常开发中，诸如发送验证码、等待扫码等界面我们经常会用到倒计时，尤其是当我写到一个自助售票系统时更是每个界面都有倒计时，
  因此为了简化倒计时的写法和更容易获取到倒计时的状态，我对倒计时里面常用的功能进行了总结，完成了这个工具类，取名CTimer。
  
  优点：
    编写倒计时方便快捷。
    同时可执行多个倒计时还易于掌控状态。
    可暂停、继续、停止指定倒计时。

  方法定义：
  
     /**
     * 开始倒计时，默认倒计时间隔为1s，即每一秒回调一次onCount
     * @param tag 倒计时标记
     * @param totalTime 倒计时总时长，单位s
     * @param cTimerListener 倒计时监听
     */
    public static void start(int tag,int totalTime,CTimerListener cTimerListener)
    
  用法示例：  
  
          //定义这个倒计时的标记为COUNT_60，即0
          private final int COUNT_60 = 0;
  
          CTimer.start(COUNT_60, 60, new CTimer.CTimerListener() {
            @Override
            public void onCountDownStart(int tag) {
                tv_60.setText("开始倒计时");
                Log.d(TAG,"====onCountDownStart===="+tag);
            }

            @Override
            public void onCount(int tag, int timeNow) {
                tv_60.setText(timeNow+"");
                Log.d(TAG,"====onCount===="+tag+" "+timeNow);
            }

            @Override
            public void onCountPause(int tag, int timeNow) {
                Log.d(TAG,"====onCountPause===="+tag+" "+timeNow);
            }

            @Override
            public void onCountRestart(int tag) {
                Log.d(TAG,"====restartCount===="+tag);
            }

            @Override
            public void onCountOver(int tag) {
                tv_60.setText("倒计时结束");
                Log.d(TAG,"====onCountOver===="+tag);
            }

            @Override
            public void onStop(int tag) {
                tv_60.setText("倒计时手动结束");
                Log.d(TAG,"====onCountOver===="+tag);
            }
        });
        
  第一个参数tag，即为这个倒计时做个标记，为了以后方便找到它。
  第二个参数totalTime,即倒计时的总时长。
  第三个参数CTimerListener,即倒计时的监听器，提供了以下回调方法：
    
    /**
     * 开始倒计时
     * @param tag 倒计时标记
     */
    public void onCountDownStart(int tag)
    /**
     * 倒计时中
     * @param tag 倒计时标记
     * @param timeNow 当前时间(s)
     */
     public abstract void onCount(int tag,int timeNow);
     /**
      * 倒计时暂停
      * @param tag 倒计时标记
      * @param timeNow 暂停时的时间(s)
      */
     public void onCountPause(int tag,int timeNow){}
     /**
      * 暂停后继续倒计时前回调
      * @param tag 倒计时标记
      */
     public void onCountRestart(int tag)
      /**
       * 倒计时结束
       * @param tag 倒计时标记
       */
     public abstract void onCountOver(int tag);
       /**
       * 手动结束倒计时后回调
       * @param tag 倒计时标记
       */
     public void onStop(int tag)
     
常用的就是onCount和onCountOver两个回调。     
     
同时，考虑到非间隔1s的倒计时，还提供了另一个倒计时方法：

    /**
     * 开始倒计时
     * @param tag 倒计时标记
     * @param totalTime 倒计时总时长，单位s
     * @param intervalTime 倒计时间隔时间，即几秒回调一次onCount，单位s
     * @param cTimerListener 倒计时监听
     */
    public static void start(int tag,int totalTime,int intervalTime,CTimerListener cTimerListener)
    
只是多了一个intervalTime参数，倒计时的间隔时间，即onCount回调的间隔时间，单位为秒。
比如：传入2，即每2秒回调一次onCount方法。

CTimer中其余提供的方法介绍：
    
     暂停COUNT_60这个倒计时：
     CTimer.pause(COUNT_60); （回调onCountPause）
     
     暂停后继续COUNT_60这个倒计时：
     CTimer.resume(COUNT_60); （首先回调onCountRestart然后正常流程）
     
     停止COUNT_60这个倒计时：
     CTimer.stop(COUNT_60); (回调onStop,onStop方法只有在调用stop或stopAll方法后才会回调，正常倒计时结束只会回调onCountOver)
     
     结束所有的倒计时：
     CTimer.stopAll();

  
