package com.mapbox.services.android.navigation.ui.v5;

import android.location.Location;

public interface LocationEngineConductorListener {

  void onLocationUpdate(Location location);
}
