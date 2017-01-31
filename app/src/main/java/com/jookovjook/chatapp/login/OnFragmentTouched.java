package com.jookovjook.chatapp.login;

import android.support.v4.app.Fragment;

interface OnFragmentTouched {
    void onFragmentTouched(Fragment fragment, float x, float y);
    void onFragmentRevealed();
}