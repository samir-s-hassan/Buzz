// NavigationBar.js

import React from "react";
import { Link } from "react-router-dom";
import { CssBaseline, Typography } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Tooltip from "@mui/material/Tooltip";
import Post from "../Post/AddPost";
import "./NavigationBar.css"; // Import the CSS file

const NavigationBar = () => {
  return (
    <>
      <CssBaseline />
      <AppBar position="static" className="app-bar">
        <Toolbar
          className="nav-links"
          style={{ justifyContent: "space-between" }}
        >
          <div>
            <Tooltip title="My profile">
              <IconButton
                color="inherit"
                component={Link}
                to="/myownprofilepage"
              >
                View/edit profile
              </IconButton>
            </Tooltip>
          </div>

          <Typography variant="h4" component="div" className="title">
            THE BUZZ
          </Typography>

          <div>
            <Tooltip title="Browse ideas">
              <IconButton
                color="inherit"
                component={Link}
                to="/browseideaspage"
              >
                Browse ideas
              </IconButton>
            </Tooltip>
          </div>

          <div className="search-section">
            <Tooltip title="Create new post">
              <IconButton color="inherit">
                <Post />
              </IconButton>
            </Tooltip>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default NavigationBar;
