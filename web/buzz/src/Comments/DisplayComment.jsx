//inspired by ChatGPT
//initially, I was going to make the edit comment in another file but this works too

import React, { useState, useEffect } from "react";
import "./DisplayComment.css";
import { Button, Typography, TextField } from "@mui/material";

const DisplayComment = (postID) => {
  const [commentsData, setCommentData] = useState([]);
  const [showComments, setShowComments] = useState(false);
  const [editCommentId, setEditCommentId] = useState(null);
  const [editedContent, setEditedContent] = useState("");

  const fetchCommentsData = async () => {
    try {
      const response = await fetch(
        `https://team-pioneers.dokku.cse.lehigh.edu/ideas/${postID.postID}/comments`
      );
      const data = await response.json();
      if (data.mStatus === "ok") {
        setCommentData(data.mData);
      } else {
        console.error("Error in response:", data.mStatus);
      }
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  useEffect(() => {
    if (showComments) {
      fetchCommentsData();
    }
  }, [postID, showComments]);

  const handleEditComment = (commentId) => {
    setEditCommentId(commentId);
    // Assuming you want to initialize the text field with existing content
    const existingContent = commentsData.find(
      (comment) => comment.mCommentId === commentId
    )?.mContent;
    setEditedContent(existingContent || "");
  };

  const handleSaveComment = async () => {
    try {
      const response = await fetch(
        `https://team-pioneers.dokku.cse.lehigh.edu/ideas/${postID.postID}/comments/${editCommentId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ mContent: editedContent }),
        }
      );

      const data = await response.json();
      if (data.mStatus === "ok") {
        console.log("Comment updated:", data.mData);
        // Reset edit state
        setEditCommentId(null);
        setEditedContent("");
        // Fetch comments again to update the UI
        fetchCommentsData();
      } else {
        console.error("Error in response:", data.mStatus);
      }
    } catch (error) {
      console.error("Error updating comment", error);
    }
  };

  return (
    <div>
      <h3>Comments for Post</h3>
      <Typography>
        <Button onClick={() => setShowComments(!showComments)}>
          {showComments ? "Hide Comments" : "Show Comments"}
        </Button>
      </Typography>
      {showComments && (
        <>
          {commentsData.length > 0 ? (
            <ul>
              {commentsData.map((comment, index) => (
                <li key={index}>
                  <strong>Comment ID:</strong> {comment.mCommentId}{" "}
                  <strong>User ID:</strong> {comment.mUserId}{" "}
                  <strong>Post ID:</strong> {comment.mPostId}{" "}
                  {editCommentId === comment.mCommentId ? (
                    <>
                      <TextField
                        variant="outlined"
                        margin="normal"
                        fullWidth
                        multiline
                        rows={2}
                        id="editedContent"
                        label="Edit Comment"
                        name="editedContent"
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                      />
                      <Button variant="outlined" onClick={handleSaveComment}>
                        Save
                      </Button>
                    </>
                  ) : (
                    <>
                      <strong>Content:</strong> {comment.mContent}{" "}
                      <strong>Date:</strong> {comment.mDate}{" "}
                      <Button
                        variant="outlined"
                        onClick={() => handleEditComment(comment.mCommentId)}
                      >
                        Edit Comment
                      </Button>
                    </>
                  )}
                </li>
              ))}
            </ul>
          ) : (
            <p>No comments available for this post.</p>
          )}
        </>
      )}
    </div>
  );
};

export default DisplayComment;
