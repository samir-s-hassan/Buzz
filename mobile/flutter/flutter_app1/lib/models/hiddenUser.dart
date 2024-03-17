import 'dart:developer' as developer;

import 'package:http/http.dart';
import 'package:flutter/material.dart';

//this class is for viewing other users whom you should not be able to see the information of Gender and Sexual Orientation of

class hiddenUser {
  final String userId;
  final String username;
  final String email;
  final String bio;

  hiddenUser({
    required this.userId,
    required this.username,
    required this.email,
    required this.bio,
  });

  factory hiddenUser.fromJson(Map<String, dynamic> json) {
    return hiddenUser(
      userId: json['mUserId'],
      username: json['mUsername'],
      email: json['mEmail'],
      bio: json['mBio'],
    );
  }

  @override
  String toString() {
    return 'hiddenUser(userId: $userId, username: $username, email: $email,  '
        'bio: $bio)';
  }
}
