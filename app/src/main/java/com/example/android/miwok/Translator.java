package com.example.android.miwok;

public class Translator {

String mDefTranslation;
String mEngTranslation;
private static final int NO_IMG_PROVIDED = -1; /* To make this code usable for phrases layout also */
    private int mImgTranslation = NO_IMG_PROVIDED; // Using img as int id to minimize memory
int mAudTranslation; // Using audio as int id to minimize memory


    public Translator(String defTranslation , String engTranslation , int audID)  //constructor with 3 arguements (for PhraseActivity propose)
    {

        mDefTranslation = defTranslation;
        mEngTranslation = engTranslation;
        mAudTranslation = audID;
    }


    public Translator(String defTranslation , String engTranslation , int imgID , int audID)  //constructor with 3 arguements for other Activities
{

    mDefTranslation = defTranslation;
    mEngTranslation = engTranslation;
    mImgTranslation = imgID;
    mAudTranslation = audID;
}


public String getDefTranslation()
{
    return mDefTranslation;
}

public String getEngTranslation() { return mEngTranslation; }

public int getImgTranslation()    { return mImgTranslation; }

public boolean hasImage()         {return (mImgTranslation != NO_IMG_PROVIDED);}

public int getAudTranslation()    {return mAudTranslation;}
}
