import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, ServiceDTO, ProviderDTO, SlotDTO } from '../types';

// export interface ReservationState {
//   service: ServiceDTO | null;
//   providers: ProviderDTO[] | null;
//   loadingStatus: LoadingStatus;
//   providersLoadingStatus: LoadingStatus;
//   error: any;
// }

// export const initialState: ReservationState = {
//   service: null,
//   providers: null,
//   loadingStatus: {loading: false, loaded: false},
//   providersLoadingStatus: {loading: false, loaded: false},
//   error: null,
// };

export interface ReservationState {
  slotsByService: Record<string, SlotDTO[]>;
  providers: ProviderDTO[] | null;
  loadingStatus: LoadingStatus;
  providersLoadingStatus: LoadingStatus;
  error: any;
}

export const initialState: ReservationState = {
  slotsByService: {},
  providers: null,
  loadingStatus: { loading: false, loaded: false },
  providersLoadingStatus: { loading: false, loaded: false },
  error: null,
};

export const reservationReducer = createReducer(
  initialState,

  // For loading slots
  on(ReservationActions.loadSlots, (state) => ({
    ...state,
    loadingStatus: { loading: true, loaded: false },
    error: null,
  })),
  // on(ReservationActions.loadSlotsSuccess, (state, { service }) => ({
  //   ...state,
  //   service,  // Store the entire ServiceDTO object
  //   loadingStatus: { loading: false, loaded: true },
  //   error: null,
  // })),
  on(ReservationActions.loadSlotsSuccess, (state, { services }) => ({
    ...state,
    slotsByService: services.reduce<Record<string, SlotDTO[]>>((acc, service) => {
      acc[service.id] = service.slots ?? [];
      return acc;
    }, {}),
    loadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ReservationActions.loadSlotsFailure, (state, { error }) => ({
    ...state,
    error,
    loadingStatus: { loading: false, loaded: true },
  })),

  // For loading providers with services
  on(ReservationActions.loadProvidersWithServices, (state) => ({
    ...state,
    providersLoadingStatus: { loading: true, loaded: false },
    error: null,
  })),
  on(ReservationActions.loadProvidersWithServicesSuccess, (state, { providers }) => ({
    ...state,
    providers,  // Store the list of ProviderDTO objects
    providersLoadingStatus: { loading: false, loaded: true },
    error: null,
  })),
  on(ReservationActions.loadProvidersWithServicesFailure, (state, { error }) => ({
    ...state,
    error,
    providersLoadingStatus: { loading: false, loaded: true },
  }))
);
