import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { StoreRouterConnectingModule } from '@ngrx/router-store';

// Import your application's reducers and metaReducers
import { reducers, metaReducers } from './reducers';  // Assume you have a central place for reducers

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,  // Handles routing and lazy loading
    CoreModule,  // CoreModule can include singleton services and common components

    // NgRx Store Setup
    StoreModule.forRoot(reducers, { metaReducers }),  // Global store configuration
    EffectsModule.forRoot([]),  // Register global effects, if any
    StoreDevtoolsModule.instrument({
      maxAge: 25,  // Retain last 25 states
      logOnly: !isDevMode(),  // Restrict extension to log-only mode in production
    }),
    StoreRouterConnectingModule.forRoot()  // Sync Angular Router with NgRx store
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
