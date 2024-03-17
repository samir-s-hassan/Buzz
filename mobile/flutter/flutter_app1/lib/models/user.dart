import 'dart:developer' as developer;

import 'package:http/http.dart';
import 'package:flutter/material.dart';

class User {
  final String userId;
  final String username;
  final String email;
  final String genderIdentity;
  final String sexualOrientation;
  final String bio;

  User({
    required this.userId,
    required this.username,
    required this.email,
    required this.genderIdentity,
    required this.sexualOrientation,
    required this.bio,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['mUserId'],
      username: json['mUsername'],
      email: json['mEmail'],
      genderIdentity: json['mGenderIdentity'],
      sexualOrientation: json['mSexualOrientation'],
      bio: json['mBio'],
    );
  }

  @override
  String toString() {
    return 'User(userId: $userId, username: $username, email: $email, '
        'genderIdentity: $genderIdentity, sexualOrientation: $sexualOrientation, '
        'bio: $bio)';
  }
}
