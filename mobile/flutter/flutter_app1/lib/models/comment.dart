class Comment {
  // Fields
  final int commentId;
  final String userId;
  final int postId;
  final String content;
  final String commentTime; // Change the type to String

  // Constructor for Comment
  Comment({
    required this.commentId,
    required this.userId,
    required this.postId,
    required this.content,
    required this.commentTime,
  });

  // Factory constructor for Comment from JSON
  factory Comment.fromJson(Map<String, dynamic> json) {
    try {
      return Comment(
        commentId: json['mCommentId'] as int? ?? 0,
        userId: json['mUserId'] as String? ?? '',
        postId: json['mPostId'] as int? ?? 0,
        content: json['mContent'] as String? ?? '',
        commentTime: json['mDate'] as String? ?? '', // Keep it as a String
      );
    } catch (e) {
      // Handle parsing errors or other issues if needed
      print('Error parsing Comment from JSON: $e');
      throw Exception('Error parsing Comment from JSON: $e');
    }
  }
}
