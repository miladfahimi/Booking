import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SpinnerComponent } from './shared/spinner/spinner.component';
import { FaDigitsPipe } from './shared/farsi-digits/fa-digits.pipe';

@NgModule({
  declarations: [
    SpinnerComponent,
    FaDigitsPipe],
  imports: [CommonModule],
  exports: [
    SpinnerComponent,
    FaDigitsPipe]
})
export class SharedModule { }
