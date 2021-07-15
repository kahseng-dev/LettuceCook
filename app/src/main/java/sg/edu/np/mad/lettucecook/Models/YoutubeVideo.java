package sg.edu.np.mad.lettucecook.Models;

import android.util.Log;

public class YoutubeVideo {

    String videoUrl;

    public YoutubeVideo() {}

    public YoutubeVideo(String videoUrl) {
        // removing the link from the db to get the video id
        String removeInvertedSlashes = videoUrl.replace("\\", "");
        String videoId = removeInvertedSlashes.replace("https://www.youtube.com/watch?v=", "");

        // using the videoId create a youtube embedded video
        this.videoUrl = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId +"\" frameborder=\"0\" allowfullscreen></iframe>";
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
