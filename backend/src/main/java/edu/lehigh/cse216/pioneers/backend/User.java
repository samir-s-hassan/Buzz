package edu.lehigh.cse216.pioneers.backend;

public class User {
    public int mUserId;
    public String mUsername;
    public String mEmail;
    public String mGenderIdentity;
    public String mSexualOrientation;
    public String mBio;
    public boolean mValid;

    /**
     * Construct a User object by providing values for its fields
     */
    User(int userId, String username, String email, String genderIdentity, String sexualOrientation, String bio, boolean valid) {
        mUserId = userId;
        mUsername = username;
        mEmail = email;
        mGenderIdentity = genderIdentity;
        mSexualOrientation = sexualOrientation;
        mBio = bio;
        mValid = valid;
    }
    /**
     * Construct a user by copying another users data into this one
     */
    User(User oldUser){
        mUserId = oldUser.mUserId;
        mUsername = oldUser.mUsername;
        mEmail = oldUser.mEmail;
        mGenderIdentity = oldUser.mGenderIdentity;
        mSexualOrientation = oldUser.mSexualOrientation;
        mBio = oldUser.mBio;
        mValid = oldUser.mValid;
    }
}