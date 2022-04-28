package com.mapbox.services.android.navigation.ui.v5;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

class NavigationViewSubscriber implements LifecycleObserver {

  private final LifecycleOwner lifecycleOwner;
  private final NavigationPresenter navigationPresenter;
  private final NavigationViewModel navigationViewModel;

  NavigationViewSubscriber(final LifecycleOwner owner, final NavigationPresenter navigationPresenter, final NavigationViewModel navigationViewModel) {
    this.navigationPresenter = navigationPresenter;
    this.navigationViewModel = navigationViewModel;
    this.lifecycleOwner = owner;
    lifecycleOwner.getLifecycle().addObserver(this);
  }

  void subscribe() {
    navigationViewModel.retrieveRoute().observe(lifecycleOwner, directionsRoute -> {
      if (directionsRoute != null) {
        navigationPresenter.onRouteUpdate(directionsRoute);
      }
    });

    navigationViewModel.retrieveDestination().observe(lifecycleOwner, point -> {
      if (point != null) {
        navigationPresenter.onDestinationUpdate(point);
      }
    });

    navigationViewModel.retrieveNavigationLocation().observe(lifecycleOwner, location -> {
      if (location != null) {
        navigationPresenter.onNavigationLocationUpdate(location);
      }
    });

    navigationViewModel.retrieveShouldRecordScreenshot().observe(lifecycleOwner, shouldRecordScreenshot -> {
      if (shouldRecordScreenshot != null && shouldRecordScreenshot) {
        navigationPresenter.onShouldRecordScreenshot();
      }
    });
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void unsubscribe() {
    navigationViewModel.retrieveRoute().removeObservers(lifecycleOwner);
    navigationViewModel.retrieveDestination().removeObservers(lifecycleOwner);
    navigationViewModel.retrieveNavigationLocation().removeObservers(lifecycleOwner);
    navigationViewModel.retrieveShouldRecordScreenshot().removeObservers(lifecycleOwner);
  }
}
