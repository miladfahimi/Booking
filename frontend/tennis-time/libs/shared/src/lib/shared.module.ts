import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpinnerComponent } from './shared/spinner/spinner.component';
import { FaDigitsPipe } from './shared/farsi-digits/fa-digits.pipe';
import { JalaliDatePipe } from './shared/jalali-date/jalali-date.pipe';
import { AuthFacadeService } from './shared/auth/auth-facade-service';

@NgModule({
  declarations: [
    SpinnerComponent,
    FaDigitsPipe,
    JalaliDatePipe
  ],
  imports: [CommonModule],
  providers: [AuthFacadeService],
  exports: [
    SpinnerComponent,
    FaDigitsPipe,
    JalaliDatePipe
  ]
})
export class SharedModule { }