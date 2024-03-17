package edu.lehigh.cse216.pioneers.admin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateIdea;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateLikes;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;
    /**
     * A prepared statement for invalidating a user in our database
     */
    private PreparedStatement mInvalidateUser;
    /**
     * A prepared statement for updating a comment in our databasem
     */
    private PreparedStatement mUpdateComment;
    /**
     * A prepared statrement for invalidating a post in our database
     */
    private PreparedStatement mInvalidatePost;

    private PreparedStatement updateOne;

    private PreparedStatement mGetOldLike;
    private PreparedStatement mAlterTableComments;

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
    static Database getDatabase(String ip, String port, String user, String pass, String tableName) {
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

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception


            //check with ethan to see what the data types of the rows are in the table
            db.mCreateTable = db.mConnection.prepareStatement(createTableCommand(tableName));

            //done
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE "+tableName);

            // Standard CRUD operations
            //function for different ids
            //done
            db.mDeleteOne = db.mConnection.prepareStatement(deleteRow(tableName));

            //unique one for each table create a function.
            //create the insertone command depending on what table we are working on.
            //DONE
            db.mInsertOne = db.mConnection.prepareStatement(insertRowString(tableName));

// this will have to change depending on the table, probably can use an if statement
//DONE
            db.mSelectAll = db.mConnection.prepareStatement(selectAllString(tableName)); 
           
//need to change it for userid for usertbl
//done
            db.mSelectOne = db.mConnection.prepareStatement(selectOneString(tableName));
           
            //updates the idea of the message
            //only admin can update this
            //backend Database.java updates the likes

            //for posttbl -->orignally for tbldata but might need minor changes, invalidate columns
            db.mUpdateIdea = db.mConnection.prepareStatement("UPDATE posttbl SET idea = ?, likes = ?, attachment = ?, link = ? WHERE postid = ?");
            db.mUpdateLikes = db.mConnection.prepareStatement("UPDATE posttbl SET likes = ? WHERE postid = ?");

            //for commenttbl need an update comment content DONE
            db.updateOne = db.mConnection.prepareStatement(updateOneString(tableName));
            
            //for usertbl need to be able to invalidate user DONE
            db.mInvalidateUser = db.mConnection.prepareStatement("UPDATE usertbl SET valid = FALSE WHERE userid = ?");
            //for posttbl to invalidate a post DONE
            db.mInvalidatePost = db.mConnection.prepareStatement("UPDATE posttbl SET valid = FALSE WHERE postid = ?");

            //interactiontbl  
            db.mGetOldLike = db.mConnection.prepareStatement("SELECT interactionType FROM interactionTbl WHERE userId=? AND postId=?");

            db.mAlterTableComments = db.mConnection.prepareStatement("ALTER TABLE commenttbl ADD COLUMN attachment TEXT, ADD COLUMN link TEXT");
            // db.mAlterTableComments.executeUpdate();


        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    //insert function
    /**
     * Creates the SQL string for insertOne
     * @param tableName name of table 
     * @return SQL string
     */
    static String insertRowString(String tableName){
        String statement ="";
                if (tableName.equals("posttbl")){
                    statement = "INSERT INTO "+tableName+" VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP, default, ?, ?)";
                }else if (tableName.equals("commenttbl")){
                    statement = "INSERT INTO "+tableName+" VALUES (default, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
                }else if (tableName.equals("usertbl")){
                    statement = "INSERT INTO "+tableName+" VALUES (?, ?, ?, ?, ?, ?, default)";
                }else if (tableName.equals("interactiontbl")){
                    statement = "INSERT INTO interactionTbl VALUES (default, ?, ?, ?)";
                }
        return statement;
        }
        


    /**
     * Creates string for deleteOne
     * @param tableName name of tbale that is being interacted with
     * @return SQL string
     */
    static String deleteRow(String tableName){
        String statement ="";
        if (tableName.equals("posttbl")){
            statement = "DELETE FROM "+tableName+" WHERE postid = ?";
        }else if (tableName.equals("commenttbl")){
            statement = "DELETE FROM "+tableName+" WHERE commentid = ?";
        }else if (tableName.equals("usertbl")){
            statement = "DELETE FROM "+tableName+" WHERE bio = ?";
        }else if (tableName.equals("interactiontbl")){
            statement = "DELETE FROM " +tableName+ " WHERE interactionId = ?";
        }
        return statement;
    }

    /**
     * Creates string for selectOne
     * @param tableName name of tbale being interacted with
     * @return sql string
     */
    static String selectOneString(String tableName){
        String statement = "";
        if(tableName.equals("usertbl")){
              statement = "SELECT * from "+tableName+" WHERE userid=?";
            }else if (tableName.equals("commenttbl")){
               statement =  "SELECT * from "+tableName+" WHERE commentid=?";
            }else if (tableName.equals("posttbl")){
                statement = "SELECT * from "+tableName+" WHERE postid=?";
            }else if (tableName.equals("interactiontbl")){
                statement = "SELECT * from "+tableName+" WHERE userid=? AND postid=?";
            }
            return statement;
    }

    /**
     * Builds select all string statement depending on tableName
     * @param tableName name of table that you are selecting from
     * @return selectall statement string.
     */
    static String selectAllString(String tableName){
        String statement="";
        if(tableName.equals("usertbl")){
            statement = "SELECT userid, username, email, genderIdentity, sexualOrientation, bio, valid FROM "+tableName+" ORDER BY username DESC";
        }else if (tableName.equals("posttbl")){
            statement = "SELECT postid, userid, idea, likes, created, valid, attachment, link FROM "+tableName+" ORDER BY created DESC";
        }else if (tableName.equals("commenttbl")){
            statement = "SELECT commentid, userid, postid, content, created, attachment, link FROM "+tableName+" ORDER BY created DESC";
        }else if (tableName.equals("interactiontbl")){
            statement = "SELECT interactionId, userid, postId, interactionType FROM "+tableName+" ORDER BY interactionId DESC";
        }
        return statement;
    }

    /**
     * Builds select all string statement depending on tableName
     * @param tableName name of table that you are selecting from
     * @return selectall statement string.
     */
    static String updateOneString(String tableName){
        String statement="";
        if(tableName.equals("usertbl")){
            statement = "UPDATE "+tableName+" SET username = ?, email = ?, genderIdentity = ?, sexualOrientation = ?, bio = ?, valid =? WHERE userId = ?";
        }else if (tableName.equals("posttbl")){
            statement = "UPDATE "+tableName+" SET likes = ?, idea = ?, attachment = ?, link = ? WHERE postId = ?";
        }else if (tableName.equals("commenttbl")){
            statement = "UPDATE "+tableName+" SET content = ?, attachment = ?, link = ? WHERE commentid = ?";
        }else if (tableName.equals("interactiontbl")){
            statement = "UPDATE "+tableName+" SET interactionType = ? WHERE userid=? AND postId=?";
        }
        return statement;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
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
     *
     * @param idea The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRowUser(String tableName, int user, String name, String email, String gender, String SO, String bio) {
        int count = 0;
        try {
            mInsertOne.setInt(1, user); //userid
            mInsertOne.setString(2, name);
            mInsertOne.setString(3, email);
            mInsertOne.setString(4, gender);
            mInsertOne.setString(5, SO);
            mInsertOne.setString(6, bio);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int insertRowPost(String tableName, int user, String idea, int likes, String attachment, String link) {
        int count = 0;
        try{
            mInsertOne.setInt(1, user);
            mInsertOne.setString(2, idea); //idea
            mInsertOne.setInt(3, likes); //likes
            mInsertOne.setString(4, attachment);//attachement
            mInsertOne.setString(5, link);//link
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int insertRowComment(String tableName, int userID, int postID, String content, String attachment, String link){
        int count = 0;
        try{
            mInsertOne.setInt(1, userID); //idea
            mInsertOne.setInt(2, postID); //likes
            mInsertOne.setString(3, content);//attachement
            mInsertOne.setString(4, attachment);//attachement
            mInsertOne.setString(5, link);//link
            //mAlterTableComments.executeUpdate(); 
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int insertRowInteraction(String tableName, int userID, int postID, int interactionType){
        int count = 0;
        try{
            mInsertOne.setInt(1, userID); //idea
            mInsertOne.setInt(2, postID); //likes
            mInsertOne.setInt(3, interactionType);//attachement
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all ideas and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<Object> selectAll(String tableName) {
        ArrayList<Object> res = new ArrayList<Object>(); // Declare res here with a scope for the entire method
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                if(tableName.equals("posttbl")){
                    //ArrayList<RowDataPost> res = new ArrayList<RowDataPost>();
                    res.add(new RowDataPost(rs.getInt("postid"), rs.getInt("userid"), rs.getString("idea"), rs.getInt("likes"),  rs.getDate("created"), rs.getString("valid"), rs.getString("attachment"), rs.getString("link")));
                }else if (tableName == "commenttbl"){
                    //ArrayList<RowDataComment> res = new ArrayList<RowDataComment>();
                    res.add(new RowDataComment(rs.getInt("commentid"), rs.getInt("userid"), rs.getInt("postid"), rs.getString("content"), rs.getDate("created"), rs.getString("attachment"), rs.getString("link")));
                }else if (tableName == "usertbl"){
                    //ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
                    res.add(new RowDataUser(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("genderidentity"), rs.getString("sexualorientation"), rs.getString("bio"), rs.getString("valid")));
                }
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches all comments from table
     * @return array of all comments in the table
     */
    ArrayList<RowDataComment> selectAllComment(){
        ArrayList<RowDataComment> res = new ArrayList<RowDataComment>();
        try{
            ResultSet rs = mSelectAll.executeQuery();
            while(rs.next()){
                res.add(new RowDataComment(rs.getInt("commentid"), rs.getInt("userid"), rs.getInt("postid"), rs.getString("content"), rs.getDate("created"), rs.getString("attachment"), rs.getString("link")));
            }
            rs.close();
            return res;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }      
    }

    /**
     * Fetches all posts fro the database
     * @return array of all posts
     */
    ArrayList<RowDataPost> selectAllPost(){
        ArrayList<RowDataPost> res = new ArrayList<RowDataPost>();
        try{
            ResultSet rs = mSelectAll.executeQuery();
            while(rs.next()){
                res.add(new RowDataPost(rs.getInt("postid"), rs.getInt("userid"), rs.getString("idea"), rs.getInt("likes"),  rs.getDate("created"), rs.getString("valid"), rs.getString("attachment"), rs.getString("link")));
            }
            rs.close();
            return res;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches all users from database
     * @return array of all users
     */
    ArrayList<RowDataUser> selectAllUser(){
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try{
            ResultSet rs = mSelectAll.executeQuery();
            while(rs.next()){
                res.add(new RowDataUser(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("genderidentity"), rs.getString("sexualorientation"), rs.getString("bio"), rs.getString("valid")));
            }
            rs.close();
            return res;
            
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches all interactions from database
     * @return array of all interactions
     */
    ArrayList<RowDataInteraction> selectAllInteraction(){
        ArrayList<RowDataInteraction> res = new ArrayList<RowDataInteraction>();
        try{
            ResultSet rs = mSelectAll.executeQuery();
            while(rs.next()){
                res.add(new RowDataInteraction(rs.getInt("interactionid"), rs.getInt("userid"), rs.getInt("postid"), rs.getInt("interactiontype")));
            }
            rs.close();
            return res;
            
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataPost selectOnePost(int id) {
        RowDataPost res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataPost(rs.getInt("postid"), rs.getInt("userid"), rs.getString("idea"), rs.getInt("likes"),  rs.getDate("created"), rs.getString("attachment"), rs.getString("link"), rs.getString("valid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Queries for one comment with matching id
     * @param id id of comment
     * @return comment object that has matching id
     */
    RowDataComment selectOneComment(int id){
        RowDataComment res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataComment(rs.getInt("commentid"), rs.getInt("userid"), rs.getInt("postid"), rs.getString("content"), rs.getDate("created"), rs.getString("attachment"), rs.getString("link"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Queries for one user with matching id
     * @param id id of user
     * @return user object that has matchoing id
     */
    RowDataUser selectOneUser(int id){
        RowDataUser res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("userid"), rs.getString("username"), rs.getString("email"), rs.getString("genderidentity"), rs.getString("sexualorientation"), rs.getString("bio"), rs.getString("valid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;

    }

    /**
     * Queries for one interaction with matching id
     * @param id id of interaction
     * @return user object that has matching id
     */
    RowDataInteraction selectOneInteraction(int id){
        RowDataInteraction res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataInteraction(rs.getInt("interactionid"), rs.getInt("userid"), rs.getInt("postid"), rs.getInt("interactiontype"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;

    }
 
    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param idea The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateIdea(int id, String idea) {
        int res = -1;
        try {
            mUpdateIdea.setString(3, idea);
            mUpdateIdea.setInt(2, id);
            res = mUpdateIdea.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the comment for a row in commenttbl
     * @param id id of row that the user wants to ulpdate
     * @param comment new comment that will be inserted into commenttbl
     * @return number of comments updated
     */
    int updateComment(int id, String comment){
        int res = -1;
        try{
            mUpdateComment.setString(1, comment);
            mUpdateComment.setInt(2, id);
            res = mUpdateComment.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param likes The new number of likes on a post
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateLikes(int id, int likes) {
        int res = -1;
        try {
            mUpdateLikes.setInt(4, likes);
            mUpdateLikes.setInt(2, id);
            res = mUpdateLikes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Invalidates user in usertbl based off id
     * @param id id of the user to invalidate
     * @return number of users invalidated
     */
    int invalidateUser(int id){
        int res = -1;
        try {
            //mInvalidateUser.setString(1, "false");
            mInvalidateUser.setInt(1, id);
            res = mInvalidateUser.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    /**invalites post based of id inputted
     * 
     * @param id id of post that gets invalidated
     * @return number of posts invalidted
     */
    int invalidatePost(int id){
        int res = -1;
        try {
            mInvalidatePost.setInt(1, id);
            res = mInvalidatePost.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// "CREATE TABLE "+ tableName + " (id SERIAL PRIMARY KEY, idea VARCHAR(2048) NOT NULL, likes INTEGER NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
  /**
   * Creates the string for the createTable SQL query
   * @param tableName name of table being created
   * @return SQL string
   */
    static String createTableCommand(String tableName){
        String statement = "";
        if (tableName.equals("commenttbl")){
            statement = "CREATE TABLE "+tableName+" (commentId SERIAL PRIMARY KEY, userid INTEGER NOT NULL, postId INTEGER NOT NULL, content VARCHAR(2048) NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, attachment TEXT)";
        } else if (tableName.equals("posttbl")){
            statement = "CREATE TABLE "+tableName+" (postid SERIAL PRIMARY KEY, userId VARCHAR(30) NOT NULL, idea VARCHAR(2048) NOT NULL, likes INTEGER NOT NULL, created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, valid VARCHAR(8) NOT NULL DEFAULT 'true', attachment TEXT, link TEXT)";
        } else if (tableName.equals("usertbl")){
            statement = "CREATE TABLE "+tableName+" (userid SERIAL PRIMARY KEY, username VARCHAR(128) NOT NULL, email VARCHAR(128) NOT NULL, genderIdentity VARCHAR(128) NOT NULL, sexualOrientation VARCHAR(128) NOT NULL, bio VARCHAR(2048) NOT NULL, valid VARCHAR(8) NOT NULL DEFAULT 'true')";
        } else if (tableName.equals("interactiontbl")){
            statement = "CREATE TABLE "+tableName+" (interactionId SERIAL PRIMARY KEY, userid INTEGER NOT NULL, postId INTEGER NOT NULL, interactionType INTEGER NOT NULL)";
        }
        return statement;
    }
}