package tricolor.com.chennaiwardmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by udhayakumarulaganathan on 03/01/18.
 */

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoadingScreenActivity.class);
                startActivity(i);

                // Complete the Splash Screen Activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
