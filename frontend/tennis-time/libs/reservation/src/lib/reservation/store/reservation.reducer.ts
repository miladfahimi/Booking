import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, ProviderDTO, ReservationStatus, ServiceDTO, SlotDTO } from '../types';
import { ReservationBasketItem } from '../types/reservation-basket.types';
import { PaymentInitiationResult } from '../types/reservation-payment.types';
import { SlotStatusNotification } from '../realtime/slot-status-notification';

export interface ReservationState {
  slotsByService: Record<string, SlotDTO[]>;
  providers: ProviderDTO[] | null;
  loadingStatus: LoadingStatus;
  providersLoadingStatus: LoadingStatus;
  selectedDate: string | null;
  basket: ReservationBasketItem[];
  basketLoading: boolean;
  checkoutStatus: LoadingStatus;
  paymentResult: PaymentInitiationResult | null;
  paymentCompletionStatus: LoadingStatus;
  paymentCompletionError: string | null;
  error: any;
  foreignHoldWarning: SlotStatusNotification | null;
}

export const initialState: ReservationState = {
  slotsByService: {},
  providers: null,
  loadingStatus: { loading: false, loaded: false },
  providersLoadingStatus: { loading: false, loaded: false },
  selectedDate: null,
  basket: [],
  basketLoading: false,
  checkoutStatus: { loading: false, loaded: false },
  paymentResult: null,
  paymentCompletionStatus: { loading: false, loaded: false },
  paymentCompletionError: null,
  error: null,
  foreignHoldWarning: null,
};


export const reservationReducer = createReducer(
  initialState,

  // For loading slots
  on(ReservationActions.loadSlots, (state, { date }) => ({
    ...state,
    loadingStatus: { loading: true, loaded: false },
    selectedDate: date,
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
  })),

  on(ReservationActions.slotStatusUpdated, (state, { notification, currentUserId }) => {
    if (
      state.selectedDate &&
      notification.reservationDate &&
      notification.reservationDate !== state.selectedDate
    ) {
      return state;
    }
    const slots = state.slotsByService[notification.serviceId];
    const isForeignBasketUpdate =
      notification.status === ReservationStatus.IN_BASKET &&
      (!!notification.userId && !!currentUserId ? notification.userId !== currentUserId : true);

    let foreignHoldWarning = state.foreignHoldWarning;
    const matchesForeignWarning =
      !!foreignHoldWarning &&
      foreignHoldWarning.serviceId === notification.serviceId &&
      (foreignHoldWarning.slotId === notification.slotId ||
        foreignHoldWarning.compositeSlotId === notification.compositeSlotId);

    if (matchesForeignWarning && notification.status !== ReservationStatus.IN_BASKET) {
      foreignHoldWarning = null;
    }

    if (!slots || !slots.length) {
      return {
        ...state,
        foreignHoldWarning,
      };
    }

    if (isForeignBasketUpdate) {
      return {
        ...state,
        foreignHoldWarning,
      };
    }
    let changed = false;
    const updatedSlots = slots.map(slot => {
      if (slot.slotId === notification.slotId || slot.slotId === notification.compositeSlotId) {
        if (slot.status === notification.status) {
          return slot;
        }
        changed = true;
        return { ...slot, status: notification.status };
      }
      return slot;
    });
    if (!changed) {
      return {
        ...state,
        foreignHoldWarning,
      };
    }
    return {
      ...state,
      slotsByService: {
        ...state.slotsByService,
        [notification.serviceId]: updatedSlots,
      },
      foreignHoldWarning,
    };
  }),

  on(ReservationActions.slotHeldByAnotherUser, (state, { notification }) => ({
    ...state,
    foreignHoldWarning: notification,
  })),

  on(ReservationActions.dismissSlotHoldWarning, (state) => ({
    ...state,
    foreignHoldWarning: null,
  }))
);