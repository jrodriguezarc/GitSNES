package itcr.gitsnes;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by JuanPC on 26/10/2014.
 */
public class BackendHandler {

    public String readJSON(){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://gitsnes.appspot.com/games");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e("log_tag", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public void sendJSON(String arg0, String arg1,
                         String arg2, String arg3,
                         String arg4, String arg5) throws JSONException {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://gitsnes.appspot.com/games");
        String sender = "https://gitsnes.appspot.com/game/"  + arg0 + "/"
                + arg1 + "/" +arg2 + "/" + arg3 + "/" +arg4 + "/" + arg5 ;
        try {
            HttpPost request = new HttpPost(sender);
            request.addHeader("content-type", "application/json");
            client.execute(request);
        } catch (Exception ex) {
            Log.i("log_tag", ex.toString());
        }
    }

}
