import React, { useState } from "react";
import {
  Button,
  TextField,
  Container,
  CssBaseline,
  Box,
} from "@mui/material";

function OtherProfile(userID) {
  const paramID = userID.userID;
  console.log(paramID);
  const [userData, setUserData] = useState({
    mUsername: "",
    mEmail: "",
    mGenderIdentity: "",
    mSexualOrientation: "",
    mBio: "",
  });

  const [showProfile, setShowProfile] = useState(false);
  const [dataFetched, setDataFetched] = useState(false);

  const toggleProfile = () => {
    setShowProfile(prevShowProfile => {
      // If transitioning from false to true and data hasn't been fetched, fetch data
      if (!prevShowProfile && !dataFetched) {
        fetchUserData();
      }
      return !prevShowProfile;
    });
  };

  const fetchUserData = async () => {
    try {
      const response = await fetch(
        `https://team-pioneers.dokku.cse.lehigh.edu/users/${paramID}`
      );
      const data = await response.json();
      if (data.mStatus === "ok") {
        setUserData(data.mData);
        console.log(data.mData);
        setDataFetched(true);
      } else {
        console.error("Error in response:", data.mStatus);
      }
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box>
        <Button variant="contained" color="primary" onClick={toggleProfile}>
          {showProfile
            ? `Hide user ${paramID} Profile`
            : `Show user ${paramID} profile`}
        </Button>
        {showProfile && (
          <Box component="form" noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="mUsername"
              label="User Name"
              name="mUsername"
              value={userData.mUsername}
              InputProps={{
                readOnly: true,
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="mEmail"
              label="Email Address"
              name="mEmail"
              value={userData.mEmail}
              InputProps={{
                readOnly: true,
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              multiline
              rows={4}
              id="mBio"
              label="Bio"
              name="mBio"
              value={userData.mBio}
              InputProps={{
                readOnly: true,
              }}
            />
          </Box>
        )}
      </Box>
    </Container>
  );
}

export default OtherProfile;
