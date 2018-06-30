package com.example.android.miwok;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter <Translator> /* extending Class ArrayAdapter to user defined WordAdapter(subClass) */
                                            /* Return type -java file -object */
{

    int mBgColorID;
    public WordAdapter(Activity context, ArrayList <Translator> numWords, int BgColor ) /* Constructor for customArrayAdapter */
                                                 /* Return type -java file -object,ArrayList var */
    {
        super(context, 0, numWords);
        mBgColorID = BgColor;
    }


    @Override /* code -> override methods */
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View ListItemView = convertView;
        if(ListItemView == null) {
            ListItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.translator, parent, false);
        }

        // Get the object located at this position in the list - Data type should be return type - corresponding java file
         Translator currentWord = getItem(position);

        // Find the Tamil Number TextView in the translator layout using the ID
        TextView tamTextView = (TextView) ListItemView.findViewById(R.id.textView);
        // Get Tamil number from the currentNumWord object using function in Translator java,set this text on the tamTextView
        tamTextView.setText(currentWord.getDefTranslation());

        // Find the English Number TextView in the translator.xml using the ID
        TextView engTextView = (TextView) ListItemView.findViewById(R.id.textView2);
        // Get English number from the currentNumWord object using function in Translator java,set this text on the engTextView
        engTextView.setText(currentWord.getEngTranslation());

        // Find the corresponding ImageView in the translator layout using the ID
        ImageView ImgView = (ImageView) ListItemView.findViewById(R.id.ImageView);

        if(currentWord.hasImage())
        {
            // Get corresponding Image from the currentNumWord object using function in Translator java,set this text on the numImgView
            ImgView.setImageResource(currentWord.getImgTranslation());

            // Make sure the ImageView is visible
            ImgView.setVisibility(View.VISIBLE);
        }
        else
            ImgView.setVisibility(View.GONE); /* If no image hide the ImageView */

        //Find textContainer view that has the text fields in translator.xml using ID
        View textContainer = (View) ListItemView.findViewById(R.id.text_container);
        //Find the appropirate colour based on what domainpg(using getcontext) is asking for
        int color = ContextCompat.getColor(getContext(),mBgColorID);
        //set the corresponding color to the textContainer
        textContainer.setBackgroundColor(color);

        // Return the whole inflate numListItemView so that it can be shown in the ListView
        return ListItemView;
    }
}
