package com.care.antimosquito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.care.core.Constants;
import com.care.core.SharedDataManager;
import com.care.core.Utilities;
import com.flurry.android.FlurryAgent;
import com.wandoujia.ads.sdk.Ads;

public class MainActivity extends AppCompatActivity {

    private TextView mModeDescriptionTextView = null;
    private ImageView mAntiMosquitoImageView = null;
    private ImageButton mSwitchImageButton = null;
    private LinearLayout mAdBannerLayout = null;

    private MediaPlayer mMediaPlayer1 = null;
    private MediaPlayer mMediaPlayer2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModeDescriptionTextView = (TextView)this.findViewById(R.id.id_mode_description);
        mAntiMosquitoImageView = (ImageView)this.findViewById(R.id.id_anti_mosquito_image);
        mSwitchImageButton = (ImageButton)this.findViewById(R.id.id_anti_mosquito_switch);
        mAdBannerLayout = (LinearLayout)this.findViewById(R.id.id_ad_banner);

        mAntiMosquitoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedDataManager.getInstance().isAntiMosquitoTurnedOn()) {
                    int nextIndex = (SharedDataManager.getInstance().getCurrentModeIndex() + 1) % Constants.MaxModeCount;
                    SharedDataManager.getInstance().setCurrentModeIndex(nextIndex);
                    ;
                    mMediaPlayer1.start();
                    changeStatus();
                }
            }
        });

        mSwitchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedDataManager.getInstance().isAntiMosquitoTurnedOn()) {
                    SharedDataManager.getInstance().setIsAntiMosquitoTurnedOn(false);
                } else {
                    SharedDataManager.getInstance().setIsAntiMosquitoTurnedOn(true);
                }

                mMediaPlayer2.start();
                changeStatus();
            }
        });

        mMediaPlayer1 = MediaPlayer.create(this, R.raw.sound1);
        mMediaPlayer1.setLooping(false);
        mMediaPlayer2 = MediaPlayer.create(this, R.raw.sound2);
        mMediaPlayer2.setLooping(false);

        changeStatus();
        initializeAds();
    }

    private void initializeAds() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Ads.init(MainActivity.this, Constants.WANDOUJIA_APP_ID, Constants.WANDOUJIA_SECRET_KEY);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Ads.preLoad(Constants.WANDOUJIA_BANNER_ID, Ads.AdFormat.banner);
                    View bannerView = Ads.createBannerView(MainActivity.this, Constants.WANDOUJIA_BANNER_ID);
                    mAdBannerLayout.addView(bannerView, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));

                }
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_review) {
            Utilities.launchAppStoreDetail(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(this);
    }

    @Override
    public void onStop() {
        FlurryAgent.onEndSession(this);

        super.onStop();
    }

    public void changeStatus() {
        if(SharedDataManager.getInstance().isAntiMosquitoTurnedOn()) {
            int index = SharedDataManager.getInstance().getCurrentModeIndex();
            mModeDescriptionTextView.setText(Constants.DescriptionIds[index]);
            mAntiMosquitoImageView.setImageResource(Constants.AntiMosquiteImageIds[index]);
            mSwitchImageButton.setBackgroundResource(R.drawable.switch_on);
            stopAntiMosquitoService();
            startAntiMosquitoService();

        } else {
            mModeDescriptionTextView.setText(R.string.share_empty);
            mAntiMosquitoImageView.setImageResource(R.drawable.mosquito);
            mSwitchImageButton.setBackgroundResource(R.drawable.switch_off);
            stopAntiMosquitoService();
        }
    }

    public void startAntiMosquitoService() {
        try {
            Intent intent = new Intent(MainActivity.this, AntiMosquitoService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAntiMosquitoService() {
        try {
            Intent intent = new Intent(MainActivity.this, AntiMosquitoService.class);
            stopService(intent);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
