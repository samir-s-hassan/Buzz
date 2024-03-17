import React, { useState } from "react";
import Button from "@mui/material/Button";

const Downvote = ({ postID }) => {
  const [isDownvoting, setIsDownvoting] = useState(false);

  const sendDownvoteToServer = () => {
    setIsDownvoting(true);

    const data = {
      mUserId: 0, // Hardcoded mUserId value
      mInteraction: -1, // Assuming -1 for downvote, adjust as needed
    };

    fetch(
      `https://team-pioneers.dokku.cse.lehigh.edu/ideas/${postID}/interactions`,
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
      })
      .finally(() => {
        // After the downvote process is complete, you can perform additional actions if needed
        setIsDownvoting(false);
      });
  };

  return (
    <div>
      <Button
        
        variant="contained"
        color="primary"
        onClick={sendDownvoteToServer}
        disabled={isDownvoting}
      >
        {isDownvoting ? "Downvoting..." : "Downvote"}
      </Button>
    </div>
  );
};

export default Downvote;
