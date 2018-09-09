
# Activity启动模式 #
 在实际项目中我们根据特定需求为每个Activity指定合适的启动模式。
 
 在ActivityManifest中给<activity>标签指定android:launcherMode属性来指定。
     
 启动模式分为四种:Standrd、SingleTop、SingleTask和SingleInstance。

* Standrd(标准模式)

  Standrd是Activity中默认的启动模式。在不进行指定的情况下,所有的Activity都会自动使用该模式。
  
  现在我建立了三个Activity:FirstActivity、SecondActivity,未指定启动模式。
   
  FirstActivity跳转SecondActivity,SecondActivity跳转FirstActivity.随后依次返回退出生命周期如图:
