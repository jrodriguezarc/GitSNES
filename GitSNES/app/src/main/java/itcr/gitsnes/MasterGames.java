package itcr.gitsnes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MasterGames extends Fragment{


    JSONArray json_arr;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListView listView;

    private static final String TAG_NAME = "name";
    private static final String TAG_DESC = "category";
    private static final String TAG_CAT = "description";
    public String qtype ,qname,qdesc,qcat;


    public MasterGames(JSONArray json){
        this.json_arr = json;
        this.qtype   = "" ;
        this.qname  = "";
        this.qdesc  = "";
        this.qcat   = "";
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        view = inflater.inflate(R.layout.master_games, container, false);

        try {
            for(int i = 0; i < json_arr.length(); i++){
                final JSONObject c = json_arr.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String category = c.getString(TAG_CAT);
                final String des = c.getString(TAG_DESC);

                if((!qname.equals("") && name.equals(qname)) ||
                       (!qcat.equals("") && name.equals(qcat)) ||
                         qtype.equals("all")) {

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_NAME, name);
                    map.put(TAG_CAT, category);
                    map.put(TAG_DESC, des);
                    oslist.add(map);

                    listView = (ListView) view.findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                            R.layout.game_detail,
                            new String[]{TAG_NAME, TAG_CAT, TAG_DESC}, new int[]{
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
                            String []desc = parts[1].split("=");
                            String []name = parts[2].split("=");

                         /*Toast.makeText(getActivity(), object.toString()
                                        + "", Toast. LENGTH_LONG).show();*/

                        UniqueGame new_fragment =
                                new UniqueGame(name[1],cat[1],desc[1]);
                        FragmentTransaction transaction =
                                getFragmentManager().beginTransaction();
                        transaction.replace(R.id.placeholder, new_fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();




                        }
                    });

                    ImageView imageView = (ImageView) view.findViewById(R.id.img_game);


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
}
