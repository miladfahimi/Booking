import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './reducers';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { StoreRouterConnectingModule } from '@ngrx/router-store';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,  // Routing module handles lazy loading
    CoreModule, StoreModule.forRoot(reducers, {
      metaReducers
    }), EffectsModule.forRoot([]), StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: !isDevMode() }), StoreRouterConnectingModule.forRoot()         // CoreModule can include services and common components
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
