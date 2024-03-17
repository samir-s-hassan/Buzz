// package edu.lehigh.cse216.pioneers.admin;

// //import java.util.ArrayList;

// import junit.framework.TestCase;

// //RUN "mvn test" IN ROOT OF ADMIN BRANCH REPO IN ORDER TO RUN TEST CASES FOR ADMIN
// public class AppTest extends TestCase {
//     private Database dbUser;
//     private Database dbComment;
//     private Database dbPost;

//     // @Override
//     // protected void setUp() {

//     //     // Create a new Database instance before each test
//     //     // Uses my own database as to not mess with that of the team's
//     //     // Next admin should use their own
//     //     db = Database.getDatabase("berry.db.elephantsql.com", "5432", "cbmonipj", "5DMD4HOqQzSrUI9HsPLmAu6Pd4QLhLQB", "usertbl");
//     // }

//     public void testUserOperations (){
//         dbUser = Database.getDatabase("berry.db.elephantsql.com", "5432", "cbmonipj", "5DMD4HOqQzSrUI9HsPLmAu6Pd4QLhLQB", "usertbl");
//         dbUser.createTable();

//         // dbUser.insertRow("usertbl");
//         // dbUser.insertRow("usertbl");
//         // dbUser.insertRow("usertbl");

//     }

//     public void testCommentOperations(){
//         dbComment = Database.getDatabase("berry.db.elephantsql.com", "5432", "cbmonipj", "5DMD4HOqQzSrUI9HsPLmAu6Pd4QLhLQB", "commenttbl");
//         dbComment.createTable();

//     } 

//     public void testPostOperations(){
//         dbPost = Database.getDatabase("berry.db.elephantsql.com", "5432", "cbmonipj", "5DMD4HOqQzSrUI9HsPLmAu6Pd4QLhLQB", "posttbl");
//         dbPost.createTable();

//     }



// }
