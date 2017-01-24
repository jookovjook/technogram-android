package com.jookovjook.chatapp.interfaces;

import com.jookovjook.chatapp.new_feed_fragment.FeedCardProvider;

public interface NewGetPublicationsInterfase {
    void onGotPublication(FeedCardProvider feedCardProvider);
    void onGotAll(int last_id);
}
