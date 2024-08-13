import { Component, OnInit } from '@angular/core';
import { LoadingService } from '@tennis-time/core';  // Adjust the path as necessary
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  loading$: Observable<boolean>;

  constructor(private loadingService: LoadingService) {
    this.loading$ = this.loadingService.loading$;
  }

  ngOnInit(): void {
    // Hide the initial inline spinner after Angular has bootstrapped
    const spinner = document.getElementById('initial-loading-spinner');
    if (spinner) {
      spinner.style.display = 'none';
    }
  }
}
