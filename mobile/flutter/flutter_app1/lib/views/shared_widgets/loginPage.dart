//instead of thinking of these as components, these are viewed webpages. things we see

import 'package:flutter/material.dart';
import 'package:flutter_app1/main.dart';
import 'package:flutter_app1/models/createProfile.dart';
import 'package:flutter_app1/views/shared_widgets/newPostPage.dart';

class LoginPage extends StatelessWidget {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _orientationController = TextEditingController();
  final TextEditingController _genderController = TextEditingController();
  final TextEditingController _bioController= TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Login'),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          TextField(
            controller: _usernameController,
            decoration: InputDecoration(labelText: 'Username'),
          ),
          TextField(
            controller: _passwordController,
            decoration: InputDecoration(labelText: 'Password'),
            obscureText: true, // Hide the entered text for the password
          ),
          ElevatedButton(
            onPressed: () {
              final username = _usernameController.text;
              final password = _passwordController.text;

              if (username == 'test' && password == 'test') {
                // Successful login, navigate to the next screen
                Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) {
                  return const MyApp(); 
                }));
              } else {
                // Invalid login, show an error message
                showDialog(
                  context: context,
                  builder: (context) {
                    return AlertDialog(
                      title: Text('Login Error'),
                      content: Text('Invalid username or password.'),
                      actions: <Widget>[
                        TextButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                          child: Text('OK'),
                        ),
                      ],
                    );
                  },
                );
              }
            },
            child: Text('Login'),
          ),
          TextButton(
            onPressed: () {
              // Navigate to the registration or create account page for new users
              final username = _usernameController.text;
              final email = _emailController.text;
              final orientation = _orientationController.text;
              final gender = _genderController.text;
              final bio = _bioController.text; 

              Navigator.push(context, MaterialPageRoute(builder: (context) {
                return CreateProfile(username, email, orientation, gender, bio);
              }));
            },
            child: Text('Create Account'),
          ),
        ],
      ),
    );
  }
}