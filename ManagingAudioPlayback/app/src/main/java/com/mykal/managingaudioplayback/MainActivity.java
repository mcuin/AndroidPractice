package com.mykal.managingaudioplayback;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AudioManager am = mContext.getSystemService(Context.AUDIO_SERVICE);
        am.registerMediaButtonEventReceiver(RemoteControlReceiver);
        am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);

        int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            am.registerMediaButtonEventReceiver(RemoteControlReceiver);
        }

        am.abandonAudioFocus(afChangeListener);

        int result2 = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        final AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange = AUDIOFOCUS_LOSS_TRANSIENT) {

                } else if (focusChange = AudioManager.AUDIOFOCUS_GAIN) {

                } else if (focusChange = AudioManager.AUDIOFOCUS_LOSS) {
                    am.unregisterMediaButtonEventReciever(RemoteControlReceiver);
                    am.abandonAudioFocus(afChangeListener);
                }
            }
        }

        if (isBlueToothA2dpOn()) {

        } else if (isSpeakerPhoneOn()) {

        } else if (isWiredHeadsetOn()) {

        } else {

        }

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
        }

        return super.onOptionsItemSelected(item);
    }

    public class RemoteControlReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

                if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {

                }
            }
        }
    }

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {

            }
        }
    }

    private IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private void startPlayback() {
        registerReceiver(myNoisyAudioStreamReceiver(), filter);
    }

    private void stopPlayback() {
        unregisterReceiver(myNoisyAudioStreamReceiver());
    }
}

