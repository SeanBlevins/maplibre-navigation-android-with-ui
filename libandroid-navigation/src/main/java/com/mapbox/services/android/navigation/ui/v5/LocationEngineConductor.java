package com.mapbox.services.android.navigation.ui.v5;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.location.engine.LocationEngine;
import com.mapbox.mapboxsdk.location.engine.LocationEngineCallback;
import com.mapbox.mapboxsdk.location.engine.LocationEngineProvider;
import com.mapbox.mapboxsdk.location.engine.LocationEngineRequest;
import com.mapbox.mapboxsdk.location.engine.LocationEngineResult;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;

import timber.log.Timber;

public class LocationEngineConductor {
  private static final int FASTEST_INTERVAL_IN_MILLIS = 1000;
  private static final int INTERVAL_IN_MILLIS = 0;

  private LocationEngine locationEngine;
  private LocationEngineConductorListener listener;

  public LocationEngineConductor(LocationEngineConductorListener listener) {
    this.listener = listener;
  }


  public void onDestroy() {
    if(locationEngine != null) {
      locationEngine.removeLocationUpdates(locationEngineCallback);
    }
  }

  void initializeLocationEngine(Context context, LocationEngine locationEngine, boolean shouldReplayRoute) {
    initialize(context, locationEngine, shouldReplayRoute);
  }

  boolean updateSimulatedRoute(DirectionsRoute route) {
    if (locationEngine instanceof ReplayRouteLocationEngine) {
      ((ReplayRouteLocationEngine) locationEngine).assign(route);
      return true;
    }
    return false;
  }

  LocationEngine obtainLocationEngine() {
    return locationEngine;
  }

  @SuppressWarnings("MissingPermission")
  private void initialize(Context context, LocationEngine locationEngine, boolean simulateRoute) {
    if (locationEngine != null) {
      this.locationEngine = locationEngine;
    } else if (simulateRoute) {
      this.locationEngine = new ReplayRouteLocationEngine();
    } else {
      this.locationEngine = LocationEngineProvider.getBestLocationEngine(context);
    }
    LocationEngineRequest locationEngineRequest = new LocationEngineRequest.Builder(INTERVAL_IN_MILLIS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setFastestInterval(FASTEST_INTERVAL_IN_MILLIS)
            .build();

    this.locationEngine.requestLocationUpdates(locationEngineRequest, locationEngineCallback, Looper.getMainLooper());
  }

  private LocationEngineCallback<LocationEngineResult> locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {
    @Override
    public void onSuccess(LocationEngineResult result) {
      listener.onLocationUpdate(result.getLastLocation());
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
      Timber.w("Request location update failed: %s", exception.getMessage());
    }
  };
}
