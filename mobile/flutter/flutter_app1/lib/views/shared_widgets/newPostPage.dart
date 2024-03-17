//instead of thinking of these as components, these are viewed webpages. things we see

import 'package:flutter/material.dart';
import 'package:flutter_app1/models/post.dart';
import 'package:flutter_app1/net/webRequests.dart';

class NewPostPage extends StatefulWidget {
  const NewPostPage({super.key, required String title});
  @override
  State<NewPostPage> createState() => _NewPostPageState();
}

/// The state of the new post page
class _NewPostPageState extends State<NewPostPage> {
  String inputText = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create new post'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center, // Center vertically
          children: [
            SizedBox(
              width: 300,
              child: TextField(
                decoration: const InputDecoration(
                  labelText: 'Type your message',
                  border: OutlineInputBorder(),
                ),
                onSubmitted: (text) {
                  setState(() {
                    inputText = text;
                    var newPost = Post(text, 0, "", "0");
                    postWebData(newPost);
                    Navigator.pop(context);
                  });
                },
              ),
            ),
            // Add icons for attaching a file and adding a link
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                IconButton(
                  icon: Icon(Icons.attach_file),
                  onPressed: () {
                    // Handle attaching a file action
                    // You can implement the logic to handle attaching a file here
                  },
                ),
                IconButton(
                  icon: Icon(Icons.link),
                  onPressed: () {
                    // Handle adding a link action
                    // You can implement the logic to handle adding a link here
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
