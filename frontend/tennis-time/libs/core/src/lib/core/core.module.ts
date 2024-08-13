import { NgModule, Optional, SkipSelf } from '@angular/core';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { CoreAuthService } from './services/core-auth.service';  // Adjust the path as necessary
import { AuthGuard } from './guards/auth.guard';  // Adjust the path as necessary
import { TokenInterceptor } from './interceptors/token.interceptor';  // Adjust the path as necessary
import { LoadingService } from './services/loading.service';  // Import the LoadingService
import { LoadingInterceptor } from './interceptors/loading.intercepter';  // Import the LoadingInterceptor

@NgModule({
  providers: [
    CoreAuthService,
    AuthGuard,
    LoadingService,  // Add LoadingService here
    provideHttpClient(withInterceptorsFromDi()),

    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,  // Register the LoadingInterceptor
      multi: true
    }
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it in the AppModule only.');
    }
  }
}
