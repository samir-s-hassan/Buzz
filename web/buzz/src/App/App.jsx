// Import the necessary modules from React and React Router
import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

// Import the components used in the routes
import LoginPage from "../Login/LoginPage"; // Import the LoginPage component
import IdeasPage from "../Ideas/IdeasPage";
import NewUser from "../Login/NewUser";
import OwnProfile from "../Profile/OwnProfile";
import ScrollUp from "./ScrollUp";

// The main App component
function App() {
  return (
    <div>
      {/* Set up React Router with BrowserRouter */}
      <BrowserRouter>
        {/* Define the routes using Routes and Route components */}
        <Routes>
          {/* Route for the home page, using LoginPage component */}
          <Route path="/" element={<LoginPage />} />
          {/* Route for the new user using new user component component */}
          <Route path="/newuser" element={<NewUser />} />
          {/* Route for the browse ideas page, using IdeasPage component */}
          <Route path="/browseideaspage" element={<IdeasPage />} />
          {/* Route for the own profile page, using OwnProfile component */}
          <Route path="/myownprofilepage" element={<OwnProfile />} />
        </Routes>
      </BrowserRouter>
      
      {/* Include the ScrollUp component outside of the Routes */}
      <ScrollUp />
    </div>
  );
}

// Export the App component as the default export
export default App;
