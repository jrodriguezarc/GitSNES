package itcr.gitsnes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.youtube.player.YouTubeIntents;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;


public class MainActivity extends FragmentActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    public static  final String MY_ACCESS_KEY_ID= "AKIAINHV4T4GZJQN4WQA";
    public static final String MY_SECRET_KEY= "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";
    public static final String BUCKET_NAME= "aae55c27-ce98-44ec-adb4-7bc5830ec378";
    public static final String MAIN_PATH = "https://s3.amazonaws.com/aae55c27-ce98-44ec-adb4-7bc5830ec378/";
    public static final String BUCKET_IMG= "cde56c29-1f398-89ec-adb4-7bcyqo0pcqlg";
    LoginButton authButton;

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    JSONArray json_arr;
    File s3game,s3image;
    String file_key = "none";
    String image_key = "none";
    boolean flag = true;

    private UiLifecycleHelper uiHelper;



    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("log_tag", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("log_tag", "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }



    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActionBar().hide();

        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Login login = new Login();
        transaction.add(R.id.placeholder, login).commit();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                Toast.makeText(getApplicationContext(), "Buscando juego aleatorio!!", Toast.LENGTH_SHORT).show();

                MasterGames new_fragment = new MasterGames(json_arr);
                new_fragment.setRandomgame("random");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.placeholder, new_fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);


        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        authButton = (LoginButton) findViewById(R.id.authButton);

        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {

            @Override
            public void onError(FacebookException error) {
                Log.i("log_tag", "Error " + error.getMessage());
            }
        });



        authButton.setReadPermissions(Arrays.asList("email"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {


            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.i("log_tag", "Accesssss Token");
                if (session.isOpened()) {
                    Log.i("log_tag", "Access Token" + session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        Log.i("log_tag", "User ID " + user.getId());
                                        Log.i("log_tag", "Email " + user.asMap().get("email"));

                                        Toast.makeText(getApplicationContext(), user.asMap().get("email").toString(), Toast.LENGTH_SHORT).show();
                                        //lblEmail.setText(user.asMap().get("email").toString());
                                    }
                                }
                            });
                } else
                    Log.i("log_tag", "Nopes Token");
            }
        });





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


    /*@Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; i++) {
            fm.popBackStackImmediate();
        }
    }*/



    public void goMainFrame(View view) {


        getActionBar().show();

        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String input = new BackendHandler().readJSON();
        try {
            json_arr = new JSONArray(input);
        } catch (JSONException e) {
            Log.i("log_tag", e.toString());
        }

        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mainback);
        rl.setBackgroundColor(Color.parseColor("#ffffff"));
        authButton.setVisibility(View.INVISIBLE);
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

        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mainback);
        rl.setBackgroundColor(Color.parseColor("#0099cc"));
        authButton.setVisibility(View.INVISIBLE);

    }


    public void advanced_query(MenuItem item) {

        SearchFrame new_fragment = new SearchFrame();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mainback);
        rl.setBackgroundColor(Color.parseColor("#ff8800"));
        authButton.setVisibility(View.INVISIBLE);

    }


    public void searching(View view) {

        EditText name = (EditText) this.findViewById(R.id.src_name);
        EditText category = (EditText) this.findViewById(R.id.src_category);


        MasterGames new_fragment = new MasterGames(json_arr);
        new_fragment.setQname(name.getText().toString());
        new_fragment.setQcat(category.getText().toString());

        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mainback);
        rl.setBackgroundColor(Color.parseColor("#ffffff"));
        authButton.setVisibility(View.INVISIBLE);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();



    }

    public void sendGame(View view) throws IOException {

        EditText name= (EditText) this.findViewById(R.id.txt_name);
        EditText description = (EditText) this.findViewById(R.id.txt_desc);
        EditText category = (EditText) this.findViewById(R.id.txt_cat);


        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.file_key = ""+UUID.randomUUID().toString().replace("-","");
        this.image_key = ""+UUID.randomUUID().toString().replace("-","");

        AmazonS3Client s3Client = new AmazonS3Client(
                new BasicAWSCredentials( MY_ACCESS_KEY_ID, MY_SECRET_KEY ));


        PutObjectRequest putObjectRequestnew = new PutObjectRequest(BUCKET_NAME, this.file_key, this.s3game);
        putObjectRequestnew.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectRequestnew);

        PutObjectRequest putObjectImagenew = new PutObjectRequest(BUCKET_IMG, this.image_key, this.s3image);
        putObjectImagenew.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectImagenew);


        String actual_key = "none";
        String actual_image = "none";

        if(this.file_key != "none")
            actual_key = this.file_key;

        if(this.image_key != "none")
            actual_image = this.image_key;

        new BackendHandler().sendJSON( "Juan Rodriguez",
                name.getText().toString(),
                category.getText().toString(),
                description.getText().toString() ,actual_image,actual_key);
                Log.i("log_tag", "Successful JSON send");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){


        uiHelper.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && flag ){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                String type = data.getType();
                if (uri != null) {
                    String path = uri.toString();
                    if (path.toLowerCase().startsWith("file://")){
                        this.s3game = new File(URI.create(path));
                        Log.i("log_tag", "archivo cargado");
                    }
                }
            }
        }


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && !flag) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            this.s3image = new File(picturePath);
            Log.i("log_tag", "imagen cargado");
        }

    }

    public void uploadfile(View view) {
        flag = true;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }




    public void download(View view) {
        TextView name= (TextView) this.findViewById(R.id.Uni_name);
        TextView _key= (TextView) this.findViewById(R.id.fileurl);

        final String file_key =  _key.getText().toString();
        final String  file_name =  name.getText().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toast.makeText(getApplicationContext(), "Starting download...", Toast.LENGTH_SHORT).show();
        String url = MAIN_PATH + file_key;
        new DownloadFileAsync().execute(url,file_name);
        Toast.makeText(getApplicationContext(), "The file was downloaded", Toast.LENGTH_SHORT).show();
    }


    public void select_image(View view) {
        flag = false;
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }






    public void goVideo(View view) {
        TextView name= (TextView) this.findViewById(R.id.Uni_name);

        Intent intent = YouTubeIntents.createSearchIntent(this,"gameplay " + name.getText().toString() + " snes") ;
        startActivity(intent);
    }






    @Override
    public void onResume() {
        super.onResume();
       // uiHelper.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
       // uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    public void goList(MenuItem item) {
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String input = new BackendHandler().readJSON();
        try {
            json_arr = new JSONArray(input);
        } catch (JSONException e) {
            Log.i("log_tag", e.toString());
        }

        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.mainback);
        rl.setBackgroundColor(Color.parseColor("#ffffff"));
        authButton.setVisibility(View.INVISIBLE);
        MasterGames new_fragment = new MasterGames(json_arr);
        new_fragment.setQtype("all");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, new_fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}


