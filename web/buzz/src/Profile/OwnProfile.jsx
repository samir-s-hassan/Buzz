import React, { useState, useEffect } from "react";
import {
  Button,
  TextField,
  Container,
  Typography,
  CssBaseline,
  Box,
} from "@mui/material";
import NavigationBar from "../NavigationBar/NavigationBar";

function OwnProfile() {
  const [userData, setUserData] = useState({
    mUsername: "",
    mEmail: "",
    mGenderIdentity: "",
    mSexualOrientation: "",
    mBio: "",
  });
  const [dataFetched, setDataFetched] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // Handle submission logic (e.g., send data to the server or update state)
    console.log("User data submitted:", userData);
  };

  const fetchUserData = async () => {
    try {
      const response = await fetch(
        "https://team-pioneers.dokku.cse.lehigh.edu/users/0"
      );
      const data = await response.json();
      if (data.mStatus === "ok") {
        setUserData(data.mData);
        setDataFetched(true);
      } else {
        console.error("Error in response:", data.mStatus);
      }
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  useEffect(() => {
    if (!dataFetched) {
      fetchUserData();
    }
  }, [dataFetched]);

  return (
    <div>
      <NavigationBar />
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Typography
            component="h1"
            variant="h4"
            sx={{ textAlign: "center", width: "100%", marginBottom: 2 }}
          >
            My User Profile
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
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
              id="mGenderIdentity"
              label="Gender Identity"
              name="mGenderIdentity"
              value={userData.mGenderIdentity}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="mSexualOrientation"
              label="Sexual Orientation"
              name="mSexualOrientation"
              value={userData.mSexualOrientation}
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
            />
            <Button
              type="submit"
              required
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Save Changes
            </Button>
          </Box>
        </Box>
      </Container>
    </div>
  );
}

export default OwnProfile;
