package com.mapbox.services.android.navigation.ui.v5;

import android.location.Location;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCamera;

class NavigationPresenter {

  private NavigationContract.View view;
  private boolean resumeState;

  NavigationPresenter(NavigationContract.View view) {
    this.view = view;
  }

  void updateResumeState(boolean resumeState) {
    this.resumeState = resumeState;
  }

  void onRecenterClick() {
    view.setSummaryBehaviorHideable(false);
    view.setSummaryBehaviorState(BottomSheetBehavior.STATE_EXPANDED);
    view.updateWaynameVisibility(true);
    view.resetCameraPosition();
    view.hideRecenterBtn();
  }

  void onCameraTrackingDismissed() {
    if (!view.isSummaryBottomSheetHidden()) {
      view.setSummaryBehaviorHideable(true);
      view.setSummaryBehaviorState(BottomSheetBehavior.STATE_HIDDEN);
      view.updateCameraTrackingMode(NavigationCamera.NAVIGATION_TRACKING_MODE_NONE);
      view.updateWaynameVisibility(false);
    }
  }

  void onSummaryBottomSheetHidden() {
    if (view.isSummaryBottomSheetHidden()) {
      view.showRecenterBtn();
    }
  }

  void onRouteUpdate(DirectionsRoute directionsRoute) {
    view.drawRoute(directionsRoute);
    if (!resumeState) {
      view.updateWaynameVisibility(true);
      view.startCamera(directionsRoute);
    }
  }

  void onDestinationUpdate(Point point) {
    view.addMarker(point);
  }

  void onShouldRecordScreenshot() {
    view.takeScreenshot();
  }

  void onNavigationLocationUpdate(Location location) {
    if (resumeState && !view.isRecenterButtonVisible()) {
      view.resumeCamera(location);
      resumeState = false;
    }
    view.updateNavigationMap(location);
  }

  void onRouteOverviewClick() {
    view.updateWaynameVisibility(false);
    view.updateCameraRouteOverview();
    view.showRecenterBtn();
  }
}
