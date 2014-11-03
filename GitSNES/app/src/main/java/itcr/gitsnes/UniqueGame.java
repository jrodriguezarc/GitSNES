package itcr.gitsnes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by JuanPC on 26/10/2014.
 */
public class UniqueGame extends Fragment {

    String name,category,desc,url_file,url_img;
    String URL = "https://s3-us-west-2.amazonaws.com/cde56c29-1f398-89ec-adb4-7bcyqo0pcqlg/";

    
    public UniqueGame(String name, String category, String desc,String file, String img){
        this.name = name;
        this.category = category;
        this.desc=desc;
        this.url_file = file;
        this.url_img =img;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final View view = inflater.inflate(R.layout.unique_game, container, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.Uni_name);
        nameTextView.setText(this.name);
        TextView typeView = (TextView) view.findViewById(R.id.Uni_category);
        typeView.setText(this.category);
        TextView descView = (TextView) view.findViewById(R.id.Uni_decription);
        descView.setText(this.desc);
        TextView file = (TextView) view.findViewById(R.id.fileurl);
        file.setText(this.url_file);
        final TextView image = (TextView) view.findViewById(R.id.imgurl);
        image.setText(this.url_img);

        new Thread(new Runnable() {
        @Override
        public void run() {
           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView myFirstImage = (ImageView) view.findViewById(R.id.Uni_img_game);
                    myFirstImage.setTag(URL+image.getText().toString());
                    new DownloadImagesTask().execute(myFirstImage);
                }
            });
        }}).start();




        return view;
    }

}
