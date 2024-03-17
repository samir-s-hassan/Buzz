// Import necessary modules from React and Material-UI
import React, { useState, useEffect } from "react";
import Button from "@mui/material/Button";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import "./ScrollUp.css"; // Import your CSS file for styling
/*
  This component renders a button that, when clicked, scrolls the page to the top.
  It becomes visible when the user scrolls down 50 pixels.
  It uses Material-UI components for the button and icon.
*/

const ScrollUp = () => {
  // State to track the visibility of the scroll-to-top button
  const [isVisible, setIsVisible] = useState(false);

  // Effect to handle scrolling and update button visibility
  useEffect(() => {
    // Show the button when the user scrolls down 50 pixels
    const handleScroll = () => {
      if (window.scrollY > 50) {
        setIsVisible(true);
      } else {
        setIsVisible(false);
      }
    };

    // Add scroll event listener when the component mounts
    window.addEventListener("scroll", handleScroll);

    // Cleanup the event listener when the component unmounts
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []); // Empty dependency array ensures the effect runs only on mount and unmount

  // Function to scroll to the top of the page with a smooth scrolling effect
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth", // Optional: Add smooth scrolling effect
    });
  };

  // Render the scroll-to-top button
  return (
    <div className={`scroll-to-top-button ${isVisible ? "visible" : ""}`}>
      {/* Material-UI Button with an icon for scrolling to the top */}
      <Button
        onClick={scrollToTop}
        variant="contained"
        color="primary"f
        endIcon={<KeyboardArrowUpIcon />}
      >
        Back to top
      </Button>
    </div>
  );
};

// Export the ScrollUp component as the default export
export default ScrollUp;
