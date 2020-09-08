# SyncDemo
Demo for write custom action in Contact to link app by  Android Sync Framwork

I need to design a custom action profile to attach contact database that to link my app.
So i read android document and read this pag[https://medium.com/@stephen.cty/android-account-sync-adapter-and-contacts-contract-database-983281be4847]

this Demo just show my basic request.
1 open app to create a account
2 mobile open setting->account->my account->sync or press the sync button in app. then read the first contact msg,insert data in contact database with my account to attach the contact
3 open contact to see first contact,it's should show my icon, and can press to open my app
 
Now i can create Account,the SyncAdapter can't be call onPerformSync().
 so i just  write msg to contact database in app. but not icon and link action.