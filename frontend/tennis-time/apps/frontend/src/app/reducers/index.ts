import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import { environment } from '../../environments/environment';
import { routerReducer } from '@ngrx/router-store';

export interface AppState {
  router: any;
  // Add more state slices here as you define them
}

export const reducers: ActionReducerMap<AppState> = {
  router: routerReducer,
  // Add more reducers here as you define them
};

export const metaReducers: MetaReducer<AppState>[] = !environment.production ? [] : [];
