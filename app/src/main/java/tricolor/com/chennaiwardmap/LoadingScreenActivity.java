package tricolor.com.chennaiwardmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by udhayakumarulaganathan on 03/01/18.
 */

public class LoadingScreenActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoadingScreenActivity.this, MapsActivity.class);
                startActivity(i);

                // Complete the Splash Screen Activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
