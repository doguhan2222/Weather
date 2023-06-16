package com.doguhanay.myweather.data

import android.content.SharedPreferences
import com.google.gson.annotations.SerializedName
import javax.inject.Inject


data class HourlyUnits (

  @SerializedName("time"           ) var time          : String? = null,
  @SerializedName("temperature_2m" ) var temperature2m : String? = null,
  @SerializedName("weathercode"    ) var weathercode   : String? = null

)

/*
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

  fun saveString(key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
  }

  fun getString(key: String, defaultValue: String): String {
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
  }

  // Add other methods for different data types as needed
}*/



/*
package com.doguhanay.myweather.data;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://dagger.dev"
)
@SuppressWarnings({
  "unchecked",
  "rawtypes"
})
public final class SharedPreferencesManager_Factory implements Factory<SharedPreferencesManager> {
  private final Provider<SharedPreferences> sharedPreferencesProvider;

  public SharedPreferencesManager_Factory(Provider<SharedPreferences> sharedPreferencesProvider) {
    this.sharedPreferencesProvider = sharedPreferencesProvider;
  }

  @Override
  public SharedPreferencesManager get() {
    return newInstance(sharedPreferencesProvider.get());
  }

  public static SharedPreferencesManager_Factory create(
          Provider<SharedPreferences> sharedPreferencesProvider) {
    return new SharedPreferencesManager_Factory(sharedPreferencesProvider);
  }

  public static SharedPreferencesManager newInstance(SharedPreferences sharedPreferences) {
    return new SharedPreferencesManager(sharedPreferences);
  }
}
*/
