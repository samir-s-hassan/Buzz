import React, { useEffect, useState } from "react";
import NavigationBar from "../NavigationBar/NavigationBar";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import "./IdeasPage.css"; // Import the CSS file
import Comment from "../Comments/AddComment";
import DisplayComment from "../Comments/DisplayComment";
import Upvote from "../Votes/Upvote";
import Downvote from "../Votes/Downvote";
import OtherProfile from "../Profile/OtherProfile";
import Button from '@mui/material/Button';


const IdeasPage = () => {
  const [ideasData, setIdeasData] = useState([]);
  const [attachment, setAttachment] = useState(false);

  useEffect(() => {
    fetchIdeasData();
  }, []);
  function downloadAttachment(attachment) {
    const decoded = atob(attachment);
    console.log(decoded);
    return decoded.download;

  }

  const fetchIdeasData = async () => {
    try {
      const response = await fetch(
        "https://team-pioneers.dokku.cse.lehigh.edu/ideas"
      );
      const data = await response.json();
      if (data.mStatus === "ok") {
        console.log("Data:", data.mData);
        setIdeasData(data.mData);
      } else {
        console.error("Error in response:", data.mStatus);
      }
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };
  return (
    <div>
      <NavigationBar />
      <div className="ideas-container">
        <h2>Ideas Page</h2>
        <div>
          {ideasData.map((idea, index) => (
            <Card key={index} className="idea-card">
              <CardContent>
                <Typography variant="h6">
                  <strong font-size="20px">Idea:</strong> {idea.mIdea} <br />
                  <strong> Date Created:</strong> {idea.mCreated} <br />
                  <strong>Likes:</strong> {idea.mLikes}
                </Typography>
                <div className="actions-section">
                  {/* Upvote and Downvote buttons */}
                  <IconButton>
                    <Upvote postID={idea.mId} />
                  </IconButton>
                  <IconButton>
                    <Downvote postID={idea.mId} />
                  </IconButton>
                  {idea.mAttachmentURL && (
                    <IconButton id="attachment button" >
                      <Button variant="contained" color="primary" onClick={downloadAttachment(idea.mAttachmentURL)}>
                        <div>
                          <strong>Download Attachment</strong>
                        </div>
                      </Button>
                    </IconButton>
                  )}
                  <IconButton className="user-profile-button">
                    <OtherProfile userID={idea.mUserId} />
                  </IconButton>
                </div>
                {idea.mLink && (<strong> Link: {idea.mLink}
                </strong>)}
                <div className="comment-section">
                  <Comment postID={idea.mId} />
                  <DisplayComment postID={idea.mId} />
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
};

export default IdeasPage;
