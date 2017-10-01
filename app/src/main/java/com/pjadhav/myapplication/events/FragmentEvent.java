package com.pjadhav.myapplication.events;

import com.pjadhav.myapplication.model.MusicFile;

/**
 * Created by pjadhav on 7/1/17.
 */

public class FragmentEvent {


    // Event used to send message from fragment to activity.
    public static class ActivityFragmentMessage {
        private String message;
        public ActivityFragmentMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }


    // Event used to send message from adapter to activity for downloading the clip.
    public static class ClipDownloadFromListMessage {
        public MusicFile mMusicFile;
        public ClipDownloadFromListMessage(MusicFile musicFile) {
            this.mMusicFile = musicFile;
        }
        public MusicFile getMusicFile() {
            return mMusicFile;
        }
    }

    // Event used to send message from activity to adapter to change the button icons(download/play).
    public static class ChangeButtonImageOnListMessage {
        private String message;
        public ChangeButtonImageOnListMessage(String message) {
            this.message = message;
        }
        public String getMessageToChangeButtonImage() {
            return message;
        }
    }

}
