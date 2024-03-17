import React, { useState } from "react";
import { Drawer, TextField, Button } from "@mui/material";
import "./AddPost.css";

const AddPost = () => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [message, setMessage] = useState("");
  const [link, setLink] = useState("");

  const handleOpenDrawer = () => {
    setIsDrawerOpen(true);
  };

  const [file, setFile] = useState();
  function handleFile(e){
    setFile(e.target.files[0]);
  }
  const handleCloseDrawer = () => {
    setIsDrawerOpen(false);
    setLink('');
    setMessage(''); // Reset the message to an empty string
  };

  //https://stackoverflow.com/questions/36280818/how-to-convert-file-to-base64-in-javascript
  function getBase64(file) {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
      console.log(reader.result);
    };
    reader.onerror = function (error) {
      console.log('Error: ', error);
    };
 }

  const handleInputChange = (e) => {
    setMessage(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submit button clicked"); // Add this line
    // Add your logic to submit the message
    sendPostToServer(message);
    // Close the drawer
    handleCloseDrawer();
  };

  const sendPostToServer = (message) => {
    let data;
    if (file != null){
    console.log(file.data)
    let base64 = getBase64(file);
    data = {
      mUserId: 0, // Hardcoded mUserId value
      mIdea: message, // Assuming the message corresponds to mIdea
      mLikes: 0, // Hardcoded mLikes value
      mFile: base64, // Hardcoded mFile value
    };
    }
    else{
      data = {
        mUserId: 0, // Hardcoded mUserId value
        mIdea: message, // Assuming the message corresponds to mIdea
        mLikes: 0, // Hardcoded mLikes value
      };
    } 

    fetch("https://team-pioneers.dokku.cse.lehigh.edu/ideas", {
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
        Create Post
      </button>
      <Drawer
        anchor="right"
        open={isDrawerOpen}
        onClose={handleCloseDrawer}
        classes={{ paper: "drawer-background" }}
      >
        <form className="drawer-content" onSubmit={handleSubmit}>
          <TextField
            label="Enter your post"
            type="text"
            value={message}
            onChange={handleInputChange}
          />
          <TextField
            label="Add a link?"
            type="text"
            value={link}
            onChange={handleInputChange}
          />
          <input type="file" onChange = {handleFile}/>
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

export default AddPost;
