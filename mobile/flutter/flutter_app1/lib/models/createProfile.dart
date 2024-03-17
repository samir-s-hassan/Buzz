//these are models of what we want so essentially components?

import 'package:flutter/material.dart';
import 'package:flutter_app1/main.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app1/views/shared_widgets/newPostPage.dart';
import 'package:flutter_app1/net/webRequests.dart';


class CreateProfile extends StatelessWidget {
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _orientationController = TextEditingController();
  final TextEditingController _genderIdentityController = TextEditingController();
  final TextEditingController _bioController = TextEditingController();
    final TextEditingController _idController = TextEditingController();



   //Fields
  String mUsername = "";
  String mEmail = "";
  String mGenderIdentity = "";
  String mSexualOrientation = ""; 
  String mBio = "";
  int mId = 0;
  
  CreateProfile(String username, String email, String genderIdentity, String sexualOrientation, String bio) {
        mUsername = username;
        mEmail = email;
        mGenderIdentity = genderIdentity;
        mSexualOrientation = sexualOrientation;
        mBio = bio;
  }

  CreateProfile.forFactory(String username, String email, String genderIdentity, String sexualOrientation, String bio){
        mUsername = username;
        mEmail = email;
        mGenderIdentity = genderIdentity;
        mSexualOrientation = sexualOrientation;
        mBio = bio;
  }

  /// factory constructor for Post from JSON
  /// @param json the json object to be converted to a Post
  /// @return a Post object
  factory CreateProfile.fromJson(Map<String, dynamic> json) {
    var newProfile = CreateProfile.forFactory(
      json['mUsername'] as String,
      json['mEmail'] as String,
      json['mOrientation'] as String,
      json['mGender'] as String,
      json['mbio'] as String,
      //json['mId'] as int,
    );
    newProfile.mId = json['mId'] as int;
    return newProfile;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Create Account'),
        leading: IconButton(
          icon: Icon(Icons.arrow_back), // Add a back arrow icon
          onPressed: () {
            Navigator.pop(context); // Navigate back to the previous screen (Login page)
          },
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            TextField(
              controller: _nameController,
              decoration: InputDecoration(labelText: 'Name'),
            ),
            TextField(
              controller: _emailController,
              decoration: InputDecoration(labelText: 'Email'),
            ),
            TextField(
              controller: _orientationController,
              decoration: InputDecoration(labelText: 'Sexual Orientation'),
            ),
            TextField(
              controller: _genderIdentityController,
              decoration: InputDecoration(labelText: 'Gender Identity'),
            ),
            TextField(
              controller: _bioController,
              decoration: InputDecoration(labelText: 'Bio'),
            ),
            ElevatedButton(
              onPressed: () {
                // Implement user registration logic here
                final name = _nameController.text;
                final email = _emailController.text;
                final orientation = _orientationController.text;
                final genderIdentity = _genderIdentityController.text;
                final bio = _bioController.text; 
                Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) {
                  return const MyApp(); 
                }));
                // Validate and process user registration data
                // have to add way to add to backend?
              },
              child: Text('Register'), 
            ),
          ],
        ),
      ),
    );
    
  }


  
// Function to send profile data to the backend
  void _sendProfileData() async {
    //input
    String name = _nameController.text;
    String email = _emailController.text;
    String orientation = _orientationController.text; 
    String genderIdentity = _genderIdentityController.text;
    String bio = _bioController.text;  

    CreateProfile userProfile = CreateProfile(name, email, orientation, genderIdentity, bio);
    await postProfileData(userProfile);
  }
}