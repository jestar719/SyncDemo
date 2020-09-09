# SyncDemo
This demo wants to synchronize the application with the contacts on the phone through the Android synchronization framework.
 
  By writing the customized type of data to the contact database and binding the existing contacts, 
  
  so that the customized icon is displayed on the contact, you can click to jump to the application.

In order to achieve this requirement, I refer to the official document and this article[Link](https://medium.com/@stephen.cty/android-account-sync-adapter-and-contacts-contract-database-983281be4847)

This Demo shows the most basic operations for this requirement.
 
1.  Create a user in the app.
  
2.  Select your own user synchronization in the setting->account menu of the phone, or click Sync in the application. 
  
    To request the system synchronization frame synchronization

    The synchronization operation is: read the system contact data, select the first one, and then according to their account type, 

    the user writes the corresponding data to the contact database to bind the contact.

3. Open the system contact, you can see that a custom icon will be displayed on the first contact data, 
    
    click to jump to the application pre-defined page
 
Now I can create users, but the onPerformSync() method of SyncAdapter is not executed when using the system synchronization framework to synchronize
 
It is possible to write data to the contact database through code in your own application, but the corresponding icon will not be displayed on the contact, and it cannot be clicked.
 
 ### 中文说明
 这个Demo是想通过Android同步框架,让应用和手机上的联系人同步.
 
 通过把自定义的类型的数据写入到联系人数据库并绑定已存在的联系人,让联系人上显示自定义的图标,可以点击跳转到应用.
 
 为了实现这个需求我参考了官方文档及这篇文章[链 接](https://medium.com/@stephen.cty/android-account-sync-adapter-and-contacts-contract-database-983281be4847)
 
 这个Demo展示了这个需求最基本的操作.
 
 1. 在应用中创建一个用户.
 2. 在手机的setting->account菜单中选择自己的用户同步,或者在应用中点击同步.来请求系统同步框架同步
    
    同步的操作为:读取系统联系人数据,选择第一个,然后根据自己的帐户类型,用户用写入对应的数据到联系人数据库去绑定这个联系人.
    
 3. 打开系统联系人,可以看到第一个联系人数据上会显示自定义的图标,点击可以跳转到应用预先定义好的页面
 
现在我可以创建用户,但使用系统同步框架同步时SyncAdapter的onPerformSync()方法没有被执行
 
而自己应用中通过代码写入数据到联系人数据库是可以的,但联系人上不会显示对应的图标,也无法做到点击.
 
 
 ![There should be two lines, showing your own icon like WeChat](/images/1.jpg)
 
 ![Here you can see that the data is successfully inserted, TNGD is the applied label](/images/2.jpg)