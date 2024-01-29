package com.sellmycode.tns.moneymania.AdMobAds;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Admob {
    OnDismiss onDismiss;

    public Admob(OnDismiss onDismiss){
        this.onDismiss = onDismiss;
    }
    public  static  void loadBannerAd(LinearLayout banner, Context context){
        if (Adsunit.isAds){
            MobileAds.initialize(context,initializationStatus -> {
                AdView adView = new AdView(context);
                banner.addView(adView);
                adView.setAdUnitId(Adsunit.BANNER);
                adView.setAdSize(AdSize.BANNER);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            });
        }
    }

    public  static  void loadInterstitialAd(Context context){
        if (Adsunit.isAds){
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, Adsunit.INTERSTITIAL, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Adsunit.mInterstitialAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            Adsunit.mInterstitialAd = interstitialAd;
                        }
                    });

        }
    }

    public static void showInterstitialAd(Activity activity,boolean isReload){
        if (Adsunit.mInterstitialAd!=null){
            Adsunit.mInterstitialAd.show(activity);
            Adsunit.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (isReload){

                        Adsunit.mInterstitialAd=null;
                        loadInterstitialAd(activity);
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }
            });
        }
    }

    public  static void loadVideoRewarded(Context context){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, Adsunit.REWARDED, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Adsunit.mRewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                Adsunit.mRewardedAd = rewardedAd;
            }
        });
    }

    public static void showVideoRewardedAd(Activity activity,boolean isReload){
        if (Adsunit.mRewardedAd!=null){
            Adsunit.mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (isReload){
                        Adsunit.mRewardedAd=null;
                        loadVideoRewarded(activity);
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Adsunit.mRewardedAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }
            });
            Adsunit.mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
        }
    }
}
