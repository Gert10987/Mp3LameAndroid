package info.gert.mp3lameAndroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by gert on 03.05.17.
 */

public class CustomProgressDialog {

    private ProgressDialog progressDialog;
    private Context context;
    private int incrementValue;

    public CustomProgressDialog(Context context) {

        this.context = context;
        incrementValue = 0;
    }

    public void init(String title, String message, int max, int type) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMax(max);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(type);
        progressDialog.setCancelable(false);
    }

    public void startTypeSpinner() {
        progressDialog.show();
    }

    public void dissmisDialog() {
        progressDialog.dismiss();
    }

    public void startTypeHorizontal() {

        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog
                            .getMax()) {
                        handle.sendMessage(handle.obtainMessage());
                        if (progressDialog.getProgress() == progressDialog
                                .getMax()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.incrementProgressBy(incrementValue);
        }
    };

    public void incrementProgress(int incrementValue) {
        this.incrementValue = incrementValue;
    }
}