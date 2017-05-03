package info.gert.mp3lameAndroid;

import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static String wavSource = Environment.getExternalStorageDirectory().toString() + "/" + "test.wav"; // you wav file
    private static String mp3Output = Environment.getExternalStorageDirectory().toString() + "/" + "testMp3.mp3"; // mp3 output

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Encode().execute();
            }
        });
    }

    public class Encode extends AsyncTask<String, Integer, String> {

        private CustomProgressDialog customProgressDialog;

        protected void onPreExecute() {

            customProgressDialog = new
                    CustomProgressDialog(MainActivity.this);

            customProgressDialog.init("Encode", "Encoding....", 0, ProgressDialog.STYLE_SPINNER);
            customProgressDialog.startTypeSpinner();
        }

        protected String doInBackground(String... arg0) {

            Mp3Encoder mp3Encoder = new Mp3Encoder();
            mp3Encoder.encode(wavSource, mp3Output);

            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.isEmpty() == false) {

                Toast.makeText(MainActivity.this, "Encode Done", Toast.LENGTH_SHORT).show();
                customProgressDialog.dissmisDialog();
            }
        }
    }
}
