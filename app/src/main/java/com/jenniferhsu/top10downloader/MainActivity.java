package com.jenniferhsu.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// This activity downloads XML files from RSS field.
public class MainActivity extends AppCompatActivity {

    private TextView xmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

        xmlTextView = (TextView) findViewById(R.id.xmlTextView);

        // add some change 1
    }

    private class DownloadData extends AsyncTask<String, Void, String>{
        private String TAG="DataDownload";
        private String mFileContent;

        @Override
        protected String doInBackground(String... params) {
            mFileContent = downloadXML(params[0]);
            if(mFileContent == null){
                Log.d(TAG, "Error downloading");
            }
            return mFileContent;
        }

        // Once doInBackground is finished, it will call this method.

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "Result was: "+result);
            xmlTextView.setText(mFileContent);
        }

        private String downloadXML(String urlPath){
            // A more efficient way of putting strings together.
            StringBuilder tempBuffer = new StringBuilder();
            try{
                // Given the urlPath, you want to create connection to that URL
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "The response code was: "+response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];

                while(true){
                    charRead = isr.read(inputBuffer);
                    if(charRead <= 0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }
                return tempBuffer.toString();
            } catch (IOException e){
                Log.d(TAG, "Error: "+e.getMessage());
                //e.printStackTrace();
            } catch (SecurityException e){
                Log.d(TAG, "Security exception. Needs permission"+e.getMessage());
            }

            // If it returns an error, then the method should still return null.
            return null;
        }
    }


}
