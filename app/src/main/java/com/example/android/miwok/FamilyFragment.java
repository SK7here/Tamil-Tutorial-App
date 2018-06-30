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
public class FamilyFragment extends Fragment {
    //It is of object type of Translator java class //final used to make use of WORDS in onItemClickListener
    ArrayList<Translator> famWords;
    ListView root;
    WordAdapter WordList; /*Custom ArrayAdapter */
    private MediaPlayer famMp;
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
                famMp.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The GAIN case means we have regained focus and can resume playback
                famMp.start();
            }
        }
    };


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //2 lines of code changed
        View RootView= (ListView) inflater.inflate(R.layout.domainpg, container, false); //default code to be added by us

        famWords =  new ArrayList<Translator>();// Takes Translator constructor with 4 arguements

        famWords.add(new Translator("அம்மா ","Mother",R.drawable.family_mother,R.raw.fam_mother));
        famWords.add(new Translator("அப்பா","Father",R.drawable.family_father,R.raw.fam_father));
        famWords.add(new Translator("தாத்தா ","GrandFather",R.drawable.family_grandfather,R.raw.fam_grandfather));
        famWords.add(new Translator("பாட்டி","GrandMother",R.drawable.family_grandmother,R.raw.fam_grandmother));
        famWords.add(new Translator("மகன்","Son",R.drawable.family_son,R.raw.fam_son));
        famWords.add(new Translator("மகள் ","Daughter",R.drawable.family_daughter,R.raw.fam_daughter));
        famWords.add(new Translator("அண்ணன்","Elder Brother",R.drawable.family_older_brother,R.raw.fam_elderbrother));
        famWords.add(new Translator("தம்பி","Younger Brother",R.drawable.family_younger_brother,R.raw.fam_youngerbrother));
        famWords.add(new Translator("அக்கா","Elder Sister",R.drawable.family_older_sister,R.raw.fam_eldersister));
        famWords.add(new Translator("தங்கை","Younger Sister",R.drawable.family_younger_sister,R.raw.fam_youngersister));
        famWords.add(new Translator("அத்தை","Aunt",R.drawable.family_mother,R.raw.fam_aunt));



        WordList = new WordAdapter(getActivity(),famWords,R.color.category_family); /* Custom ArrayAdapter def */
        root= (ListView) RootView.findViewById(R.id.rootView);
        root.setAdapter(WordList); /* Adding child Custom ArrayAdapter to single ListView */

        AudManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE); // Audio focus sake

        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                freeMP(); //To free Media player at beginning if ot is holding any other sound file
                Translator famaudiopos = famWords.get(position); // To get position of audio

                int result = AudManager.requestAudioFocus(mOnAudioFocusChangeListener, //listener
                        AudioManager.STREAM_MUSIC, //Enable volume correction
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT /*TRANSIENT because audio file is small*/);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    Toast.makeText(getActivity(), "Hear carefully", Toast.LENGTH_SHORT).show();
                    famMp = MediaPlayer.create(getActivity(), famaudiopos.getAudTranslation()); //Playing corresponding audio
                    famMp.start(); //plays music
                    famMp.setOnCompletionListener(mCompleteListener); //calling function "after complete playing" the song
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
        if (famMp != null)
        {
            famMp.release();
            famMp = null;
            AudManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}