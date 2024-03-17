# Back-End Server

Created artifacts through JavaDoc:  
[Link to All Classes](target/site/apidocs/allclasses-index.html)  
[Link to All Packages](target/site/apidocs/allpackages-index.html)  
[Link to Help](target/site/apidocs/help-doc.html)  
[Link to Index](target/site/apidocs/index-all.html)  
[Link to Package Classes](target/site/apidocs/index.html)  
[Link to Hierarchy](target/site/apidocs/overview-tree.html)

Backend  
    
    Local
        1. On a new terminal, cd into the the root of your project repository
        2. Now, cd into the backend folder of your backend branch
        3. Run "mvn clean"
        4. Run "mvn package"
        5. Run PORT=8998 DATABASE_URL=postgres://(your db user):(your db password)@(your db host)/(your db user) java -jar ./target/backend-1.0-SNAPSHOT-jar-with-dependencies.jar OR PORT=8998 DATABASE_URL=postgres://(your db user):(your db password)@(your db host)/(your db user) mvn exec:java 
        6. Based on the terminal output, we will receive a link to where the backend is listening on (the previous command has the backend run on http://localhost:8998) but depending on what the PORT = {} it will be different 
        7. Since we are no longer serving up our index.html a 404 error not found is expected but we can visit http://localhost:8998/ideas to see our backend
        8. Once you are done, you can close all processes by running Ctrl^C and then closing the terminal
  
    Remote 
        1. On a new terminal, ssh into dokku using the command "ssh yourUserID@dokku.cse.lehigh.edu"
        2. On a separate terminal, cd into the root of your project repository
        3. The following commands will all be run on this separate terminal
        4. Make sure you have CORS enabled which you can do by running the command "ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'config:set team-pioneers CORS_ENABLED=true'
        5. Start your app with the command "ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start team-pioneers'"
        6. If your app is already started and already CORS enabled, these commands won't cause any harm
        7. You will receive two links to open your Dokku application to which you can visit
        8. Once you are done, disable CORS: ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'config:set team-pioneers CORS_ENABLED=false' AND shutdown your app: ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:stop team-pioneers'
        9. Exit out of dokku.cse.lehigh.edu by typing the command "exit" on its terminal Admin - Local

    Javadoc 
        1. On a new terminal, cd into the the root of your project repository
        2. Now, cd into the backend folder of your backend branch  
        3. Run "mvn javadoc:javadoc"
        4. The html output files will be located in target/site/apidocs

    Changes
        User,comment and interaction tables
        Added skeleton for oauth verification
        Users
            Users have an id, username, email, sexual orientation, gender identity, bio and validation
            User id is retrieved from google
            Admin can validate/invalidate users
        Comments
            Contain commentId, postId, userId and content
            Tied to a post by postId
        Interactions
            Has userId, postId and interaction type
            interaction type can be -1 for dislike, 0 for neutral and 1 for like
            Tied to post by postId
        
    Debt
        Oauth needs to save sessionId and use it for subsequent requests
        Date does not always appear correctly on frontend
        Some methods need comments
        Not enough tests


