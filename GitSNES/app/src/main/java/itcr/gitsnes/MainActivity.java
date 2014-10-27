package itcr.gitsnes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MainActivity extends FragmentActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    JSONArray json_arr;
    byte file_on_bytes[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Login login = new Login();
        transaction.add(R.id.placeholder, login).commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add_game) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){

        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; i++) {
            fm.popBackStackImmediate();
        }
    }



    public void goMainFrame(View view) {
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String input = new BackendHandler().readJSON();
        try {
            json_arr = new JSONArray(input);
        } catch (JSONException e) {
            Log.i("log_tag", e.toString());
        }


        MasterGames new_fragment = new MasterGames(json_arr);
        new_fragment.setQtype("all");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void add_games(MenuItem item) {
        AddGame new_fragment = new AddGame();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void advanced_query(MenuItem item) {
        SearchFrame new_fragment = new SearchFrame();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void searching(View view) {

        EditText name = (EditText) this.findViewById(R.id.src_name);
        EditText category = (EditText) this.findViewById(R.id.src_category);


        MasterGames new_fragment = new MasterGames(json_arr);
        new_fragment.setQname(name.getText().toString());
        new_fragment.setQcat(category.getText().toString());

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();



    }

    public void sendGame(View view) {

        EditText name= (EditText) this.findViewById(R.id.txt_name);
        EditText description = (EditText) this.findViewById(R.id.txt_desc);
        EditText category = (EditText) this.findViewById(R.id.txt_cat);


        try {
            new BackendHandler().sendJSON( "JuanRodriguez",
                    name.getText().toString(),
                    category.getText().toString(),
                    description.getText().toString() , "fileurl","imageurl");
            Log.i("log_tag","Successful JSON send");
        } catch (JSONException e) {
            Log.i("log_tag",e.toString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadfile(View view) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}


