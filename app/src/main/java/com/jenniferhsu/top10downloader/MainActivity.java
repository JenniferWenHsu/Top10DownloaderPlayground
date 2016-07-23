package com.jenniferhsu.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Async Task.
    // String: Download location
    // Second parameter: usually you want to use this for the progress bar
    // Thrid parameter: actual result
    private class DownloadData extends AsyncTask<String, Void, String> {

        private String TAG="DownloadData";

        private String mFileContents;
        // String... means that the number of the parameters is a variable.
        @Override
        protected String doInBackground(String... params) {
            // tasks that you want to run in the background
            mFileContents = downloadXMLFile(params[0]);
            if (mFileContents == null){
                Log.d(TAG, "Error downloading!");
            }

            return mFileContents;
        }

        // Write the code that does the actual downloading.
        private String downloadXMLFile(String urlPath){
            // Create a temporary buffer to store all those files
            // because we can only download so much while downloading.
            StringBuilder tempBuffer = new StringBuilder();
            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // An example would be the 404 you get from a webpage
                int response = connection.getResponseCode();
                Log.d(TAG, "The respond code is: "+response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];
                while(true){
                    // Try to read the datat in the inputBuffer.
                    // Returns the number it's reading.
                    charRead = isr.read(inputBuffer);
                    if(charRead <= 0) {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }
                return tempBuffer.toString();

            } catch (IOException e){
                Log.d(TAG, "IO Exception reading: "+e.getMessage());
            }

            //REMEMBER TO GET RID OF THIS LINE!!!!!
            return tempBuffer.toString();
        }
    }


}
