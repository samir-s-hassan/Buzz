import React, { useState } from 'react';
import Button from '@mui/material/Button';

const Upvote = ({ postID }) => {
  const [isUpvoting, setIsUpvoting] = useState(false);

  const sendUpvoteToServer = () => {
    setIsUpvoting(true);

    const data = {
      mUserId: 0, // Hardcoded mUserId value
      mInteraction: 1, // Assuming 1 for upvote, adjust as needed
    };

    fetch(`https://team-pioneers.dokku.cse.lehigh.edu/ideas/${postID}/interactions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        // Handle the response data if necessary
        console.log('Data sent to server:', data);
        window.location.reload();
      })
      .catch(error => {
        console.error('Error sending data to server:', error);
      })
      .finally(() => {
        // After the upvote process is complete, you can perform additional actions if needed
        setIsUpvoting(false);
      });
  };

  return (
    <div>
      <Button variant="contained" color="primary" onClick={sendUpvoteToServer} disabled={isUpvoting}>
        {isUpvoting ? 'Upvoting...' : 'Upvote'}
      </Button>
    </div>
  );
};

export default Upvote;

//add console.logs to verify if voting works