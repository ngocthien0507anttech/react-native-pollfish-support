package com.anttech.reactnativepollfishsupport;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.pollfish.classes.SurveyInfo;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishCompletedSurveyListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishReceivedSurveyListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.main.PollFish;
import com.pollfish.main.PollFish.ParamsBuilder;

import java.util.HashMap;


public class PollfishSupportModule extends ReactContextBaseJavaModule {
    private static final String TAG = "PollfishSupport";
    private EventManager eventManager;

    private ReactApplicationContext mContext;

    private boolean isInitializing = false;

    public PollfishSupportModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
        this.eventManager = new EventManager(reactContext);
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
                    .customMode(autoMode) // Set true to avoid auto-popup behavior
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
                            WritableMap map = new WritableNativeMap();
                            map.putBoolean("wasClosedByInitialize", isInitializing);
                            eventManager.send("surveyClosed", map);
                        }
                    })
                    .build());
            if (autoMode) {
                isInitializing = true;
                PollFish.hide();
                isInitializing = false;
            }
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