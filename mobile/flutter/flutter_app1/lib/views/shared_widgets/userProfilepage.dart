import 'package:flutter/material.dart';
import 'package:flutter_app1/models/user.dart';
import 'package:flutter_app1/net/webRequests.dart';
 

class UserProfilePage extends StatefulWidget {
  final User user;

  UserProfilePage({required this.user});

  @override
  _UserProfilePageState createState() => _UserProfilePageState();
}

class _UserProfilePageState extends State<UserProfilePage> {
  late TextEditingController usernameController;
  late TextEditingController emailController;
  late TextEditingController genderIdentityController;
  late TextEditingController sexualOrientationController;
  late TextEditingController bioController;

  @override
  void initState() {
    super.initState();
    // Initialize controllers with user data
    usernameController = TextEditingController(text: widget.user.username);
    emailController = TextEditingController(text: widget.user.email);
    genderIdentityController =
        TextEditingController(text: widget.user.genderIdentity);
    sexualOrientationController =
        TextEditingController(text: widget.user.sexualOrientation);
    bioController = TextEditingController(text: widget.user.bio);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('User Profile'),
      ),
      body: ListView(
        padding: EdgeInsets.all(16.0),
        children: [
          buildEditableField('Username', usernameController),
          buildEditableField('Email', emailController),
          buildEditableField('Gender Identity', genderIdentityController),
          buildEditableField('Sexual Orientation', sexualOrientationController),
          buildEditableField('Bio', bioController),
          ElevatedButton(
            onPressed: () {
              // Save changes or update user profile logic
              saveChanges();
            },
            child: Text('Save Changes'),
          ),
        ],
      ),
    );
  }

  Widget buildEditableField(String label, TextEditingController controller) {
    return ListTile(
      title: Text(label),
      subtitle: TextField(
        controller: controller,
        decoration: InputDecoration(
          border: OutlineInputBorder(),
        ),
      ),
    );
  }

  void saveChanges() {
    // Implement logic to save changes to user profile
    // Access the edited values using controllers:
    final newUsername = usernameController.text;
    final newEmail = emailController.text;
    final newGenderIdentity = genderIdentityController.text;
    final newSexualOrientation = sexualOrientationController.text;
    final newBio = bioController.text;

    // Perform the necessary operations to update the user profile
    // (e.g., make a network request to update the user data)

    // You may want to show a success message or handle errors here
  }

  @override
  void dispose() {
    // Dispose of controllers to prevent memory leaks
    usernameController.dispose();
    emailController.dispose();
    genderIdentityController.dispose();
    sexualOrientationController.dispose();
    bioController.dispose();
    super.dispose();
  }
}
