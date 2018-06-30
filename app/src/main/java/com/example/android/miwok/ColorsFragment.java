package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    //It is of object type of Translator java class //final used to make use of WORDS in onItemClickListener
    ArrayList<Translator> colWords;
    ListView root;
    WordAdapter colWordList; /*Custom ArrayAdapter */
    private MediaPlayer colMp;
    //Declaring below function globally as a variable to make use of it without creating function every time
    private MediaPlayer.OnCompletionListener mCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            freeMP();
        }
    };

    private AudioManager AudManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //Moved to another normal app or another audio app being played we pause
                colMp.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The GAIN case means we have regained focus and can resume playback
                colMp.start();
            }
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Lines to be changed
        View RootView= (ListView) inflater.inflate(R.layout.domainpg, container, false); //default code to be added by us



        colWords = new ArrayList<Translator>();// Takes Translator constructor with 4 arguements

        colWords.add(new Translator("சிவப்பு","Red",R.drawable.color_red,R.raw.col_red));
        colWords.add(new Translator("மஞ்சள்","Yellow",R.drawable.color_mustard_yellow,R.raw.col_yellow));
        colWords.add(new Translator("வெள்ளை","White",R.drawable.color_white,R.raw.col_white));
        colWords.add(new Translator("கருப்பு","Black",R.drawable.color_black,R.raw.col_black));
        colWords.add(new Translator("பழுப்பு","Brown",R.drawable.color_brown,R.raw.col_brown));
        colWords.add(new Translator("வெள்ளி","Silver",R.drawable.color_gray,R.raw.col_silver));
        colWords.add(new Translator("பச்சை","Green",R.drawable.color_green,R.raw.col_green));
        colWords.add(new Translator("பொன்னிறம்","Gold",R.drawable.color_dusty_yellow,R.raw.col_gold));
        colWords.add(new Translator("வெண்கலம்","Bronze",R.drawable.color_brown,R.raw.col_bronze));



        colWordList = new WordAdapter(getActivity(),colWords,R.color.category_colors); /* Custom ArrayAdapter def */
        root= (ListView) RootView.findViewById(R.id.rootView); //Define list view as child of RootView
        root.setAdapter(colWordList); /* Adding child Custom ArrayAdapter to single ListView */

        AudManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE); // Audio focus sake

        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                freeMP(); //To free Media player at beginning if ot is holding any other sound file
                Translator colaudiopos = colWords.get(position); // To get position of audio

                int result = AudManager.requestAudioFocus(mOnAudioFocusChangeListener, //listener
                        AudioManager.STREAM_MUSIC, //Enable volume correction
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT /*TRANSIENT because audio file is small*/);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    Toast.makeText(getActivity(), "Hear carefully", Toast.LENGTH_SHORT).show();
                    colMp = MediaPlayer.create(getActivity(), colaudiopos.getAudTranslation()); //Playing corresponding audio
                    colMp.start(); //plays music
                    colMp.setOnCompletionListener(mCompleteListener);//calling function "after complete playing" the song
                }
            }
        });

        return RootView;
    }

    @Override
    public void onStop() //cuts down app actions if user goes out of app immediately
    {
        super.onStop();
        freeMP();
    }

    public void freeMP() //function to free audio file variable
    {
        if (colMp != null)
        {
            colMp.release();
            colMp = null;
            AudManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}

