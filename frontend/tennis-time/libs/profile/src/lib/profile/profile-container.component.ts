import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'tt-profile-container',
  templateUrl: './profile-container.component.html',
  styleUrls: ['./profile-container.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProfileContainerComponent { }
