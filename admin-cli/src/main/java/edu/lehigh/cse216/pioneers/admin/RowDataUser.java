package edu.lehigh.cse216.pioneers.admin;

public class RowDataUser {

    int mUserid;
    String mUsername;
    String mEmail;
    String mGenderidentity;
    String mSexualorientation;
    String mBio;
    String mValid;

    public RowDataUser(int userId, String username, String email, String genderIdentity, String sexualOrientation, String bio, String valid){
        mUserid = userId;
        mUsername = username;
        mEmail = email;
        mGenderidentity = genderIdentity;
        mSexualorientation = sexualOrientation;
        mBio = bio;
        mValid = valid;
    }



    
}
