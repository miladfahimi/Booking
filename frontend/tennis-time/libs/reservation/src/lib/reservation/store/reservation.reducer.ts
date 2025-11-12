import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, ServiceDTO, ProviderDTO, SlotDTO } from '../types';
import { ReservationBasketItem } from '../types/reservation-basket.types';
import { PaymentInitiationResult } from '../types/reservation-payment.types';

export interface ReservationState {
  slotsByService: Record<string, SlotDTO[]>;
  providers: ProviderDTO[] | null;
  loadingStatus: LoadingStatus;
  providersLoadingStatus: LoadingStatus;
  basket: ReservationBasketItem[];
  basketLoading: boolean;
  checkoutStatus: LoadingStatus;
  paymentResult: PaymentInitiationResult | null;
  paymentCompletionStatus: LoadingStatus;
  paymentCompletionError: string | null;
  error: any;
}

export const initialState: ReservationState = {
  slotsByService: {},
  providers: null,
  loadingStatus: { loading: false, loaded: false },
  providersLoadingStatus: { loading: false, loaded: false },
  basket: [],
  basketLoading: false,
  checkoutStatus: { loading: false, loaded: false },
  paymentResult: null,
  paymentCompletionStatus: { loading: false, loaded: false },
  paymentCompletionError: null,
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
  })),

  on(ReservationActions.loadBasket, (state) => ({
    ...state,
    basketLoading: true,
    error: null,
  })),

  on(ReservationActions.addSlotToBasket, (state) => ({
    ...state,
    basketLoading: true,
    error: null,
  })),

  on(ReservationActions.removeSlotFromBasket, (state) => ({
    ...state,
    basketLoading: true,
    error: null,
  })),

  on(ReservationActions.loadBasketSuccess, (state, { items }) => ({
    ...state,
    basket: items,
    basketLoading: false,
    checkoutStatus: { loading: false, loaded: false },
    paymentResult: null,
    paymentCompletionStatus: { loading: false, loaded: false },
    paymentCompletionError: null,
  })),

  on(ReservationActions.loadBasketFailure, (state, { error }) => ({
    ...state,
    basketLoading: false,
    error,
  })),

  on(ReservationActions.addSlotToBasketSuccess, (state, { item }) => {
    const withoutExisting = state.basket.filter(existing => existing.slotId !== item.slotId);

    return {
      ...state,
      basket: [...withoutExisting, item],
      basketLoading: false,
      paymentResult: null,
      checkoutStatus: { loading: false, loaded: false },
    };
  }),

  on(ReservationActions.addSlotToBasketFailure, (state, { error }) => ({
    ...state,
    basketLoading: false,
    error,
  })),

  on(ReservationActions.removeSlotFromBasketSuccess, (state, { slotId }) => ({
    ...state,
    basket: state.basket.filter(item => item.slotId !== slotId),
    basketLoading: false,
  })),

  on(ReservationActions.removeSlotFromBasketFailure, (state, { error }) => ({
    ...state,
    basketLoading: false,
    error,
  })),

  on(ReservationActions.clearBasket, (state) => ({
    ...state,
    basketLoading: true,
  })),

  on(ReservationActions.clearBasketSuccess, (state) => ({
    ...state,
    basket: [],
    basketLoading: false,
    checkoutStatus: { loading: false, loaded: false },
  })),

  on(ReservationActions.clearBasketFailure, (state, { error }) => ({
    ...state,
    basketLoading: false,
    error,
  })),

  on(ReservationActions.checkoutBasket, (state) => ({
    ...state,
    checkoutStatus: { loading: true, loaded: false },
    error: null,
  })),

  on(ReservationActions.checkoutBasketSuccess, (state, { reservations, payment }) => ({
    ...state,
    basket: [],
    basketLoading: false,
    checkoutStatus: { loading: false, loaded: true },
    paymentResult: payment,
    paymentCompletionStatus: { loading: false, loaded: false },
    paymentCompletionError: null,
    error: null,
  })),

  on(ReservationActions.checkoutBasketFailure, (state, { error }) => ({
    ...state,
    checkoutStatus: { loading: false, loaded: false },
    error,
  })),

  on(ReservationActions.completeMockPayment, (state) => ({
    ...state,
    paymentCompletionStatus: { loading: true, loaded: false },
    paymentCompletionError: null,
  })),

  on(ReservationActions.completeMockPaymentSuccess, (state) => ({
    ...state,
    paymentCompletionStatus: { loading: false, loaded: true },
    paymentCompletionError: null,
    paymentResult: null,
  })),

  on(ReservationActions.completeMockPaymentFailure, (state, { error }) => ({
    ...state,
    paymentCompletionStatus: { loading: false, loaded: false },
    paymentCompletionError: typeof error === 'string' ? error : 'PAYMENT_FAILED',
  }))
);