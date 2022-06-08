package com.aizhan.rockitapp.VideoClasses;

import android.net.Uri;

public class MusVideo {
    String video_desc;
    String video_uri;

    public MusVideo(){

    }

    public MusVideo(String video_desc, String video_uri) {
        this.video_desc = video_desc;
        this.video_uri = video_uri;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public String getVideo_uri() {
        return video_uri;
    }
}
