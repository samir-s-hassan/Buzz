import React, { useState } from "react";
import { Drawer, TextField, Button, Tooltip } from "@mui/material";
import "./AddComment.css";

const Comment = (postID) => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [message, setMessage] = useState("");

  const handleOpenDrawer = () => {
    setIsDrawerOpen(true);
  };

  const handleCloseDrawer = () => {
    setIsDrawerOpen(false);
    setMessage(""); // Reset the message to an empty string
  };

  const handleInputChange = (e) => {
    setMessage(e.target.value);
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submit button clicked"); // Add this line
    // Add your logic to submit the message
    sendCommentToServer(message, postID);
    // Close the drawer
    handleCloseDrawer();
  };

  const sendCommentToServer = (comment, { postID }) => {
    console.log("Current postID:", postID);
    const data = {
      postID: postID.postID, // parameter postID value // VERY IMPORTANT KEEP THE ORDER OF SENT CORRECT
      mUserId: 0, // Hardcoded mUserId value
      mContent: comment, // message content
    };

    fetch(
      `https://team-pioneers.dokku.cse.lehigh.edu/ideas/${postID}/comments`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        // Handle the response data if necessary
        console.log("Data sent to server:", data);
        window.location.reload();
      })
      .catch((error) => {
        console.error("Error sending data to server:", error);
      });
  };

  return (
    <div>
      <button onClick={handleOpenDrawer} className="custom-button">
        <Tooltip title="Create new comment">Create Comment</Tooltip>
      </button>
      <Drawer
        anchor="right"
        open={isDrawerOpen}
        onClose={handleCloseDrawer}
        classes={{ paper: "drawer-background" }}
      >
        <form className="drawer-content" onSubmit={handleSubmit}>
          <TextField
            label={`Enter your comment for post ${postID.postID}`}
            type="text"
            value={message}
            onChange={handleInputChange}
          />
          <div className="drawer-buttons">
            <Button type="submit" className="submit">
              Submit
            </Button>
            <Button onClick={handleCloseDrawer} className="cancel">
              Cancel
            </Button>
          </div>
        </form>
      </Drawer>
    </div>
  );
};

export default Comment;
