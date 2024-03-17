// import logo from "./logo.svg";
// source code: https://github.com/omarmoreta/Google-Identity-Services/tree/main
// source video: https://www.youtube.com/watch?v=roxC8SMs7HU
// source code is from source video which was posted on coursesite
import { jwtDecode } from "jwt-decode";
import { useEffect, useState } from "react";
import NavigationBar from "../NavigationBar/NavigationBar";
import { Button } from "@mui/material";
import "./LoginPage.css";
import NewUser from "./NewUser";
import IconButton from "@mui/material/IconButton";
import { Link } from "react-router-dom";

function LoginPage() {
  const [user, setUser] = useState({});
  const [exists, setExists] = useState(false);
  const [createNewUser, setCreateNewUser] = useState(false);

  function handleCallbackResponse(response) {
    console.log("Encoded JWT ID token: " + response.credential);
    let userObject = jwtDecode(response.credential);
    console.log(userObject);
    setUser(userObject);
    document.getElementById("signInDiv").hidden = true;
    sendCredentialToServer(response.credential);
  }

  function sendCredentialToServer(credential) {
    const data = {
      idTokenString: credential,
    };

    fetch(
      "https://team-pioneers.dokku.cse.lehigh.edu/login/auth?credential=" +
      credential,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "credential": JSON.stringify(data),
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
        console.log("Credential sent to server:", data);
        if (data.mStatus === "ok") {
          console.log("User exists");
          setExists(true);
        }
        else{
          setCreateNewUser(true);
        }
        // Handle the response data if necessary
      })
      .catch((error) => {
        console.error("Error sending credential to server:", error);
      });
  }

  function handleSignOut(event) {
    setUser({});
    document.getElementById("signInDiv").hidden = false;
  }

  useEffect(() => {
    /* global google */
    google.accounts.id.initialize({
      //PLEASE MAKE AN ENV VARIABLE FOR CLIENT ID
      client_id:
        "64782561268-bm9vevggd5nq6ocsakie7ac9p5jh28vo.apps.googleusercontent.com", // "*********PLACE YOUR ********* GOOGLE CLIENT ID********* HERE*********",
      callback: handleCallbackResponse,
    });
    google.accounts.id.renderButton(document.getElementById("signInDiv"), {
      theme: "outline",
      size: "large",
    });
    google.accounts.id.prompt();
  }, []);
  // if we have no user: sign in button
  // if we have a user: show the log out button
  // Make an accesss the buzz button on this page
  // allow people to visit profile of each user
  return (
    <div className="App">
      {exists && <NavigationBar />}
      <div id="signInDiv"></div>
      <br />
      {exists && (
        <div className="flex-container" flex-direction ="row" align-items ="center">
          <div>
            <IconButton to="/browseideaspage" component={Link}>
              Browse the Buzz
            </IconButton>
          </div>
          <div><Button
            variant="contained"
            color="secondary"
            onClick={(e) => handleSignOut(e)}
          >
            Sign Out
          </Button>
          </div>
        </div>
      )}
      {!exists && createNewUser &&(
        <div>
          <br />
          <img src={user.picture} alt=""></img>
          <br />
          <h3>{user.name}</h3>
          <NewUser userName={user.name} userEmail={user.email} />
        </div>
      )}
    </div>
  );
}

export default LoginPage;
