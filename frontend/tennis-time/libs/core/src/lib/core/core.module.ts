import { NgModule, Optional, SkipSelf } from '@angular/core';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import { CoreAuthService } from './services/core-auth.service';  // Adjust the path as necessary
import { AuthGuard } from './guards/auth.guard';  // Adjust the path as necessary
import { TokenInterceptor } from './interceptors/token.interceptor';  // Adjust the path as necessary


@NgModule({
  providers: [
    CoreAuthService,
    provideHttpClient(withInterceptorsFromDi()),

    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
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
