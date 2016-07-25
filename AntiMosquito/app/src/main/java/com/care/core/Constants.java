package com.care.core;

import com.care.antimosquito.R;

/**
 * Created by laliu on 2015/8/11.
 */
public class Constants {

    public static final String LinkMarketPackageFormat = "market://details?id=%s";

    public static String SharedPreferenceFileName = "care_anti_mosquito";

    public static String FlurryApplicationKey = "DCCDGQKMRCC5K84WP936";
    public static String WANDOUJIA_APP_ID = "100041934";
    public static String WANDOUJIA_SECRET_KEY = "7cd9d22a78f1e0b689735537e117388a";
    public static String WANDOUJIA_BANNER_ID = "169c1f7ed62727d0d86c8e2295d22d5f";


    // Key for shared data
    public static String KeyAntiMosquitoTurnedOn = "AntiMosquitoTurnedOn";
    public static String KeyCurrentModeIndex = "CurrentModeIndex";

    public static int MaxModeCount = 4;
    public static int[] VoiceIds = {
            R.raw.mix,
            R.raw.pisk15,
            R.raw.pisk20,
            R.raw.pisk25
    };

    public static int []DescriptionIds = {
            R.string.voice_description_mix,
            R.string.voice_description_adult,
            R.string.voice_description_kids,
            R.string.voice_description_pets
    };

    public static int[] AntiMosquiteImageIds = {
            R.drawable.anti_mosquito_0,
            R.drawable.anti_mosquito_1,
            R.drawable.anti_mosquito_2,
            R.drawable.anti_mosquito_3
    };
}
