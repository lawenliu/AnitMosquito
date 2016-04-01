package com.care.antimosquito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.care.core.Constants;
import com.care.core.SharedDataManager;
import com.care.core.Utilities;
import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdBanner;
import com.flurry.android.ads.FlurryAdBannerListener;
import com.flurry.android.ads.FlurryAdErrorType;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mModeDescriptionTextView = null;
    private ImageView mAntiMosquitoImageView = null;
    private ImageButton mSwitchImageButton = null;
    private RelativeLayout mFlurryAdBannerLayout = null;

    private FlurryAdBanner mFlurryAdBanner = null;

    private MediaPlayer mMediaPlayer1 = null;
    private MediaPlayer mMediaPlayer2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModeDescriptionTextView = (TextView)this.findViewById(R.id.id_mode_description);
        mAntiMosquitoImageView = (ImageView)this.findViewById(R.id.id_anti_mosquito_image);
        mSwitchImageButton = (ImageButton)this.findViewById(R.id.id_anti_mosquito_switch);
        mFlurryAdBannerLayout = (RelativeLayout)this.findViewById(R.id.id_flurry_ad_banner);

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

        mFlurryAdBanner = new FlurryAdBanner(this, mFlurryAdBannerLayout, Constants.FlurryAdSpace);
        mFlurryAdBanner.setListener(new FlurryAdBannerListener() {
            @Override
            public void onFetched(FlurryAdBanner flurryAdBanner) {
                flurryAdBanner.displayAd();
            }

            @Override
            public void onRendered(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onShowFullscreen(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onCloseFullscreen(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onAppExit(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onClicked(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onVideoCompleted(FlurryAdBanner flurryAdBanner) {
            }

            @Override
            public void onError(FlurryAdBanner flurryAdBanner, FlurryAdErrorType flurryAdErrorType, int i) {
                flurryAdBanner.destroy();
            }
        });

        mMediaPlayer1 = MediaPlayer.create(this, R.raw.sound1);
        mMediaPlayer1.setLooping(false);
        mMediaPlayer2 = MediaPlayer.create(this, R.raw.sound2);
        mMediaPlayer2.setLooping(false);

        changeStatus();
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
        if(mFlurryAdBanner != null) {
            mFlurryAdBanner.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(this);
        mFlurryAdBanner.fetchAd();
    }

    @Override
    public void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(this);
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
