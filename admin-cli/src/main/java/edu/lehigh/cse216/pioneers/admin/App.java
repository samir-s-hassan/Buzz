package edu.lehigh.cse216.pioneers.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */


 //Things to add:
 // fix view all rows, something wrong with populating the array. 
 // invalidating a user and a post, should be simple. just changing the values of valid to 0 or 1. 
 // many of the commands still need to be changed so they can actually change the new tables. This will be tedious, but not to hard.
 // creating a new table --> done
 //
public class App {

    // Maximum message or "idea" length
    private static final int MAX_IDEA_LENGTH = 2048;

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create a new table");
        //System.out.println("  [D] Drop a table");
        System.out.println("  [I] Interact with a specific database");
        //This has been moved to only be displayed when the admin selects the specifc datatable that they would like to interact with
            // System.out.println("  [1] Query for a specific row");
            // System.out.println("  [*] Query for all rows");
            // System.out.println("  [-] Delete a row");
            // System.out.println("  [+] Insert a new row");
            // System.out.println("  [~] Update a row's idea");
            // System.out.println("  [L] Update a row's likes");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * prints the menu of the interact section
     * @param tableName name of the table that the user is interacting with
     */
    static void menuInteract(String tableName){
            System.out.println("What would you like to do with " + tableName);
            System.out.println("  [1] Query for a specific row");
            System.out.println("  [*] Query for all rows");
            System.out.println("  [-] Delete a row");
            System.out.println("  [+] Insert a new row");  
            System.out.println("  [D] Drop a table");
            if(tableName.equals("posttbl")){
                System.out.println("  [L] Update a row's likes");
                System.out.println("  [P] Invalidate a post");
                System.out.println("  [~] Update a row's idea");//only for post and comment tables
            }
            if(tableName.equals("usertbl")){
                System.out.println("  [U] Invalidate a user");
            }
            if(tableName.equals("commenttbl")){//add invalidate here 
                System.out.println("  [C] Update Comment");
                System.out.println("  [~] Update a row's idea");//only for post and comment tables
            }
    }
    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TDI1*-+~Lq?U";

        //printing out the menu of options for the admin to do from the menu method above
        menu();

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

     static char promptInteract(BufferedReader in, String tableName) {
        // The valid actions:
        String actions = "1*-+~LDUCP";

        //printing out the menu of options for the admin to do from the menu method above
        menuInteract(tableName);

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }
    

    /**
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @return users validated table name
     */
    static String getTableName(BufferedReader in, char action){
        
        while(true){
            String tableName = "";

            if(action == 'T'){
                System.out.println("What name should the new table have?");
            }else if(action == 'I'){   
                System.out.println("What table should be interacted with?");
            }else if(action == 'D'){
                System.out.println("What table should be dropped?");
            }

            try{
            tableName = in.readLine();
            } catch (IOException e){
                e.printStackTrace();
                continue;
            }
            if (tableName.length() > 0){
                return tableName;
            }
        }
    }

    /**
     * Ask the user to enter a String message with a character limit
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param idea A message to display when asking for input
     * @param maxLength The maximum allowed length for the string
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String idea, int maxLength) {
        String s = "";
        try {
            while (true) {
                System.out.print(idea + " :> ");
                s = in.readLine();
                if (s.length() <= maxLength) {
                    break;
                } else {
                    System.out.println("Idea is too long. Please enter an idea with " + maxLength + " characters or less.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param idea A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String idea) {
        int i = -1;
        try {
            System.out.print(idea + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println();
            System.out.println("Please input an integer.");
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        Scanner scanner = new Scanner(System.in);


        // Get a fully-configured connection to the database, or exit 
        // immediately
        // Declare db at the top of your main method so it is in scope for all the action blocks
        Database db = null; // Initialize to null
       

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                //create table
                String tableName = getTableName(in, action);
                db = Database.getDatabase(ip, port, user, pass, tableName);
                 if (db == null)
                    return;
                db.createTable();
            } else if ( action == 'I'){
                //interact with a table
                String tableName = getTableName(in, action);
                db = Database.getDatabase(ip, port, user, pass, tableName);
                if (db == null){
                    return;
                }
                //menuInteract(tableName);
                char actionInteract = promptInteract(in, tableName);

                if ( actionInteract == '1'){ // Query for specific row
                    int id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    //query for row in user table
                    if(tableName.equals("usertbl")){
                        RowDataUser res = db.selectOneUser(id);
                        if (res != null) {
                            System.out.printf("Username: %-20s\tEmail: %-10s\tGender Identity: %-10s\tSexual Orientation: %-15s\tBio: %-15s\tValid:%s\n", res.mUsername, res.mEmail, res.mGenderidentity, res.mSexualorientation, res.mBio, res.mValid); 
                        }
                        else{
                            System.out.println("Row with id of " + id + " does not exist in the database. Please try again.");
                        }

                    }else if (tableName.equals("commenttbl")){
                        RowDataComment res = db.selectOneComment(id);
                        if (res != null) {
                            System.out.printf("CommentID: %s\t User ID: %s\tPost ID: %s\tContent: %-50s\tCreated: %s\t Attachment: %s\t Link: %s\n", res.mCommentid, res.mUserid, res.mPostid, res.mContent, res.mCreated, res.mAttachment, res.mLink); 
                        } else {
                            System.out.println("NO row found with id = " + id);
                        }
                    }else if(tableName.equals("posttbl")){
                        RowDataPost res = db.selectOnePost(id);
                        if (res != null) {
                            System.out.printf("Post ID: %s\tUser ID: %s\tIdea: %-30s\tLikes: %d\tCreated: %s\tValid: %s\tAttachment: %s\tLink: %s\n", res.mPostId, res.mUserId, res.mIdea, res.mLikes, res.mCreated, res.mValid, res.mAttachment, res.mLink);                        
                        } else {
                            System.out.println("NO row found with id = " + id);
                        }
                    }else if(tableName.equals("interactiontbl")){
                        RowDataInteraction res = db.selectOneInteraction(id);
                        if (res != null) {
                            System.out.println("Interaction ID: " + res.mInteractionID + "User ID: " + res.mUserID + " Post ID: " + res.mPostID + " Interaction Type: " + res.mInteractionType);
                        } else {
                            System.out.println("NO row found with id = " + id);
                        }
                    }
                }else if (actionInteract == '*'){ // displays all rows
                    if(tableName.equals("usertbl")){
                        ArrayList<RowDataUser> res = db.selectAllUser();
                        if (res == null){
                            System.out.println("Check");
                            continue;
                        }                
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (RowDataUser rd: res){
                            System.out.printf("Username: %-20s\tEmail: %-10s\tGender Identity: %-10s\tSexual Orientation: %-15s\tBio: %-15s\tValid: %s\n", rd.mUsername, rd.mEmail, rd.mGenderidentity, rd.mSexualorientation, rd.mBio, rd.mValid); 
                        }
                    }else if(tableName.equals("posttbl")){
                        ArrayList<RowDataPost> res = db.selectAllPost();
                        if (res == null){
                            continue;
                        }
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (RowDataPost rd: res){
                            System.out.printf("Post ID: %s\tUser ID: %s\tIdea: %-30s\tLikes: %d\tCreated: %s\tValid: %s\tAttachment: %s\tLink: %s\n", rd.mPostId, rd.mUserId, rd.mIdea, rd.mLikes, rd.mCreated, rd.mValid, rd.mAttachment, rd.mLink);                        }
                    }else if (tableName.equals("commenttbl")){
                    ArrayList<RowDataComment> res = db.selectAllComment();
                    if (res == null){
                        continue;
                    }
                    System.out.println("  Current Database Contents");
                    System.out.println("  -------------------------"); 
                    for (RowDataComment rd: res){
                        System.out.printf("User ID: %s\tPost ID: %s\tContent: %-50s\tCreated: %s\t Attachment: %s\t Link: %s\n", rd.mUserid, rd.mPostid, rd.mContent, rd.mCreated, rd.mAttachment, rd.mLink); 
                    }
                    }else if (tableName.equals("interactiontbl")){
                        ArrayList<RowDataInteraction> res = db.selectAllInteraction();
                        if (res == null){
                            continue;
                        }
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------"); 
                        for (RowDataInteraction rd: res){
                            System.out.printf("Interaction ID: %s\tUser ID: %s\tPost ID: %s\tInteraction Type: %s\n", rd.mInteractionID, rd.mUserID, rd.mPostID, rd.mInteractionType); 
                        }
                    }
                 
                }else if (actionInteract == '-'){ //deletes row
                    int id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    int res = db.deleteRow(id);
                    if (res == -1)
                        continue;
                    // System.out.println("bio of row");
                    // String bio = scanner.nextLine(); 
                    // int res = db.deleteRowTEST(bio); 
                    // System.out.println("  " + res + " rows deleted");                    
                    
                }else if (actionInteract == '+'){
                    if (tableName.equals("usertbl")){
                        System.out.println("Enter userid of row:");
                        int id = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Name of new row:");
                        String name = scanner.nextLine();
                        System.out.println("Email of new row:");
                        String email = scanner.nextLine();
                        System.out.println("Gender of new row:");
                        String gender = scanner.nextLine();
                        System.out.println("Sexual Orientation of new row:");
                        String sexualOrientation = scanner.nextLine();
                        System.out.println("Bio of new row:");
                        String bio = scanner.nextLine();
                        int res = db.insertRowUser(tableName, id, name, email, gender, sexualOrientation, bio);
                        if(res == -1)
                            continue;
                        System.out.println(" " + res + " rows added");
                    }else if (tableName.equals("posttbl")){
                        System.out.println("Enter userid of row:");
                        int userid = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Idea of new row:");
                        String idea = scanner.nextLine();
                        System.out.println("Likes of new row:");
                        int likes = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Attachment of new row:");
                        String attachement = scanner.nextLine();
                        System.out.println("Link of new row:");
                        String link = scanner.nextLine();
                        int res = db.insertRowPost(tableName, userid, idea, likes, attachement, link);
                        if(res == -1)
                            continue;
                        System.out.println(" " + res + " rows added");
                    }else if (tableName.equals("commenttbl")){
                        System.out.println("Enter userid of row:");
                        int userid = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Enter postid of row:");
                        int postid = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Enter content of row:");
                        String content = scanner.nextLine();
                        System.out.println("Attachment of new row:");
                        String attachement = scanner.nextLine();
                        System.out.println("Link of new row:");
                        String link = scanner.nextLine();
                        int res = db.insertRowComment(tableName, userid, postid, content, attachement, link);
                        if(res == -1)
                            continue;
                        System.out.println(" " + res + " rows added");
                    }else if (tableName.equals("interactiontbl")){
                        System.out.println("Enter userid of row:");
                        int userid = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Enter postid of row:");
                        int postid = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.println("Enter interactiontype (1 or -1) of row:");
                        int content = scanner.nextInt();
                        int res = db.insertRowInteraction(tableName, userid, postid, content);
                        if(res == -1)
                            continue;
                        System.out.println(" " + res + " rows added");
                    }
                }else if (actionInteract == '~'){ // edit a row. this will have to reworked for each table.
                    int id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    String newIdea = getString(in, "Enter the new idea (limited to 2048 characters)", MAX_IDEA_LENGTH);
                    int res = db.updateIdea(id, newIdea);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");

                }else if (actionInteract == 'L'){
                    int id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    int newLikes = getInt(in, "Enter the number of desired likes");
                    int res = db.updateLikes(id, newLikes);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");



                }else if (actionInteract == 'D'){
                    System.out.println("Do you really want to delete all the posts in the Database?");
                    System.out.println("Doing so will erase all information.");
                    System.out.print("Type out Buzz department name (our team 30 name) in all lowercase: ");
                    String verifyDeletion = "";
                    try {
                        verifyDeletion = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //if admin user does not enter this passcode exactly, will not delete the table
                    if (verifyDeletion.equals("pioneers")) {
                        db.dropTable();
                    } else {
                        System.out.println();
                        System.out.println("Deletion cancelled. Verification name does not match.");
                    }
                    continue;
                }else if(actionInteract=='U'){
                    int id = getInt(in, "what userid should we invalidate");
                    if(id == -1){
                        continue;
                    }
                    db.invalidateUser(id);
                    System.out.println("Invalidated userid: " + id);
                    RowDataUser res = db.selectOneUser(id);
                    if (res != null) {
                        System.out.printf("Username: %-20s\tEmail: %-10s\tGender Identity: %-10s\tSexual Orientation: %-15s\tBio: %-15s\tValid:%s\n", res.mUsername, res.mEmail, res.mGenderidentity, res.mSexualorientation, res.mBio, res.mValid); 
                    }
                }else if(actionInteract == 'C'){
                    int id = getInt(in, "What comment id sould we update");
                    String content = getString(in, "Enter the new comment: ", MAX_IDEA_LENGTH);
                    if(id == -1){
                        continue;
                    }
                    db.updateComment(id, content);
                }else if(actionInteract == 'P'){
                    int id = getInt(in, "What POSTID should we invalidate? ");
                    if(id ==-1){
                        continue;
                    }
                    db.invalidatePost(id);
                    System.out.println("Invalidated postid: " + id);
                    RowDataPost res = db.selectOnePost(id);
                    if (res != null) {
                        System.out.printf("Post ID: %s\tUser ID: %s\tIdea: %-30s\tLikes: %d\tCreated: %s\tValid: %s\tAttachment: %s\tLink: %s\n", res.mPostId, res.mUserId, res.mIdea, res.mLikes, res.mCreated, res.mValid, res.mAttachment, res.mLink);                        
                    }
                }
                    
        } 
        // Always remember to disconnect from the database when the program exits
        db.disconnect();
    }
}
}