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
public class PhrasesFragment extends Fragment {

    //It is of object type of Translator java class //final used to make use of WORDS in onItemClickListener
    ArrayList<Translator> phraseWords;
    ListView root;
    WordAdapter phraseWordList; /*Custom ArrayAdapter */
    private MediaPlayer phraseMp;
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
                phraseMp.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The GAIN case means we have regained focus and can resume playback
                phraseMp.start();
            }
        }
    };


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Lines to be changed
        View RootView= (ListView) inflater.inflate(R.layout.domainpg, container, false); //default code to be added by us


        phraseWords = new ArrayList<Translator>();/* Takes Translator constructor with 3 arguements */
        phraseWords.add(new Translator("உங்கள் பெயர் என்ன?","What is your Name?",R.raw.wh_name));
        phraseWords.add(new Translator("நீங்கள் எங்கே போக வேண்டும்?","Where do you want to go?",R.raw.wh_destination));
        phraseWords.add(new Translator("எங்கே இருந்து வருகிறாய்?","Where are you coming from?",R.raw.wh_native));
        phraseWords.add(new Translator("எப்படி இருக்கிறீர்கள்?","How are you?",R.raw.wh_fine));
        phraseWords.add(new Translator("நீங்கள் எங்கு வசிக்கிறீர்கள்?","Where are you living?",R.raw.wh_residence));
        phraseWords.add(new Translator("நீ எப்படி வந்தாய்?","How did you come?",R.raw.wh_transport));
        phraseWords.add(new Translator("நீங்கள் எப்போது வருகிறீர்கள்?","When are you coming?",R.raw.wh_arrival));
        phraseWords.add(new Translator("உங்களுக்கு என்ன வேண்டும்?","What do you want?",R.raw.wh_need));
        phraseWords.add(new Translator("ஏன் இங்கு வந்தாய்?","Why did you come here?",R.raw.wh_reason));
        phraseWords.add(new Translator("நீ எங்கே பணியாற்றுகிறாய்?","Where are you working?",R.raw.wh_work));
        phraseWords.add(new Translator("நீங்கள் எனக்கு உதவ முடியுமா?","Can you help me?",R.raw.wh_help));


        phraseWordList = new WordAdapter(getActivity(),phraseWords,R.color.category_phrases); /* Custom ArrayAdapter def */
        root= (ListView) RootView.findViewById(R.id.rootView);
        root.setAdapter(phraseWordList); /* Adding child Custom ArrayAdapter to single ListView */

        AudManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE); // Audio focus sake

        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                freeMP(); //To free Media player at beginning if ot is holding any other sound file
                Translator phraseaudiopos = phraseWords.get(position); // To get position of audio

                int result = AudManager.requestAudioFocus(mOnAudioFocusChangeListener, //listener
                        AudioManager.STREAM_MUSIC, //Enable volume correction
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT /*TRANSIENT because audio file is small*/);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    Toast.makeText(getActivity(), "Hear carefully", Toast.LENGTH_SHORT).show();
                    phraseMp = MediaPlayer.create(getActivity(), phraseaudiopos.getAudTranslation()); //Playing corresponding audio
                    phraseMp.start(); //plays music
                    phraseMp.setOnCompletionListener(mCompleteListener); //calling function "after complete playing" the song
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
        if (phraseMp != null)
        {
            phraseMp.release();
            phraseMp = null;
            AudManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
