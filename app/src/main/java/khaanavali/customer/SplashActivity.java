package khaanavali.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class SplashActivity extends Activity implements Animation.AnimationListener {


    ImageView imgPoster,imgBack;
    Button btnStart;

    // Animation
    Animation animZoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_in);

       // imgPoster = (ImageView) findViewById(R.id.imgLogo);
        imgBack = (ImageView) findViewById(R.id.imgback);

        // load the animation
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);

        // set animation listener
        animZoomIn.setAnimationListener(this);

        // button click event

        Thread timer= new Thread(){
            public void run(){
                try{
                    imgBack.startAnimation(animZoomIn);

                    sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    Intent i= new Intent(SplashActivity.this,MainActivity.class);

                    startActivity(i);
                    finish();
                }

            }


        };
        timer.start();

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animZoomIn) {
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }
}