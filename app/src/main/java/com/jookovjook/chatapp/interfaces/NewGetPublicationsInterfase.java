package com.jookovjook.chatapp.interfaces;

import com.jookovjook.chatapp.feed_fragment.FeedCardProvider;

public interface NewGetPublicationsInterfase {
    void onGotPublication(FeedCardProvider feedCardProvider);
    void onGotAll(int last_id);
}
