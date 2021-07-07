package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Vector;

import sg.edu.np.mad.lettucecook.Models.YoutubeAdapter;
import sg.edu.np.mad.lettucecook.Models.YoutubeVideo;

public class RecipeDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Youtube Video Guides Recycler View
        recyclerView = findViewById(R.id.ytRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        youtubeVideos.add(new YoutubeVideo("https:\\/\\/www.youtube.com\\/watch?v=1IszT_guI08"));
        YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);
    }
}