import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { StoreRouterConnectingModule } from '@ngrx/router-store';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';


// Import your application's reducers and metaReducers
import { reducers, metaReducers } from './reducers';  // Assume you have a central place for reducers

// Import the CoreModule from the Nx libs folder
import { CoreModule } from '@tennis-time/core';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,  // Handles routing and lazy loading
    CoreModule,  // CoreModule imported from the Nx library

    // NgRx Store Setup
    StoreModule.forRoot(reducers, { metaReducers }),  // Global store configuration
    EffectsModule.forRoot([]),  // Register global effects, if any
    StoreDevtoolsModule.instrument({
      maxAge: 25,  // Retain last 25 states
      logOnly: !isDevMode(),  // Restrict extension to log-only mode in production
    }),
    StoreRouterConnectingModule.forRoot()  // Sync Angular Router with NgRx store
  ],
  providers:[
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
