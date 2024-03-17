# Web Front-End to local machine

1. Make sure you are on the "web" branch of this project
2. From the root of the repository, cd into "web/buzz"
3. Once you are in the directory, run "npm start" BUT WAIT this might not work if
4. You don't have node_modules on your local machine
5. Install node_modules using the command "npm install". This command can be run from anywhere but I recommend running it in the web/buzz directory
6. Now, run "npm start" from the web/buzz directory
7. The React App is now running

# Web Front-End to Dokku

1. I did the same thing where I used React's npm build function to create a production build of the front end app. I switched to the backend-dokku branch of our repo. From the root of the repo on the backend-dokku branch, I went into src/main/resources/web. In this directory, I pasted all the contents of the build folder. The build folder is the one that was outputted when you ran the React npm build function. I didn't copy paste anything else. Essentially the src/main/resources/web has all the contents of the build folder. 
2. Once I pushed to Dokku, this worked in serving my main page of my React application. However, the issue I am now having is that the routes I had set up on my locally hosted React app are not being reflected on our Dokku pushed website. 

Dokku App commands

ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start team-pioneers'
ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:stop team-pioneers'
ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'logs team-pioneers -t'
