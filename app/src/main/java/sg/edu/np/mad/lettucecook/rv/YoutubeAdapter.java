package sg.edu.np.mad.lettucecook.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.lettucecook.R;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.VideoViewHolder> {
    List<YoutubeVideo> youtubeVideoList;

    public YoutubeAdapter(List<YoutubeVideo> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
    }

    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube, parent, false);
        return new VideoViewHolder(view);
    }

    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youtubeVideoList.get(position).getVideoUrl(), "text/html", "utf-8");
    }

    public int getItemCount() {
        return youtubeVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView videoWeb;
        public VideoViewHolder(View itemView) {
            super(itemView);
            videoWeb = (WebView) itemView.findViewById(R.id.youtube_video);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient());
        }
    }
}
