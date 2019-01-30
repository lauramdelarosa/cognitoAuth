package com.delarosa.cognitoAuth;

/**
 * Interface with the Activity.
 */
public interface OnFragmentInteractionListener {
    void onButtonPress(boolean signIn);
    void showPopup(String title, String content);
}