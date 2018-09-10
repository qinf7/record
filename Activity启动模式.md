
# Activity启动模式与生命周期 #
## 任务栈 ##
 在Android中是使用任务栈(Task)来管理Activity,根据Activitiy启动先后顺序插入任务栈中.任务栈具有后进先出的特性.而只有在处于栈顶的Activity才可以与用户进行交互.
 
 在ActivityManifest中可以通过给<activity>标签的android:taskAffinity属性来指定栈,但需要配合Activity启动模式SingleTask或者SingleInstance.
 
 如果Activity没有显式指定taskAffinity属性,那么的它的这个属性就是Application的taskAffinity.如果Application也没有指定属性值,那么指就是包名.
 
    注:一个应用程序不一定只有一个任务栈.如果栈内没有Activity存在那么该任务栈便会被销毁.
    
 ## 启动模式 ##

 在实际项目中我们根据特定需求为每个Activity指定合适的启动模式。
 
 在ActivityManifest中通过给<activity>标签的android:launcherMode属性来指定启动模式。
     
 启动模式分为四种:Standard、SingleTop、SingleTask和SingleInstance.

* Standard(标准模式)

  Standard是Activity中默认的启动模式。在不进行指定的情况下,所有的Activity都会自动使用该模式.
  
  现在我建立了两个Activity:FirstActivity、SecondActivity,未指定启动模式.
   
  FirstActivity跳转SecondActivity,SecondActivity跳转FirstActivity.随后依次返回退出.
  
  进栈生命周期如下: ![standard启动生命周期](https://raw.githubusercontent.com/qinf1996/record/master/standrd%E5%90%AF%E5%8A%A8%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)
  
  我们看到FirstActivity先执行onPause,经过SecondActivity的onCreate,onStart,onResume.然后再执行FirstActivity的onSaveInstanceState和onStop.
  
      注:onSaveInstanceState是Activity在异常销毁时(如内存不足杀死)保存状态时所调用的一个方法,它执行在onPasue前onStop后.
      
      FirstActivity我们启动了两次,在Standrd启动模式下会创建两个不同的FirstActivity实例入栈
      
  退栈生命周期如下：![standard退出生命周期](https://raw.githubusercontent.com/qinf1996/record/master/standrd%E9%80%80%E5%87%BA%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)
  
  同样我们看到最上层的FirstActivity先执行onPause,等SecondActivity执行onRestart、onStart、onResume后,再执行FirstActivity的onStop、onDestory销毁Activity.
  
* SingleTop(栈顶模式)
  SingleTop:如果启动的Activity已经在栈顶了那么便会选择复用此Activity.如果不在栈顶那么便会正常启动Activity入栈退栈.
  
  还是刚才那两个Actviity.我们将FirstActivity的LauncherMode设置为SingleTop,SecondActivity保持不变.
  
  我们分两种情况进行查看生命周期:
    
    1、FirstActivity启动FirstActivity,并返回.Activity生命周期如下:![singletop-1](https://raw.githubusercontent.com/qinf1996/record/master/singletop%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-1.png)
    
      注: 我们看到FirstActivity并没有重新创建,指向是同一个实例.执行顺序为:onPause、onNewIntent、onResume。
      
      onNewIntent是Activity进行复用时会触发的函数.
      
   2、FirstActivity启动SecondActivity,SecondActivity又启动FirstActivity.生命周期如下:![singletop-2](https://raw.githubusercontent.com/qinf1996/record/master/singletop%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png)
    
      注: 我们看到FirstActivity设置SingleTop由于并没有处于栈顶,SecondActivity启动时又创建了一个FirstActivity实例.
      
* SingleTask(唯一实例模式)
   
   SingleTask:设置该启动的Activity会在任务栈中只会保持一个实例,并且会将处于它后面的Activity给销毁.
   
   还是刚才那两个Actviity.我们将FirstActivity的LauncherMode设置为SingleTask,SecondActivity保持不变.
   
   FirstActivity启动SecondActivity,SecondActivity又启动FirstActivity.并退出.
