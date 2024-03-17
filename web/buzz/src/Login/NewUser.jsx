import React, { useState } from "react";
import { useNavigate } from "react-router-dom";


import {
  Button,
  TextField,
  Container,
  Typography,
  CssBaseline,
  Box,
} from "@mui/material";

function NewUser({ userName, userEmail }) {
  const navigate = useNavigate()
  const [userData, setUserData] = useState({
    userName: userName,
    userEmail: userEmail,
    userGenderIdentity: "",
    userSexualOrientation: "",
    userNote: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    console.log(`Updating ${name} with value: ${value}`);
    setUserData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const sendUserDataToServer = () => {
    const data = {
        mUsername: userData.userName,
        mEmail: userData.userEmail,
        mGenderIdentity: userData.userGenderIdentity,
        mSexualOrientation: userData.userSexualOrientation,
        mBio: userData.userNote
    };
  
    console.log('Sending data to server:', data);
  
    fetch("https://team-pioneers.dokku.cse.lehigh.edu/login/create-new-user", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Data sent to server:", data);
        navigate("/browseideaspage")
       // Handle the response data if necessary
        // Optionally, you can update the UI or perform additional actions
      })
      .catch((error) => {
        console.error("Error sending data to server:", error);
      });
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box>
        <Typography
          component="h1"
          variant="h5"
          sx={{ textAlign: "center", width: "100%", marginBottom: 2 }}
        >
          Signup Form
        </Typography>
        <Box component="form" noValidate sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            fullWidth
            id="userName"
            label="User Name"
            name="userName"
            value={userData.userName}
            onChange={handleInputChange}
            InputProps={{
              readOnly: true,
            }}
          />
          <TextField
            margin="normal"
            fullWidth
            id="userEmail"
            label="Email Address"
            name="userEmail"
            value={userData.userEmail}
            onChange={handleInputChange}
            InputProps={{
              readOnly: true,
            }}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="userGenderIdentity"
            label="Gender Identity"
            name="userGenderIdentity"
            value={userData.userGenderIdentity}
            onChange={handleInputChange}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="userSexualOrientation"
            label="Sexual Orientation"
            name="userSexualOrientation"
            value={userData.userSexualOrientation}
            onChange={handleInputChange}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            multiline
            rows={4}
            id="userNote"
            label="Bio"
            name="userNote"
            value={userData.userNote}
            onChange={handleInputChange}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={sendUserDataToServer}
          >
            Save Profile
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default NewUser;
