import 'package:flutter/material.dart';
import 'dart:io';
import 'package:flutter_app1/views/shared_widgets/newPostPage.dart';
import './net/webRequests.dart';
import 'dart:developer' as developer;
import 'package:flutter_app1/models/post.dart';
import 'package:flutter_app1/models/comment.dart';
import 'package:flutter_app1/models/user.dart';
import 'views/shared_widgets/loginPage.dart';
import 'views/shared_widgets/userProfilepage.dart';

void main() {
  HttpOverrides.global =
      MyHttpOverrides(); //https://stackoverflow.com/questions/54285172/how-to-solve-flutter-certificate-verify-failed-error-while-performing-a-post-req
  runApp(const MyApp());

  runApp(MaterialApp(
    home: LoginPage(), // Use the LoginPage widget
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue), //change?
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'The Buzz'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    Future<void> navigateToUserProfile(String userID) async {
      User user1 =
          await getUserInfo(userID); // Replace "userId" with the actual user ID
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => UserProfilePage(user: user1),
        ),
      );
    }
    // This method is rerun every time setState is called
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
        actions: [
          Ink(
            child: IconButton(
              onPressed: () {
                // Fetch user data (you might already have it at this point)
                navigateToUserProfile("erf225");
              },
              icon: const Icon(Icons.person), // Add the profile icon
            ),
          ),
          Ink(
            child: IconButton(
              onPressed: () {
                // Navigate to the page for creating a new idea
              },
              icon: const Icon(Icons.lightbulb_outline), // Idea icon
            ),
          ),
          Ink(
              child: IconButton(
                  onPressed: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) {
                      return const NewPostPage(title: 'Create new post');
                      //figure out how to update posts after posting
                      //probably need to change making the post list to its own file
                    }));
                  },
                  icon: const Icon(Icons.add)))
        ],
      ),
      body: const Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: PostList(),
      ),
    );
  }
}

class PostList extends StatefulWidget {
  const PostList({super.key});

  @override
  State<StatefulWidget> createState() => _PostListState();
}

class _PostListState extends State<PostList> {
  late Future<List<Post>> futurePosts;
  @override
  void initState() {
    super.initState();
    futurePosts = getWebData();
  }

  @override
  Widget build(BuildContext context) {
    late Future<List<Post>> futurePosts = getWebData();
    var fb = FutureBuilder<List<Post>>(
      future: futurePosts,
      builder: (BuildContext context, AsyncSnapshot<List<Post>> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          return const Center(child: Text('Error retrieving posts'));
        } else {
          Widget child = ListView.builder(
            padding: const EdgeInsets.all(16.0),
            itemCount: snapshot.data!.length,
            itemBuilder: ((context, index) {
              return Column(
                children: <Widget>[
                  ListTile(
                    title: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          snapshot.data![index].message,
                          style: const TextStyle(fontSize: 18.0),
                        ),
                        Text(
                          snapshot.data![index].postTime,
                          style: const TextStyle(fontSize: 14.0),
                        ),
                        Text(
                          'User ID: ${snapshot.data![index].userId}',
                          style: const TextStyle(fontSize: 14.0),
                        ),
                        Text(
                          'Post ID: ${snapshot.data![index].id}',
                          style: const TextStyle(fontSize: 14.0),
                        ),
                      ],
                    ),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        IconButton(
                          icon: const Icon(Icons.thumb_up),
                          iconSize: 20.0,
                          onPressed: () {
                            setState(() {
                              snapshot.data![index].likes++;
                              incrementLikes(snapshot.data![index]);
                            });
                          },
                        ),
                        Text(
                          '${snapshot.data![index].likes}',
                          style: const TextStyle(fontSize: 14.0),
                        ),
                        IconButton(
                          icon: const Icon(Icons.thumb_down),
                          iconSize: 20.0,
                          onPressed: () {
                            setState(() {
                              snapshot.data![index].likes++;
                              decrementLikes(snapshot.data![index]);
                            });
                          },
                        ),
                      ],
                    ),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisSize: MainAxisSize.min,
                          children: <Widget>[
                            IconButton(
                              icon: const Icon(Icons.comment),
                              iconSize: 20.0,
                              onPressed: () {
                                showComments(context, snapshot.data![index]);
                              },
                            ),
                            Text('View'),
                            IconButton(
                              icon: const Icon(Icons.add_comment),
                              iconSize: 20.0,
                              onPressed: () {
                                addComment(context, snapshot.data![index]);
                              },
                            ),
                            Text('Add'),
                          ],
                        ),
                        Row(
                          mainAxisSize: MainAxisSize.min,
                          children: <Widget>[
                            IconButton(
                              icon: const Icon(Icons.attach_file),
                              iconSize: 20.0,
                              onPressed: () {
                                // Handle add attachment action
                              },
                            ),
                            Text('File'),
                            IconButton(
                              icon: const Icon(Icons.link),
                              iconSize: 20.0,
                              onPressed: () {
                                // Handle attach link action
                              },
                            ),
                            Text('Link'),
                          ],
                        ),
                      ],
                    ),
                  ),
                  const Divider(height: 1.0)
                ],
              );
            }),
          );
          return child;
        }
      },
    );
    return fb;
  }
}

void showComments(BuildContext context, Post post) async {
  try {
    List<Comment> comments = await getCommentsForPost(post.id);

    print('Comments for Post ${post.id}: $comments');

    // Show comments in a new screen
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => CommentsScreen(post: post, comments: comments),
      ),
    );
  } catch (e) {
    // Handle errors, e.g., display an error message
    print('Error fetching comments: $e');
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Error fetching comments: $e'),
      ),
    );
  }
}

void addComment(BuildContext context, Post post) async {
  String comment = ""; // Initialize an empty string for the comment

  // Show a dialog with a text field for the user to input their comment
  await showDialog(
    context: context,
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text('Add Comment'),
        content: TextField(
          onChanged: (value) {
            // Update the comment variable as the user types
            comment = value;
          },
          decoration: InputDecoration(
            hintText: 'Type your comment here...',
          ),
        ),
        actions: <Widget>[
          ElevatedButton(
            onPressed: () {
              // Close the dialog when the user presses the "Cancel" button
              Navigator.of(context).pop();
            },
            child: Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () {
              // Close the dialog and add the comment when the user presses the "Submit" button
              Navigator.of(context).pop();
              // Call the function to send the comment to the server
              addCommentToServer(post, comment);
            },
            child: Text('Submit'),
          ),
        ],
      );
    },
  );
}

void addCommentToServer(Post post, String comment) async {
  // Handle the action to add a comment to the selected post
  // You can navigate to a new screen or show a bottom sheet with a comment input.
  developer.log('Add Comment to Post ${post.id}');

  try {
    // Call the function to send the comment to the server
    await sendCommentToServer(comment, post.id);
    // Optionally, you can update the UI to reflect the new comment
    // or perform any other necessary actions.
  } catch (error) {
    // Handle errors, if any, during the comment submission.
    developer.log('Error adding comment: $error');
  }
}

class CommentsScreen extends StatelessWidget {
  final Post post;
  final List<Comment> comments;

  CommentsScreen({required this.post, required this.comments});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Comments for Post ${post.id}'),
      ),
      body: ListView.builder(
        itemCount: comments.length,
        itemBuilder: (context, index) {
          return Card(
            margin: const EdgeInsets.all(8.0),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    comments[index].content,
                    style: TextStyle(fontSize: 18.0),
                  ),
                  SizedBox(height: 8.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'User ID: ${comments[index].userId}',
                        style: TextStyle(fontSize: 14.0),
                      ),
                      Text(
                        'Comment ID: ${comments[index].commentId}',
                        style: TextStyle(fontSize: 14.0),
                      ),
                    ],
                  ),
                  SizedBox(height: 8.0),
                  Text(
                    'Comment Time: ${comments[index].commentTime}',
                    style: TextStyle(fontSize: 14.0),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}

//https://stackoverflow.com/questions/54285172/how-to-solve-flutter-certificate-verify-failed-error-while-performing-a-post-req
class MyHttpOverrides extends HttpOverrides {
  @override
  HttpClient createHttpClient(SecurityContext? context) {
    return super.createHttpClient(context)
      ..badCertificateCallback =
          (X509Certificate cert, String host, int port) => true;
  }
}
