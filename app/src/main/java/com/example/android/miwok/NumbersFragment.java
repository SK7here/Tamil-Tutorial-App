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
public class NumbersFragment extends Fragment {

    //It is of object type of Translator java class //final used to make use of WORDS in onItemClickListener
    ArrayList<Translator> Words;
    ListView root;
    WordAdapter WordList; /*Custom ArrayAdapter */
    //Declaring below function globally as a variable to make use of it without creating function every time

    private AudioManager AudManager;
    private MediaPlayer numMp;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //Moved to another normal app or another audio app being played we pause
                numMp.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The GAIN case means we have regained focus and can resume playback
                numMp.start();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            freeMP();
        }
    };



    public NumbersFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // lines of code changed
        View RootView= (ListView) inflater.inflate(R.layout.domainpg, container, false); //default code to be added by us

        Words = new ArrayList<Translator>();// Takes Translator constructor with 4 arguements
        Words.add(new Translator("ஒன்று","One",R.drawable.number_one,R.raw.number_one));
        Words.add(new Translator("இரண்டு","Two",R.drawable.number_two,R.raw.number_two));
        Words.add(new Translator("மூன்று","Three",R.drawable.number_three,R.raw.number_three));
        Words.add(new Translator("நான்கு","Four",R.drawable.number_four,R.raw.number_four));
        Words.add(new Translator("ஐந்து","Five",R.drawable.number_five,R.raw.number_five));
        Words.add(new Translator("ஆறு","Six",R.drawable.number_six,R.raw.number_six));
        Words.add(new Translator("ஏழு","Seven",R.drawable.number_seven,R.raw.number_seven));
        Words.add(new Translator("எட்டு","Eight",R.drawable.number_eight,R.raw.number_eight));
        Words.add(new Translator("ஒன்பது","Nine",R.drawable.number_nine,R.raw.number_nine));
        Words.add(new Translator("பத்து","Ten",R.drawable.number_ten,R.raw.number_ten));



        WordList = new WordAdapter(getActivity(),Words,R.color.category_numbers); /* Custom ArrayAdapter def */
        ListView root = (ListView) RootView.findViewById(R.id.rootView); //Define list view as child of RootView
        root.setAdapter(WordList); /* Adding child Custom ArrayAdapter to single ListView */

        AudManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE); // Audio focus sake

        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                freeMP(); //To free Media player at beginning if ot is holding any other sound file
                Translator numaudiopos = Words.get(position); // To get position of audio
                int result = AudManager.requestAudioFocus(mOnAudioFocusChangeListener, //listener
                        AudioManager.STREAM_MUSIC, //Enable volume correction
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT /*TRANSIENT because audio file is small*/);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    // We have audio focus now.
                    Toast.makeText(getActivity(), "Hear carefully", Toast.LENGTH_SHORT).show();
                    numMp = MediaPlayer.create(getActivity(), numaudiopos.getAudTranslation()); //Playing corresponding audio
                    numMp.start(); //plays music
                    numMp.setOnCompletionListener(mCompleteListener); //calling function "after complete playing" the song
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
        if (numMp != null)
        {
            numMp.release();
            numMp = null;
            AudManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}

