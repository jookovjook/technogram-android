package com.jookovjook.chatapp.interfaces;

import com.jookovjook.chatapp.feed_fragment.FeedCardProvider;

public interface GetPublicationsInterfase {
    void onGotPublication(FeedCardProvider feedCardProvider);
    void onGotAll(int last_id);
}
