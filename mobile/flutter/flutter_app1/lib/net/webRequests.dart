import 'dart:developer' as developer;
import 'dart:convert';
import 'package:flutter_app1/models/createProfile.dart';
import 'package:flutter_app1/models/post.dart';
import 'package:flutter_app1/models/comment.dart';
import 'package:flutter_app1/models/user.dart';
import 'package:flutter_app1/models/hiddenUser.dart';

import 'package:http/http.dart' as http;

//PUT ALL THE ROUTES IN THIS ONE

/// Gets posts from backend
/// @return a promise of a list of posts
Future<List<Post>> getWebData() async {
  developer.log('Making web request...');
  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas'); 
  // ^
  //ISSUE: https://github.com/flutter/flutter/issues/132912 ?
  // //Fix is to not enable all exceptions in debug mode
  var headers = {"Accept": "application/json"}; // <String,String>{};

  var response = await http.get(
    url,
    headers: headers,
  );
  developer.log('url: $url');
  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');

  final List<Post> returnData;
  if (response.statusCode == 504) {
    //handling timeout
    developer.log('ERROR: Server timed out.');
    response = await http.get(
      url,
      headers: headers,
    );
  }
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    var res = jsonDecode(response.body);
    developer.log(res.runtimeType.toString());
    var resData = res['mData'];
    developer.log('json decode: $resData');
    if (resData is List) {
      returnData = (resData).map((x) => Post.fromJson(x)).toList();
      // returnData.sort(((a, b) => b.postTime.compareTo(a.postTime)));
    } else {
      developer.log('ERROR: Unexpected json response type (was not a List).');
      returnData = List.empty();
    }
  } else {
    throw Exception(
        'Failed to retrieve web data (server returned ${response.statusCode})');
  }

  return returnData;
}

/// Sends a new post to server
/// @param newPost the post to be sent
/// @return void
Future<void> postWebData(Post newPost) async {
  developer.log('Sending post to server...');
  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas');
  var headers = <String, String>{
    'Accept': "application/json"
  }; // <String,String>{};
  var response = await http.post(
    url,
    headers: headers,
    body: jsonEncode({'mIdea': newPost.message, 'mLikes': newPost.likes, 'mUserId': ""}), //fix my userID SAMIR
  );
  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');
  //error can return 200 
  if (response.statusCode != 200) {
    throw Exception(
        'Failed to post web data (server returned ${response.statusCode})');
  } else {
    developer.log(response.body);
    developer.log('Successfully posted web data.');
    return;
  }
}
/// Increments like count by one for specified post
/// @param post the post to be liked
/// @return void
Future<void> incrementLikes(Post post) async {
  developer.log('Updating post...');
  var url =
      Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/${post.id}/interactions');
  var headers = {'Content-type': "application/json"}; // <String,String>{};

  var response = await http.post(url,
      headers: headers,
      body: jsonEncode({'mUserId': "0", 'mInteraction': 1}));

  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');

  if (response.statusCode != 200) {
    throw Exception(
        'Failed to post web data (server returned ${response.statusCode})');
  }
}

/// Increments like count by one for specified post
/// @param post the post to be liked
/// @return void
Future<void> decrementLikes(Post post) async {
  developer.log('Updating post...');
  var url =
      Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/${post.id}/interactions');
  var headers = {'Content-type': "application/json"}; // <String,String>{};

  var response = await http.post(url,
      headers: headers,
      body: jsonEncode({'mUserId': "0", 'mInteraction': -1}));

  developer.log('Post ID: ${post.id}'); 
  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');

  if (response.statusCode != 200) {
    throw Exception(
        'Failed to post web data (server returned ${response.statusCode})');
  }
}

//trying to send profile info to backend 
Future<void> postProfileData(CreateProfile profile) async {
  developer.log('Posting profile data to server');
  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/users');
  var headers = <String, String>{
    'Accept': "application/json"
  }; // <String,String>{};

  var response = await http.put(url,
      headers: headers,
      body: jsonEncode({'mUsername': profile.mUsername, 'mEmail': profile.mEmail, 'mOrientation': profile.mSexualOrientation, 'mGender': profile.mGenderIdentity, 'mbio': profile.mBio}));

  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');
  //error can return 200 
  try {
  if (response.statusCode != 200) {
    throw Exception(
      'Failed to post web data (server returned ${response.statusCode})');
  } else {
    developer.log(response.body);
    developer.log('Successfully posted web data.');
    return;
  }
} catch (e) {
  // Handle the exception
  developer.log('Error: $e');
  // You can add further error handling or rethrow the exception if needed
  rethrow;
}
}

//getting all the comments for a post
Future<List<Comment>> getCommentsForPost(int postId) async {
  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/$postId/comments');
  var headers = {"Accept": "application/json"};

  try {
    var response = await http.get(
      url,
      headers: headers,
    );

    if (response.statusCode == 200) {
      var res = jsonDecode(response.body);
      var resData = res['mData'];

      print('Response body: $resData'); // Add this line to print the response body

      if (resData is List) {
        return (resData).map((x) => Comment.fromJson(x)).toList();
      } else {
        throw Exception('Unexpected json response type (was not a List).');
      }
    } else {
      throw Exception('Failed to retrieve comments (server returned ${response.statusCode})');
    }
  } catch (e) {
    // Handle exceptions, e.g., network issues
    throw Exception('Error fetching comments: $e');
  }
}

//getting all the comments for a post
Future<User> getUserInfo(String userID) async {
  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/users/$userID');
  var headers = {"Accept": "application/json"};

  try {
    var response = await http.get(
      url,
      headers: headers,
    );

    if (response.statusCode == 200) {
      var res = jsonDecode(response.body);
      var resData = res['mData'];

      print('Response body: $resData'); // Add this line to print the response body

      if (resData != null) {
        return User.fromJson(resData);
      } else {
        throw Exception('Unexpected json response (data was null).');
      }
    } else {
      throw Exception('Failed to retrieve user info (server returned ${response.statusCode})');
    }
  } catch (e) {
    // Handle exceptions, e.g., network issues
    throw Exception('Error fetching user info: $e');
  }
}


Future<void> sendCommentToServer(String comment, int postID) async {
  print("Current postID: $postID");

  var data = {
    'postID': postID, // parameter postID value
    'mUserId': "0", // Hardcoded mUserId value
    'mContent': comment, // message content
  };

  var url = Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/$postID/comments');
  var headers = {'Content-Type': 'application/json'};

  try {
    var response = await http.post(
      url,
      headers: headers,
      body: jsonEncode(data),
    );

    if (response.statusCode == 200) {
      var responseData = jsonDecode(response.body);
      // Handle the response data if necessary
      print('Data sent to server: $responseData');
      // You might not want to reload the entire window in Flutter, consider updating the UI.
      // window.location.reload();
    } else {
      throw Exception('Network response was not ok');
    }
  } catch (error) {
    // Handle errors
    print('Error sending data to server: $error');
  }
}

// NOT USING ANYMORE P2 CODE 
//Future<void> sendDownvote (int postID) async {
//   try {
//     // Set loading state if needed
//     // var setIsDownvoting = true;

//     var data = {
//       'mUserId': 0, // Hardcoded mUserId value
//       'mInteraction': -1, // Assuming -1 for downvote, adjust as needed
//     };

//     var response = await http.post(
//       Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/$postID/interactions'),
//       headers: {
//         'Content-Type': 'application/json',
//       },
//       body: jsonEncode(data),
//     );

//     if (response.statusCode == 200) {
//       // Handle the response data if necessary
//       var responseData = jsonDecode(response.body);
//       print('Data sent to server: $responseData');
//       // Perform additional actions if needed
//     } else {
//       // Use var for a more concise Exception message
//       throw Exception('Network response was not ok');
//     }
//   } catch (error) {
//     // Handle errors
//     print('Error sending data to server: $error');
//   } finally {
//     // After the downvote process is complete, you can perform additional actions if needed
//     // var setIsDownvoting = false;
//   }
// }

// NOT USING ANYMORE P2 CODE
//Future<void> sendUpvote (int postID) async {
//   try {
//     // Set loading state if needed

//     var data = {
//       'mUserId': 0, // Hardcoded mUserId value
//       'mInteraction': 1, // Assuming 1 for upvote, adjust as needed
//     };

//     var response = await http.post(
//       Uri.parse('https://team-pioneers.dokku.cse.lehigh.edu/ideas/$postID/interactions'),
//       headers: {
//         'Content-Type': 'application/json',
//       },
//       body: jsonEncode(data),
//     );

//     if (response.statusCode == 200) {
//       // Handle the response data if necessary
//       var responseData = jsonDecode(response.body);
//       print('Data sent to server: $responseData');
//       // Perform additional actions if needed
//     } else {
//       // Use var for a more concise Exception message
//       throw Exception('Network response was not ok');
//     }
//   } catch (error) {
//     // Handle errors
//     print('Error sending data to server: $error');
//   } finally {
//     // After the upvote process is complete, you can perform additional actions if needed
//   }
// }
