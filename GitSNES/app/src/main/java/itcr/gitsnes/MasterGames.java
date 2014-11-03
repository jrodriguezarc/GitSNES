package itcr.gitsnes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.google.android.youtube.player.YouTubePlayerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MasterGames extends Fragment{


    JSONArray json_arr;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListView listView;


    private static final String TAG_NAME = "name";
    private static final String TAG_DESC = "category";
    private static final String TAG_CAT = "description";
    private static final String TAG_FILE = "file_url";
    private static final String TAG_IMG = "image_url";
    String URL = "https://s3-us-west-2.amazonaws.com/cde56c29-1f398-89ec-adb4-7bcyqo0pcqlg/";
    static String image_key = "asasdas";
    public String qtype ,qname,qdesc,qcat,randomgame;


    public MasterGames(JSONArray json){
        this.json_arr = json;
        this.qtype   = "" ;
        this.qname  = "";
        this.qdesc  = "";
        this.qcat   = "";
        this.randomgame = "";
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.master_games, container, false);
        int rd = randInt(0,json_arr.length()-1);
        try {
            for(int i = 0; i < json_arr.length(); i++){
                final JSONObject c = json_arr.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String category = c.getString(TAG_CAT);
                String des = c.getString(TAG_DESC);
                String img = c.getString(TAG_IMG);
                String file = c.getString(TAG_FILE);


                Log.i("log_tag", Integer.toString(rd) );
                if(randomgame.equals("random") && rd == i){
                    UniqueGame new_fragment =
                            new UniqueGame(name,category,des,file,img);
                    FragmentTransaction transaction =
                            getFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder, new_fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return view;
                }






                if((!qname.equals("") && name.equals(qname)) ||
                       (!qcat.equals("") && name.equals(qcat)) ||
                         qtype.equals("all")) {



                    HashMap<String, String> map = new HashMap<String, String>();
                    des      = des.replace("_", " ");
                    category = category.replace("_", " ");
                    map.put(TAG_NAME, name);
                    map.put(TAG_CAT, category);
                    map.put(TAG_DESC, des);
                    map.put(TAG_FILE, img);
                    map.put(TAG_IMG, file);
                    oslist.add(map);

                    listView = (ListView) view.findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                            R.layout.game_detail,
                            new String[]{TAG_NAME, TAG_CAT, TAG_DESC, TAG_IMG,TAG_FILE}, new int[]{
                            R.id.name, R.id.category, R.id.decription});

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                            Object object = listView.getItemAtPosition(position);
                            String json_code=  object.toString();
                            json_code = json_code.replace("{","");
                            json_code = json_code.replace("}","");
                            String parts[] = json_code.split(",");
                            String []cat = parts[0].split("=");
                            String []desc = parts[3].split("=");
                            String []name = parts[4].split("=");
                            String []_file = parts[1].split("=");
                            String []_img = parts[2].split("=");
                            image_key = _img[1];

                         /*Toast.makeText(getActivity(), object.toString()
                                        + "", Toast. LENGTH_LONG).show();*/

                            Log.i("log_tag",_file[1] );
                            Log.i("log_tag",_img[1] );

                        UniqueGame new_fragment =
                                new UniqueGame(name[1],cat[1],desc[1],_file[1],_img[1]);


                        FragmentTransaction transaction =
                                getFragmentManager().beginTransaction();
                        transaction.replace(R.id.placeholder, new_fragment);


                        transaction.addToBackStack(null);
                        transaction.commit();

                        }
                    });



                               /* ImageView myFirstImage = (ImageView) view.findViewById(R.id.img_game);
                                myFirstImage.setTag(URL+image_key);
                                new DownloadImagesTask().execute(myFirstImage);*/



                    listView.setAdapter(adapter);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }


    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public void setQcat(String qcat) {
        this.qcat = qcat;
    }

    public void setQdesc(String qdesc) {
        this.qdesc = qdesc;
    }

    public void setRandomgame(String randomgame) {
        this.randomgame = randomgame;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
