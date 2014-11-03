package itcr.gitsnes;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by JuanPC on 31/10/2014.
 */
public class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {


    private String DEVELOPER_KEY = "AIzaSyBK5oT8_nrt_fmC7fv5ClG2HIhSRpaWjvU";

    private YouTubePlayer activePlayer;

    public static PlayerYouTubeFrag newInstance(String url) {

        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        playerYouTubeFrag.setArguments(bundle);

        return playerYouTubeFrag;
    }

    private void init() {

        initialize(DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) { }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                activePlayer = player;
                activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored) {
                    activePlayer.loadVideo(getArguments().getString("url"), 0);

                }
            }
        });
    }


}
