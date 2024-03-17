# Mobile App

Instructions on launching mobile app (Creating a Mobile Front-end with Flutter)  
1. Start from the root of the repository  
1. Make sure you are on mobile branch. You can do this by "git checkout mobile." To verify which branch you are on, you can also run "git checkout"  
1. Enter the mobile directory. Then, enter the flutter directory. Lastly enter, the "flutter_app1" directory. This is the directory containing the flutter application. You can do this each time by "cd directory_name"  
1. Open this directory in Visual Studio Code
1. Looking at the bottom right corner on VS Code, click on the devices section (Usually something will be written like "macOS" or "Chrome" or "Windows")
1. You can select any device to use (I chose the Pixel 2 API 33). Start this mobile device.
1. To run the app, you can either Invoke Run > Start Debugging on VS Code (top right corner) OR Press F5 on your computer 
1. It might ask you to choose your debugger in which case, choose the "Flutter and Dart" option
1. You are done; You can close the app by quitting the application

Dokku App commands

ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start team-pioneers' 
ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:stop team-pioneers' 
ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'logs team-pioneers -t'