package edu.lehigh.cse216.pioneers.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Database class facilitates database operations and management for the application.
 * It includes methods for creating, connecting to, and interacting with the database.
 */
public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;
    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAllPosts;
    private PreparedStatement mSelectAllUsers;
    private PreparedStatement mSelectAllCommentsForPost;
    private PreparedStatement mSelectAllInteractions;
    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOnePost;
    private PreparedStatement mSelectOneUser;
    private PreparedStatement mSelectOneComment;
    private PreparedStatement mSelectOneInteraction;
    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOnePost;
    private PreparedStatement mDeleteOneUser;
    private PreparedStatement mDeleteOneComment;
    private PreparedStatement mDeleteOneInteraction;
    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOnePost;
    private PreparedStatement mInsertOneUser;
    private PreparedStatement mInsertOneComment;
    private PreparedStatement mInsertOneInteraction;
    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOnePost;
    private PreparedStatement mUpdateOneUser;
    private PreparedStatement mUpdateOneComment;
    private PreparedStatement mUpdateOneInteraction;
    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreatePostTable;
    private PreparedStatement mCreateUserTable;
    private PreparedStatement mCreateCommentTable;
    private PreparedStatement mCreateInteractionTable;
    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropPostTable;
    private PreparedStatement mDropUserTable;
    private PreparedStatement mDropCommentTable;
    private PreparedStatement mDropInteractionTable;

    /**
     * Prepared statements for adding/removing likes
     */
    private PreparedStatement mAddLike;
    private PreparedStatement mRemoveLike;
    private PreparedStatement mGetOldLike;

    /**
     * Post is like a struct in C: we use it to hold data, and we allow
     * direct access to its fields. In the context of this Database, Post
     * represents the data we'd see in a row.
     * 
     * We make Post a static class of Database because we don't really want
     * to encourage users to think of Post as being anything other than an
     * abstract representation of a row of the database. Post and the
     * Database are tightly coupled: if one changes, the other should too.
     */

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String ip, String port, String user, String pass) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "postTbl"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception

            //CHANGED FOR THE NEW ID, IDEAS, LIKES, AND TIME
            db.mCreatePostTable = db.mConnection.prepareStatement(
                    "CREATE TABLE postTbl (postId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, idea VARCHAR(2048) NOT NULL, likes INTEGER NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            db.mCreateUserTable = db.mConnection.prepareStatement(
                        "CREATE TABLE userTbl (userId INTEGER PRIMARY KEY, username VARCHAR(128) NOT NULL, email VARCHAR(128) NOT NULL, genderIdentity VARCHAR(128) NOT NULL, sexualOrientation VARCHAR(128) NOT NULL, bio VARCHAR(2048) NOT NULL, valid VARCHAR(8) NOT NULL DEFAULT 'true')"); // valid VARCHAR(8) NOT NULL DEFAULT 'true'
            db.mCreateCommentTable = db.mConnection.prepareStatement("CREATE TABLE commentTbl (commentId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, postId INTEGER NOT NULL, content VARCHAR(2048) NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            db.mCreateInteractionTable = db.mConnection.prepareStatement("CREATE TABLE interactionTbl (interactionId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, postId INTEGER NOT NULL, interactionType INTEGER NOT NULL)");
            
            // Standard CRUD operations
            db.mDropPostTable = db.mConnection.prepareStatement("DROP TABLE postTbl");
            db.mDeleteOnePost = db.mConnection.prepareStatement("DELETE FROM postTbl WHERE postId = ?");
            db.mInsertOnePost = db.mConnection.prepareStatement("INSERT INTO postTbl VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP, default)");
            db.mSelectAllPosts = db.mConnection.prepareStatement("SELECT postId, userId, idea, likes, created, valid FROM postTbl ORDER BY created DESC");
            db.mSelectOnePost = db.mConnection.prepareStatement("SELECT * from postTbl WHERE postId=?");
            db.mUpdateOnePost = db.mConnection.prepareStatement("UPDATE postTbl SET likes = ? WHERE postId = ?");

            //Users CRUD operations
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE userTbl");
            db.mDeleteOneUser = db.mConnection.prepareStatement("DELETE FROM userTbl WHERE userId = ?");
            db.mInsertOneUser = db.mConnection.prepareStatement("INSERT INTO userTbl VALUES (default, ?, ?, ?, ?, ?, default)");
            db.mSelectAllUsers = db.mConnection.prepareStatement("SELECT userId, username, email, genderIdentity, sexualOrientation, bio, valid FROM userTbl ORDER BY username DESC");
            db.mSelectOneUser = db.mConnection.prepareStatement("SELECT * from userTbl WHERE userId=?");
            db.mUpdateOneUser = db.mConnection.prepareStatement("UPDATE userTbl SET username = ?, email = ?, genderIdentity = ?, sexualOrientation = ?, bio = ?, valid = ? WHERE userId = ?");

            //comments CRUD operations
            db.mDropCommentTable = db.mConnection.prepareStatement("DROP TABLE commentTbl");
            db.mDeleteOneComment = db.mConnection.prepareStatement("DELETE FROM commentTbl WHERE commentId = ?");
            db.mInsertOneComment = db.mConnection.prepareStatement("INSERT INTO commentTbl VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP)");
            db.mSelectAllCommentsForPost = db.mConnection.prepareStatement("SELECT commentId, userId, postId, content, created FROM commentTbl WHERE postId = ? ORDER BY created DESC");
            db.mSelectOneComment = db.mConnection.prepareStatement("SELECT * from commentTbl WHERE commentId=?");
            db.mUpdateOneComment = db.mConnection.prepareStatement("UPDATE commentTbl SET content = ? WHERE commentId = ?");

            //interactions CRUD operations
            db.mDropInteractionTable = db.mConnection.prepareStatement("DROP TABLE interactionTbl");
            db.mDeleteOneInteraction = db.mConnection.prepareStatement("DELETE FROM interactionTbl WHERE interactionId = ?");
            db.mInsertOneInteraction = db.mConnection.prepareStatement("INSERT INTO interactionTbl VALUES (default, ?, ?, ?)");
            db.mSelectAllInteractions = db.mConnection.prepareStatement("SELECT interactionId, userId, postId, interactionType FROM interactionTbl ORDER BY interactionId DESC");
            db.mSelectOneInteraction = db.mConnection.prepareStatement("SELECT * from interactionTbl WHERE userId=? AND postId=?");
            db.mUpdateOneInteraction = db.mConnection.prepareStatement("UPDATE interactionTbl SET interactionType = ? WHERE userId=? AND postId=?");

            System.out.println("this one");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param host The IP address or hostname of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param path The path to use, can be null
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String host, String port, String path, String user, String pass) {
        if (path == null || "".equals(path)) {
            path = "/";
        }

        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            String dbUrl = "jdbc:postgresql://" + host + ':' + port + path;
            Connection conn = DriverManager.getConnection(dbUrl, user, pass);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        db = db.createPreparedStatements(); // I CREATED THIS NEW PIAZZA POST, 10/2 COMMENTING OUT BECAUSE NOT WORKING
                                            // https://piazza.com/class/llprqbkdlo04r6/post/165
        return db;
    }

    private Database createPreparedStatements() { // I CREATED THIS NEW PIAZZA POST
                                                  // https://piazza.com/class/llprqbkdlo04r6/post/165

        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "postTbl"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception

            //CHANGED FOR THE NEW ID, IDEAS, LIKES, AND TIME
            this.mCreatePostTable = this.mConnection.prepareStatement(
                    "CREATE TABLE postTbl (postId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, idea VARCHAR(2048) NOT NULL, likes INTEGER NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, valid VARCHAR(8) NOT NULL DEFAULT 'true')");
            this.mCreateUserTable = this.mConnection.prepareStatement(
                        "CREATE TABLE userTbl (userId INTEGER PRIMARY KEY, username VARCHAR(128) NOT NULL, email VARCHAR(128) NOT NULL, genderIdentity VARCHAR(128) NOT NULL, sexualOrientation VARCHAR(128) NOT NULL, bio VARCHAR(2048) NOT NULL, valid VARCHAR(8) NOT NULL DEFAULT 'true'");
            this.mCreateCommentTable = this.mConnection.prepareStatement("CREATE TABLE commentTbl (commentId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, postId INTEGER NOT NULL, content VARCHAR(2048) NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            this.mCreateInteractionTable = this.mConnection.prepareStatement("CREATE TABLE interactionTbl (interactionId SERIAL PRIMARY KEY, userId INTEGER NOT NULL, postId INTEGER NOT NULL, interactionType INTEGER NOT NULL)");

            // Standard Post CRUD operations
            this.mDropPostTable = this.mConnection.prepareStatement("DROP TABLE postTbl");
            this.mDeleteOnePost = this.mConnection.prepareStatement("DELETE FROM postTbl WHERE postId = ?");
            this.mInsertOnePost = this.mConnection.prepareStatement("INSERT INTO postTbl VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP, default)");
            this.mSelectAllPosts = this.mConnection.prepareStatement("SELECT postId, userId, idea, likes,  created, valid FROM postTbl ORDER BY created DESC");
            this.mSelectOnePost = this.mConnection.prepareStatement("SELECT * from postTbl WHERE postId=?");
            this.mUpdateOnePost = this.mConnection.prepareStatement("UPDATE postTbl SET likes = ?, idea = ? WHERE postId = ?");
            this.mAddLike = this.mConnection.prepareStatement("UPDATE postTbl SET likes = likes + 1 WHERE postId = ?");
            this.mRemoveLike = this.mConnection.prepareStatement("UPDATE postTbl SET likes = likes - 1 WHERE postId = ?");

            //Users CRUD operations
            this.mDropUserTable = this.mConnection.prepareStatement("DROP TABLE userTbl");
            this.mDeleteOneUser = this.mConnection.prepareStatement("DELETE FROM userTbl WHERE userId = ?");
            this.mInsertOneUser = this.mConnection.prepareStatement("INSERT INTO userTbl VALUES (?, ?, ?, ?, ?, ?, default)");
            this.mSelectAllUsers = this.mConnection.prepareStatement("SELECT userId, username, email, genderIdentity, sexualOrientation, bio, valid FROM userTbl ORDER BY username DESC");
            this.mSelectOneUser = this.mConnection.prepareStatement("SELECT * from userTbl WHERE userid=?");
            this.mUpdateOneUser = this.mConnection.prepareStatement("UPDATE userTbl SET username = ?, email = ?, genderIdentity = ?, sexualOrientation = ?, bio = ?, valid =? WHERE userId = ?");

            //Comments CRUD operations
            this.mDropCommentTable = this.mConnection.prepareStatement("DROP TABLE commentTbl");
            this.mDeleteOneComment = this.mConnection.prepareStatement("DELETE FROM commentTbl WHERE commentId = ?");
            this.mInsertOneComment = this.mConnection.prepareStatement("INSERT INTO commentTbl VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP)");
            this.mSelectAllCommentsForPost = this.mConnection.prepareStatement("SELECT commentId, userId, postId, content, created FROM commentTbl WHERE postId = ? ORDER BY created DESC");
            this.mSelectOneComment = this.mConnection.prepareStatement("SELECT * from commentTbl WHERE commentId=?");
            this.mUpdateOneComment =this.mConnection.prepareStatement("UPDATE commentTbl SET content = ? WHERE commentId = ?");

              //interactions CRUD operations
              this.mDropInteractionTable = this.mConnection.prepareStatement("DROP TABLE interactionTbl");
              this.mDeleteOneInteraction = this.mConnection.prepareStatement("DELETE FROM interactionTbl WHERE interactionId = ?");
              this.mInsertOneInteraction = this.mConnection.prepareStatement("INSERT INTO interactionTbl VALUES (default, ?, ?, ?)");
              this.mSelectAllInteractions = this.mConnection.prepareStatement("SELECT interactionId, userId, postId, interactionType FROM interactionTbl ORDER BY interactionId DESC");
              this.mSelectOneInteraction = this.mConnection.prepareStatement("SELECT * from interactionTbl WHERE userId=? AND postId=?");
              this.mUpdateOneInteraction = this.mConnection.prepareStatement("UPDATE interactionTbl SET interactionType = ? WHERE userId=? AND postId=?");
              this.mGetOldLike = this.mConnection.prepareStatement("SELECT interactionType FROM interactionTbl WHERE userId=? AND postId=?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            this.disconnect();
            return null;
        }
        return this;
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param db_url       The url to the database
     * @param port_default port to use if absent in db_url
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database parseURL(String db_url, String port_default) {
        try {
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String host = dbUri.getHost();
            String path = dbUri.getPath();
            String port = dbUri.getPort() == -1 ? port_default : Integer.toString(dbUri.getPort());

            return getDatabase(host, port, path, username, password);
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * CHANGED FOR INSERTING AN IDEA, LIKES, AND TIME NOW
     * @param idea The idea for this new row
     * @param likes The likes for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertPost(int userId,String idea, int likes) {
        int count = 0;
        try {
            mInsertOnePost.setInt(1, userId);
            mInsertOnePost.setString(2, idea);
            mInsertOnePost.setInt(3, likes);
            count += mInsertOnePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            count = -1;
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * CHANGED FOR RETURNING ALL THE PostS NOW INCLUDING THE LIKES, IDEAS, AND TIME
     * @return All rows, as an ArrayList
     */
    ArrayList<Post> selectAllPosts() {
        ArrayList<Post> res = new ArrayList<Post>();
        try {
            ResultSet rs = mSelectAllPosts.executeQuery();
            while (rs.next()) {
                boolean valid = false;
                if (rs.getString("valid").compareTo("true") == 0){
                    valid = true;
                }
                res.add(new Post(rs.getInt("postId"), rs.getInt("userId"), rs.getString("idea"), rs.getInt("likes"), rs.getDate("created"), valid));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * CHANGED FOR ONE Post INCLUDING THE LIKE, IDEA, and TIME THROUGH THE ID
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    Post selectOnePost(int id) {
        Post res = null;
        try {
            mSelectOnePost.setInt(1, id);
            ResultSet rs = mSelectOnePost.executeQuery();
            if (rs.next()) {
                boolean valid = false;
                if (rs.getString("valid").compareTo("true") == 0){
                    valid = true;
                }
                res = new Post(rs.getInt("postId"),rs.getInt("userId") ,rs.getString("idea"), rs.getInt("likes"),rs.getDate("created"), valid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * CHANGED FOR DELETING THE ROW
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRowPost(int id) {
        int res = -1;
        try {
            mDeleteOnePost.setInt(1, id);
            res = mDeleteOnePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * CHANGED FOR UPDATING THE LIKES THROUGH GETTING THE ID
     * @param id      The id of the row to update
     * @param likes   the new likes of the update
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOnePost(int id, int likes, String idea) {
        int res = -1;
        try {
            mUpdateOnePost.setInt(1, likes);
            mUpdateOnePost.setString(2, idea);
            mUpdateOnePost.setInt(3, id);
            res = mUpdateOnePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    int changeLikes(int id, boolean up){
        int res = -1;
        if (up)
        {
            try{
                mAddLike.setInt(1, id);
            res = mAddLike.executeUpdate();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            try{
                mRemoveLike.setInt(1, id);
                res = mRemoveLike.executeUpdate();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return res;
    } 
    /**
     * Create postTbl. If it already exists, this will print an error
     */
    void createPostTable() {
        try {
            mCreatePostTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove postTbl from the database. If it does not exist, this will print
     * an error.
     */
    void dropPostTable() {
        try {
            mDropPostTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all users from the userTbl
     */
    ArrayList<User> selectAllUsers(){
        ArrayList<User> res = new ArrayList<User>();
        try{
            ResultSet rs = mSelectAllUsers.executeQuery();
            while(rs.next()){
                boolean valid = false;
                if (rs.getString("valid").compareTo("true") == 0){
                    valid = true;
                }
                res.add(new User(rs.getInt("userId"), rs.getString("username"), rs.getString("email"), rs.getString("genderIdentity"), rs.getString("sexualOrientation"), rs.getString("bio"), valid));
            }
            rs.close();
            return res;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Retrieve a user from the database
     * @param id The id of the user to retrieve
     * 
     * @return The user object, or null if the ID was invalid
     */
    User selectOneUser(int id){
        User res = null;
        try{
            mSelectOneUser.setInt(1, id);
            ResultSet rs = mSelectOneUser.executeQuery();
            boolean valid = false;
            if(rs.next()){
                if (rs.getString("valid").compareTo("true") == 0){
                valid = true;
            }
                res = new User(rs.getInt("userId"), rs.getString("username"), rs.getString("email"), rs.getString("genderIdentity"), rs.getString("sexualOrientation"), rs.getString("bio"), valid);
            }
            else{
                return null;
            }
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    int insertUser(int userId, String username, String email, String genderIdentity, String sexualOrientation, String bio){
        int count = 0;
        try{
            mInsertOneUser.setInt(1, userId);
            mInsertOneUser.setString(2, username);
            mInsertOneUser.setString(3, email);
            mInsertOneUser.setString(4, genderIdentity);
            mInsertOneUser.setString(5, sexualOrientation);
            mInsertOneUser.setString(6, bio);
            count += mInsertOneUser.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            count = -1;
        }
        return count;
    }
    int updateOneUser(int userId, String username, String email, String genderIdentity, String sexualOrientation, String bio, boolean valid){
        int res = -1;
        try{
            
            mUpdateOneUser.setString(1, username);
            mUpdateOneUser.setString(2, email);
            mUpdateOneUser.setString(3, genderIdentity);
            mUpdateOneUser.setString(4, sexualOrientation);
            mUpdateOneUser.setString(5, bio);
            if (valid){
                mUpdateOneUser.setString(6, "true");
            }
            else{
                mUpdateOneUser.setString(6, "false");
            }
            mUpdateOneUser.setInt(7, userId);
            res = mUpdateOneUser.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;

    }
    int deleteUser(int id){
        int res = -1;
        try{
            mDeleteOneUser.setInt(1, id);
            res = mDeleteOneUser.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    void CreateUserTable(){
        try{
            mCreateUserTable.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    void DropUserTable(){
        try{
            mDropUserTable.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    int insertComment(int userId , int postId, String content){
        int count = 0;
        try{
            mInsertOneComment.setInt(1, userId);
            mInsertOneComment.setInt(2, postId);
            mInsertOneComment.setString(3, content);
            count += mInsertOneComment.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            count = -1;
        }
        return count;
    }

    ArrayList<Comment> selectAllComments(int postId){
        ArrayList<Comment> res = new ArrayList<Comment>();
        try{
            mSelectAllCommentsForPost.setInt(1, postId);
            ResultSet rs = mSelectAllCommentsForPost.executeQuery();
            while(rs.next()){
                res.add(new Comment(rs.getInt("commentId"), rs.getInt("userId"), rs.getInt("postId"), rs.getString("content"), rs.getDate("created")));
            }
            rs.close();
            return res;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    Comment selectOneComment(int id){
        Comment res = null;
        try{
            mSelectOneComment.setInt(1, id);
            ResultSet rs = mSelectOneComment.executeQuery();
            if(rs.next()){
                res = new Comment(rs.getInt("commentId"), rs.getInt("userId"), rs.getInt("postId"), rs.getString("content"), rs.getDate("created"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    int deleteComment(int id){
        int res = -1;
        try{
            mDeleteOneComment.setInt(1, id);
            res = mDeleteOneComment.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    int updateOneComment(int id, String content){
        int res = -1;
        try{
            mUpdateOneComment.setString(1, content);
            mUpdateOneComment.setInt(2, id);
            res = mUpdateOneComment.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return res;
    }

void createCommentTable(){
    try{
        mCreateCommentTable.execute();
    }
    catch(SQLException e){
        e.printStackTrace();
    }
}
void deleteCommentTable(){
    try{
        mDropCommentTable.execute();
    }
    catch(SQLException e){
        e.printStackTrace();
    }
}

int insertInteraction(int postId, int userId, int interactionType){
    int count = 0;
    try{
        mInsertOneInteraction.setInt(1, userId);
        mInsertOneInteraction.setInt(2, postId);
        mInsertOneInteraction.setInt(3, interactionType);
        count += mInsertOneInteraction.executeUpdate();
    }
    catch(SQLException e){
        e.printStackTrace();
        count = -1;
    }
    return count;

}

ArrayList<Interaction> selectAllInteractions(){
    ArrayList<Interaction> res = new ArrayList<Interaction>();
    try{
        ResultSet rs = mSelectAllInteractions.executeQuery();
        while(rs.next()){
            res.add(new Interaction( rs.getInt("userId"), rs.getInt("postId"), rs.getInt("interactionType")));
        }
        rs.close();
        return res;
    }
    catch(SQLException e){
        e.printStackTrace();
        return null;
    }
}

Interaction selectOneInteraction(int userId, int postId){
    Interaction res = null;
    try{
        mSelectOneInteraction.setInt(1, userId);
        mSelectOneInteraction.setInt(2, postId);
        ResultSet rs = mSelectOneInteraction.executeQuery();
        if(rs.next()){
            res = new Interaction( rs.getInt("userId"), rs.getInt("postId"), rs.getInt("interactionType"));
        }
        else{
            return null;
        }
    }
    catch(SQLException e){
        e.printStackTrace();
    }
    return res;
}
int updateInteraction(int userId, int postId, int interaction){
    int res = -5;
    try{
        mUpdateOneInteraction.setInt(1, interaction);
        mUpdateOneInteraction.setInt(2, userId);
        mUpdateOneInteraction.setInt(3, postId);
        res = mUpdateOneInteraction.executeUpdate();
    }
    catch(SQLException e){
        e.printStackTrace();
    }
    return res;
}

}
