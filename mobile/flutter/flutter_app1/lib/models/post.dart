import 'dart:developer' as developer;

class Post {
  // Fields
  String message = "";
  int likes = 0;
  String postTime = ""; // Change the type to String
  int id = 0;
  String userId = ""; // New field for user ID
  String attachmentUrl = "";
  bool isValid = false;

  // Constructor for Post
  Post(String text, int likeCount, String timeOfPost, String userId) {
    message = text;
    likes = likeCount;
    postTime = timeOfPost;
    this.userId = userId;
  }

  // Factory constructor for Post
  Post.forFactory(String text, int likeCount, String dateString, String userId) {
    message = text;
    likes = likeCount;
    postTime = dateString;
    // postTime = dateStringToDataTime(dateString);
    // developer.log('postTime: $postTime');
    this.userId = userId;
  }

  // Factory constructor for Post from JSON
  factory Post.fromJson(Map<String, dynamic> json) {
    var newPost = Post.forFactory(
      json['mIdea'] as String,
      json['mLikes'] as int,
      json['mCreated'] as String,
      json['mUserId'] as String,
    );
    newPost.id = json['mId'] as int;
    newPost.attachmentUrl = json['mAttachmentURL'] as String? ?? ''; // Handle null for attachmentUrl
    newPost.isValid = json['mValid'] as bool;
    return newPost;
  }


  /// converts backend date format to dart datetime
  /// may not be needed
  /// @param dateString the date as a string
  /// @return a DateTime object
  dateStringToDataTime(String dateString){
    String month = dateString.substring(0,3);
    String day = dateString.substring(4,6);
    String year = dateString.substring(8,12);
    String time = dateString.substring(13,21);
    String ampm = dateString.substring(22,23);
    int hour = int.parse(time.substring(0,1));
    if(ampm == "PM"){
      hour += 12;
    }
    String minute = time.substring(3,5);
    String second = time.substring(6,8);
    int monthAsInt = 0;
    if (month == "Jan"){
      monthAsInt = 01;
    }
    else if (month == "Feb"){
      monthAsInt = 02;
    }
    else if (month == "Mar"){
      monthAsInt = 03;
    }
    else if (month == "Apr"){
      monthAsInt = 04;
    }
    else if (month == "May"){
      monthAsInt = 05;
    }
    else if (month == "Jun"){
      monthAsInt = 06;
    }
    else if (month == "Jul"){
      monthAsInt = 07;
    }
    else if (month == "Aug"){
      monthAsInt = 08;
    }
    else if (month == "Sep"){
      monthAsInt = 09;
    }
    else if (month == "Oct"){
      monthAsInt = 10;
    }
    else if (month == "Nov"){
      monthAsInt = 11;
    }
    else if (month == "Dec"){
      monthAsInt = 12;
    }
    DateTime timeAsDateTime = DateTime(int.parse(year), monthAsInt, int.parse(day), hour, int.parse(minute), int.parse(second));
    return timeAsDateTime;
  }

}