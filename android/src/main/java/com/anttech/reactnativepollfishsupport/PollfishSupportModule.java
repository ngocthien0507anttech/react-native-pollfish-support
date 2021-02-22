package com.anttech.reactnativepollfishsupport;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.pollfish.classes.SurveyInfo;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishCompletedSurveyListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishReceivedSurveyListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.main.PollFish;
import com.pollfish.main.PollFish.ParamsBuilder;

import java.util.HashMap;

public class PollfishSupportModule extends ReactContextBaseJavaModule {

    private static final String TAG = "PollfishSupport";
    private EventManager eventManager;

    private Intent mSurveyIntent;
    private Callback requestSurveyCallback;

    public PollfishSupportModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "PollfishSupport";
    }

    @ReactMethod
    public void initialize(final String apiKey,
        final boolean debugMode,
        final boolean autoMode,
        final boolean offerWallMode,
        final String uuid) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
            Log.d(TAG, ">> Initializing Pollfish");

            PollFish.initWith( getCurrentActivity(),
                new ParamsBuilder(apiKey)
                    .releaseMode(!debugMode) // Due to inconsitency with iOS, we negate whatever is passed in for production
                    .rewardMode(autoMode) // Set true to avoid auto-popup behavior
                    .offerWallMode(offerWallMode)
                    .requestUUID(uuid) // Unique user identifier, passed back in the callback
                    .pollfishReceivedSurveyListener(new PollfishReceivedSurveyListener() {
                        @Override
                        public void onPollfishSurveyReceived(@Nullable SurveyInfo surveyInfo) {
                            WritableMap map = new WritableNativeMap();
                            if (surveyInfo != null) {
                                map.putBoolean("playfulSurvey", surveyInfo.getSurveyClass().equals("Pollfish/Playful"));
                                map.putInt("surveyPrice", surveyInfo.getRewardValue());
                            } else {
                                map.putBoolean("playfulSurvey", false);
                                map.putInt("surveyPrice", 0);
                            }
                            eventManager.send("surveyReceived", map);
                        }
                    })
                    .pollfishSurveyNotAvailableListener(new PollfishSurveyNotAvailableListener() {
                        @Override
                        public void onPollfishSurveyNotAvailable() {
                            eventManager.send("surveyNotAvailable");
                        }
                    })
                    .pollfishCompletedSurveyListener(new PollfishCompletedSurveyListener() {
                        @Override
                        public void onPollfishSurveyCompleted(@Nullable SurveyInfo surveyInfo) {
                            WritableMap map = new WritableNativeMap();
                            if (surveyInfo != null) {
                                map.putBoolean("playfulSurvey", surveyInfo.getSurveyClass().equals("Pollfish/Playful"));
                                map.putInt("surveyPrice", surveyInfo.getRewardValue());
                            } else {
                                map.putBoolean("playfulSurvey", false);
                                map.putInt("surveyPrice", 0);
                            }
                            eventManager.send("surveyCompleted", map);
                        }
                    })
                    .pollfishUserNotEligibleListener(new PollfishUserNotEligibleListener() {
                        @Override
                        public void onUserNotEligible() {
                            eventManager.send("userNotEligible");
                        }
                    })
                    .pollfishOpenedListener(new PollfishOpenedListener() {
                        @Override
                        public void onPollfishOpened() {
                            eventManager.send("surveyOpened");
                        }
                    })
                    .pollfishClosedListener(new PollfishClosedListener() {
                        @Override
                        public void onPollfishClosed() {
                            eventManager.send("surveyClosed");
                        }
                    })
                    .build());
          }
        });



    }

    @ReactMethod
    public void destroy() {
      // no-op on Android
    }

    @ReactMethod
    public void show() {
        PollFish.show();
    }

    @ReactMethod
    public void hide() {
        PollFish.hide();
    }

    @ReactMethod
    public void surveyAvailable(Promise promise) {
        try {
            promise.resolve(PollFish.isPollfishPresent());
        } catch(Exception e) {
            promise.reject("Failed to determine if Pollfish survey is present", e);
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

}
