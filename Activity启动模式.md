
# Activity启动模式与生命周期 #
## 任务栈 ##
 在Android中是使用任务栈(Task)来管理Activity, 根据Activitiy启动先后顺序插入任务栈中. 任务栈具有后进先出的特性. 而只有在处于栈顶的Activity才可以与用户进行交互.
 
 在ActivityManifest中可以通过给<activity>标签的android:taskAffinity属性来指定栈, 但需要配合Activity启动模式SingleTask或者SingleInstance.
 
 如果Activity没有显式指定taskAffinity属性, 那么的它的这个属性就是Application的taskAffinity. 如果Application也没有指定属性值,那么指就是包名.
 
    注:一个应用程序不一定只有一个任务栈. 如果栈内没有Activity存在那么该任务栈便会被销毁.
    
 ## 启动模式 ##

 在实际项目中我们根据特定需求为每个Activity指定合适的启动模式。
 
 在ActivityManifest中通过给<activity>标签的android:launcherMode属性来指定启动模式。
     
 启动模式分为四种:Standard、SingleTop、SingleTask和SingleInstance.

* Standard(标准模式)

 > Standard是Activity中默认的启动模式. 在不进行指定的情况下, 所有的Activity都会自动使用该模式.
  
  现在我建立了两个Activity: FirstActivity、SecondActivity, 未指定启动模式.
   
  FirstActivity跳转SecondActivity, SecondActivity跳转FirstActivity. 随后依次返回退出.
  
  进栈生命周期如下: ![standard启动生命周期](https://raw.githubusercontent.com/qinf1996/record/master/standard%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-1.png)
  
  我们看到FirstActivity先执行onPause,经过SecondActivity的onCreate,onStart,onResume. 然后再执行FirstActivity的onSaveInstanceState和onStop.
  
      注:onSaveInstanceState是Activity在异常销毁时(如内存不足杀死)保存状态时所调用的一个方法, 它执行在onPasue前onStop后.
      
      FirstActivity我们启动了两次, 在Standrd启动模式下会创建两个不同的FirstActivity实例入栈
      
  退栈生命周期如下：![standard退出生命周期](https://raw.githubusercontent.com/qinf1996/record/master/standard%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png)
  
  同样我们看到最上层的FirstActivity先执行onPause,等SecondActivity执行onRestart、onStart、onResume后, 再执行FirstActivity的onStop、onDestory销毁Activity.
  
* SingleTop(栈顶模式)

   > SingleTop:如果启动的Activity已经在栈顶了那么便会选择复用此Activity. 如果不在栈顶那么便会正常启动Activity入栈退栈.
  
     还是刚才那两个Actviity. 我们将FirstActivity的LauncherMode设置为SingleTop,SecondActivity保持不变.
  
     我们分两种情况进行查看生命周期:
    
     1、FirstActivity启动FirstActivity, 并返回.Activity生命周期如下:![singletop-1](https://raw.githubusercontent.com/qinf1996/record/master/singletop%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-1.png)
    
      注: 我们看到FirstActivity并没有重新创建, 指向是同一个实例. 执行顺序为:onPause、onNewIntent、onResume.  
      onNewIntent是Activity进行复用时会触发的函数.
      
     2、FirstActivity启动SecondActivity,SecondActivity又启动FirstActivity. 生命周期如下:![singletop-2](https://raw.githubusercontent.com/qinf1996/record/master/singletop%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png)
    
      注: 我们看到FirstActivity设置SingleTop由于并没有处于栈顶, SecondActivity启动时又创建了一个FirstActivity实例.
      
* SingleTask(唯一实例模式)
   
    > SingleTask:Activity设置此启动模式, 如果Activity在任务栈中存在实例, 将会复用任务栈中Activity并销毁该Activity实例后入栈的Activity实例.  
    如果Activity在任务栈中无实例, 那么便会创建一个实例入栈.
   
    还是刚才那两个Actviity. 我们将FirstActivity的LauncherMode设置为SingleTask, SecondActivity保持不变.
   
    FirstActivity启动SecondActivity,SecondActivity又启动FirstActivity并退出. 生命周期如下:![singletask-1](https://raw.githubusercontent.com/qinf1996/record/master/singletask%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-1.png)
   
      注:我们看到在SecondActivity再次启动FirstActivity时,FirstActivity进行了复用.  
      
      生命周期:FirstActivity--OnNewIntent、onRestart、onStart、onResume, SecondActivity--onPause、onStop、onDestory.  

      注意这里会有一个坑, 假设这时你SecondActivity再次启动FirstActivity时需要携带参数, 你会发现在FirstActivity中,getIntent()获取的值一直为null.  
  
      其实是因为Activity复用时会调用onNewIntent并传递一个新的Intent, 也就是刚才启动时候携带参数的Intent.
      
    在前面我们说过在SingleTask启动模式下, 我们是可以指定栈并生效的. 现在我们把SecondActivity启动模式设置为SingleTop并指定栈名, FirstActivity设置默认启动模式. 
   
    FirstActivity启动SecondActivity, SecondActivity启动FirstActivity. 生命周期如下:![singletask-2](https://raw.githubusercontent.com/qinf1996/record/master/singletask%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png)

      注: 我们看到第一个FirstActivity和SecondActivity是处于不同栈内的, SecondActivity启动的FirstActivity是处于同一栈内的.  
    
     在Android中一般情况下, 谁启动你的, 你就跟它处于同一个栈中.当然也有特殊情况,后面会说.
      
     我们再看一下, 将FirstActivity启动模式也设置为SingleTask,SecondActivity保持不变.
    
     还是按照刚才的启动顺序看一下生命周期:![singletask-3](https://raw.githubusercontent.com/qinf1996/record/master/singletask%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-3.png)
    
      注:我们看到这时SecondActivity启动FirstActivity并没有重新创建实例且不属于同栈内, 而是复用了原来FirstActivity.
      
 * SingleInstance(单栈模式)

   > SingleInstance: 设置该模式会让Activity处于一个单独的任务栈中, 且该栈只会有这一个实例. 可自己通过taskAffinif来指定栈名, 也可不指定系统会默认帮你创建一个.
    
     将SecondActivity启动模式设置为SingleInstance. FirstActivity启动SecondActivity, SecondActivity再启动FirstActivity. 生命周期如下:
   ![singleinstance-1]{https://raw.githubusercontent.com/qinf1996/record/master/singleinstance%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png}
    
       注:我们看到SecondActivity就算启动FirstActivity, FirstActivity也并没有和SecondActivity处于同一个栈中.
      
     再模拟一个情景,FirstActivity默认启动模式, SecondActivity设置SingleInstance启动模式并指定栈名, ThirdActivity设置SingleTask启动模式并指定与SecondActivity相同栈名. 生命周期如下: ![singleinstance-2](https://raw.githubusercontent.com/qinf1996/record/master/singleinstance%E5%90%AF%E5%8A%A8%E6%A8%A1%E5%BC%8F-2.png)

       注:就算我们将SecondActivity和ThirdActivity指定了同一个栈名,它两还是并没有处于同一个栈内.
 
 
