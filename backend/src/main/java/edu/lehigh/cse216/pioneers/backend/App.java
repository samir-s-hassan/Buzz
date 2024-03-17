package edu.lehigh.cse216.pioneers.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import spark.Spark;
import com.google.gson.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


// Import Google's JSON library

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    private static final String DEFAULT_PORT_DB = "5432";
    private static final int DEFAULT_PORT_SPARK = 4567;

    /**
     * Get a fully-configured connection to the database, or exit immediately
     * Uses the Postgres configuration from environment variables
     * 
     * NB: now when we shutdown the server, we no longer lose all data
     * 
     * @return null on failure, otherwise configured database object
     */
    private static Database getDatabaseConnection() {
        if (System.getenv("DATABASE_URL") != null) {
            return Database.parseURL(System.getenv("DATABASE_URL"), DEFAULT_PORT_DB);
        }

        Map<String, String> env = System.getenv();
        String ip = env.get("POSTGRES_IP");
        String port = env.get("POSTGRES_PORT");
        String user = env.get("POSTGRES_USER");
        String pass = env.get("POSTGRES_PASS");
        return Database.getDatabase(ip, port, user, pass);
    }

    /**
     * Get an integer environment variable if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }
    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends.  This only needs to be called once.
     * 
     * @param origin The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
    // Create an OPTIONS route that reports the allowed CORS headers and methods
    Spark.options("/*", (request, response) -> {
        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }
        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
        }
        return "OK";
    });

    // 'before' is a decorator, which will run before any 
    // get/post/put/delete.  In our case, it will put three extra CORS
    // headers into the response
    Spark.before((request, response) -> {
        response.header("Access-Control-Allow-Origin", origin);
        response.header("Access-Control-Request-Method", methods);
        response.header("Access-Control-Allow-Headers", headers);
    });
}
    /**
     * The main method that initializes the HTTP server, sets up routes for various HTTP requests,
     * and handles incoming requests and responses. This method uses the Spark framework for handling
     * HTTP requests and the Gson library for handling JSON objects.
     *
     * @param args Command line arguments if any
     */
    public static void main(String[] args) {

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        Hashtable<Integer, Integer> sessionTracker = new Hashtable<Integer, Integer>();
        // Database holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        // every time we start the server, we'll have an empty Database,
        // with IDs starting over from 0.
        final Database Database = getDatabaseConnection();

        // Set the port on which to listen for requests from the environment
        Spark.port(getIntFromEnv("PORT", DEFAULT_PORT_SPARK));

        // Set up the location for serving static files.  If the STATIC_LOCATION
        // environment variable is set, we will serve from it.  Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/auth");
            return "";
        });
    
        // GET route that returns all message titles and Ids. All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.
        Spark.get("/ideas", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, Database.selectAllPosts()));
        });

        // GET route that returns everything for a single row in the Database.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/ideas/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Post data = Database.selectOnePost(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new element to the Database. This will read
        // JSON from the body of the request, turn it into a SimplePostRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        Spark.post("/ideas", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimplePostRequest req = gson.fromJson(request.body(), SimplePostRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = Database.insertPost(req.mUserId, req.mIdea, req.mLikes); //CHANGED P1S6
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // PUT route for updating a row in the Database. This is almost
        // exactly the same as POST
        Spark.put("/ideas/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimplePostRequest req = gson.fromJson(request.body(), SimplePostRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = Database.updateOnePost(idx, req.mLikes, req.mIdea);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result)); 
                //can work on this later where the return message doesn't display the ID but the status code with 1 works and -1 doesn't
            }
        });

        // DELETE route for removing a row from the Database
        Spark.delete("/ideas/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int result = Database.deleteRowPost(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // GET route for all users
        Spark.get("/users", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, Database.selectAllUsers()));
        });

        // GET route for a single user
        Spark.get("/users/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            User data = Database.selectOneUser(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new user
        // Used when a user logs in for the first time? (or maybe not at all)
        Spark.post("/users", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleUserRequest req = gson.fromJson(request.body(), SimpleUserRequest.class);
            System.out.print(req);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            // int newId = Database.insertUser(req.mUsername, req.mEmail, req.mGenderIdentity, req.mSexualOrientation, req.mBio);
            int newId = 0;
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // PUT route for updating a user
        Spark.put("/users/:id", (request, response)->{
            int idx = Integer.parseInt(request.params("id"));
            SimpleUserRequest req = gson.fromJson(request.body(), SimpleUserRequest.class);
            response.status(200);
            response.type("application/json");
            int result = Database.updateOneUser(idx, req.mUsername, req.mEmail, req.mGenderIdentity, req.mSexualOrientation, req.mBio, req.mValid);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result)); 
            }
        });

        // DELETE route for removing a user
        Spark.delete("/users/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            response.status(200);
            response.type("application/json");
            int result = Database.deleteUser(idx);
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // get all comments for a posts
        Spark.get("/ideas/:postId/comments", (request, response) -> {
            int postId = Integer.parseInt(request.params("postId"));
            response.status(200);
            response.type("application/json");
            ArrayList<Comment> commentList = Database.selectAllComments(postId);
            if (commentList == null) {
                return gson.toJson(new StructuredResponse("error", postId + " not found", null));
            } 
            // commentList.removeIf(comment -> comment.mPostId != postId);
            if (commentList.size() == 0) {
                return gson.toJson(new StructuredResponse("error", postId + " has no comments", null));
            } 
            return gson.toJson(new StructuredResponse("ok", null, commentList));
            
        }); 

        //update comment
        Spark.put("ideas/:postId/comments/:commentId", (request, response) ->{
            int commentId = Integer.parseInt(request.params("commentId"));
            SimpleCommentRequest req = gson.fromJson(request.body(), SimpleCommentRequest.class);
            response.status(200);
            response.type("application/json");
            int result = Database.updateOneComment(commentId, req.mContent);
            if (result == -1){
                return gson.toJson(new StructuredResponse("error", "unable to update row " + commentId, null));
            }
            else{
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // get a single comment for a post
        Spark.get("/ideas/:postId/comments/:commentId", (request, response) -> {
            int commentId = Integer.parseInt(request.params("commentId"));
            response.status(200);
            response.type("application/json");
            Comment comment = Database.selectOneComment(commentId);
            if (comment == null) {
                return gson.toJson(new StructuredResponse("error", commentId + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, comment));
            }
        });

        // add a comment to a post
        Spark.post("/ideas/:postId/comments", (request, response) -> {
            int postId = Integer.parseInt(request.params("postId"));
            SimpleCommentRequest req = gson.fromJson(request.body(), SimpleCommentRequest.class);
            response.status(200);
            response.type("application/json");
            int newId = Database.insertComment(req.mUserId, postId, req.mContent);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        //Posting a new interaction
        Spark.post("/ideas/:postId/interactions", (request, response) -> { 
            int postId = Integer.parseInt(request.params("postId"));
            SimpleInteractionRequest req = gson.fromJson(request.body(), SimpleInteractionRequest.class);
            Interaction checkDB = Database.selectOneInteraction(req.mUserId, postId);
            if (checkDB == null){
                response.status(201);
                response.type("application/json");
                System.out.println("Interaction is null");
                int inserted = Database.insertInteraction(postId, req.mUserId,  req.mInteraction);
                if (inserted == -1) {
                    return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
                } else {
                    if (req.mInteraction == 1){
                        Database.changeLikes(postId, true);
                    }
                    else if (req.mInteraction == -1){
                        Database.changeLikes(postId, false);
                    }
                    return gson.toJson(new StructuredResponse("ok", "" + inserted, null));
                }
            }
            else {
                response.status(200);
                response.type("application/json");
                int updated = Database.updateInteraction(req.mUserId, postId, req.mInteraction);
                int oldInteraction = checkDB.interactionType;
                System.out.println(oldInteraction + " updated");
                System.out.println(req.mInteraction + " interaction");
                if (updated == -5 || (oldInteraction == -1 && req.mInteraction == -1) || (oldInteraction == 1 && req.mInteraction == 1)) {
                    return gson.toJson(new StructuredResponse("error", "error updating", null));
                } else {
                    if (req.mInteraction == 1 && oldInteraction == -1){
                        Database.changeLikes(postId, true);
                        Database.changeLikes(postId, true);
                    }
                    else if (req.mInteraction == -1 && oldInteraction == 1){
                        Database.changeLikes(postId, false);
                        Database.changeLikes(postId, false);
                    }
                    else if(req.mInteraction == 0 && oldInteraction == 1){
                        Database.changeLikes(postId, false);
                    }
                    else if(req.mInteraction == 0 && oldInteraction == -1){
                        Database.changeLikes(postId, true);
                    }
                    else if(oldInteraction == 0 && req.mInteraction == 1){
                        Database.changeLikes(postId, true);
                    }
                    else if(oldInteraction == 0 && req.mInteraction == -1){
                        Database.changeLikes(postId, false);
                    }
                    else if(oldInteraction == 1 && req.mInteraction == 0){
                        Database.changeLikes(postId, false);
                    }
                    else if(oldInteraction == -1 && req.mInteraction == 0){
                        Database.changeLikes(postId, true);
                        
                    }
                    return gson.toJson(new StructuredResponse("ok", "" + updated, null));
                }
            }

        });

        Spark.post("/login/auth", (request, response) -> {
        String idTokenString = request.queryParams("credential");
        // SimpleUserRequest req = gson.fromJson(request.body(), SimpleUserRequest.class);
        GoogleIdToken.Payload payload = Authentication.authenticateToken(idTokenString);
        //figure out how to have client store a user id and recall it.
        int userId =  Integer.parseInt((String)payload.get("sub"));
        if (Database.selectOneUser(userId) == null){
            return gson.toJson(new StructuredResponse("User Not in DB", "create new user", null));
        }
        else {
            int sessionId = (int) Math.random() * 1000000000;
            sessionTracker.put(userId, sessionId);
        }
        return gson.toJson(new StructuredResponse("ok", "" + "updated", null));
        });

        Spark.post("login/create-new-user", (request, response) ->{
        SimpleUserRequest req = gson.fromJson(request.body(), SimpleUserRequest.class);
        int newId = Database.insertUser(0,req.mUsername, req.mEmail, req.mGenderIdentity, req.mSexualOrientation, req.mBio);
        if (newId == -1) {
            return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
        } else {
            return gson.toJson(new StructuredResponse("ok", "" + newId, null));
        }
        });
    }
}
