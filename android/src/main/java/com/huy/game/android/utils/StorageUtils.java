package com.huy.game.android.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.core.Single;

public class StorageUtils {
    private static final String STORE_NAME = "loggerStore";

    private static final Preferences pref_error = new Preferences() {
        @Nullable
        @Override
        public <T> T get(@NonNull  Key<T> key) {
            return null;
        }

        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return new HashMap<>();
        }
    };

    RxDataStore<Preferences> datastore;

    private static final StorageUtils ourInstance = new StorageUtils();

    // Private constructor because this is a singleton.
    private StorageUtils() { }

    // Get the singleton instance.
    // If the instance hasn't been initialised yet, initialise it.
    public static StorageUtils getInstance(Context context) {
        if (ourInstance.datastore == null) {
            ourInstance.datastore = new RxPreferenceDataStoreBuilder(context, STORE_NAME).build();
        }

        return ourInstance;
    }

    public boolean setStringValue(String Key, String value){
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);

        Single<Preferences> updateResult =  datastore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);

        return updateResult.blockingGet() != pref_error;
    }

    public String getStringValue(String Key) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<String> value = datastore.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem("null");
        return value.blockingGet();
    }

    public boolean setIntValue(String key, int value) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(key);

        Single<Preferences> updateResult =  datastore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);

        return updateResult.blockingGet() != pref_error;
    }

    public int getIntValue(String key) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(key);

        Single<Integer> value = datastore.data()
            .firstOrError()
            .map(prefs -> prefs.get(PREF_KEY))
            .onErrorReturnItem(-1);
        return value.blockingGet();
    }
}
