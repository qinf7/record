
# Activity启动模式 #
 在实际项目中我们根据特定需求为每个Activity指定合适的启动模式。
 
 在ActivityManifest中给<activity>标签指定android:launcherMode属性来指定。
     
 启动模式分为四种:Standrd、SingleTop、SingleTask和SingleInstance。

* Standrd(标准模式)

  Standrd是Activity中默认的启动模式。在不进行指定的情况下,所有的Activity都会自动使用该模式。
  
  现在我建立了三个Activity:FirstActivity、SecondActivity,未指定启动模式。
   
  FirstActivity跳转SecondActivity,SecondActivity跳转FirstActivity.随后依次返回退出.
  
  启动生命周期如下: ![standrd启动生命周期](https://raw.githubusercontent.com/qinf1996/record/master/standrd%E5%90%AF%E5%8A%A8%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png "standrd启动生命周期")
  
  我们看到FirstActivity先执行onPause,经过SecondActivity的onCreate,onStart,onResume.然后再执行FirstActivity的onSaveInstanceState和onStop.
  
      注:onSaveInstanceState是Activity在异常销毁时(如内存不足杀死)保存状态时所调用的一个方法,它执行在onPasue前onStop后.
      
  退出生命周期如下：
