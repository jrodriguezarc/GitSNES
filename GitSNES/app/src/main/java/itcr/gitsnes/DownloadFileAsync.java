package itcr.gitsnes;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by JuanPC on 29/10/2014.
 */
class DownloadFileAsync  extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... aurl){

        Log.i("log_tag", "comenzando");
        int count;
        try {
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream("/sdcard/Download/"+ aurl[1] + ".smc");
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            Log.i("log_tag", "Descagado");
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {

            Log.i("log_tag", e.toString());
        }


        return null;
    }
}