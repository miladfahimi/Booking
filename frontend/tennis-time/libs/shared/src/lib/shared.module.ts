import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpinnerComponent } from './shared/spinner/spinner.component';
import { FaDigitsPipe } from './shared/farsi-digits/fa-digits.pipe';
import { AuthFacadeService } from './shared/auth/auth-facade-service';

@NgModule({
  declarations: [
    SpinnerComponent,
    FaDigitsPipe
  ],
  imports: [CommonModule],
  providers: [AuthFacadeService],
  exports: [
    SpinnerComponent,
    FaDigitsPipe
  ]
})
export class SharedModule { }
