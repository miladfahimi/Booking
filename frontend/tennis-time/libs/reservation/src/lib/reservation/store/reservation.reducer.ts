import { createReducer, on } from '@ngrx/store';
import * as ReservationActions from './reservation.actions';
import { LoadingStatus, ProviderDTO, ReservationStatus, ServiceDTO, SlotBasketState, SlotDTO } from '../types';
import { ReservationBasketItem } from '../types/reservation-basket.types';
import { PaymentInitiationResult } from '../types/reservation-payment.types';
import { SlotStatusNotification } from '../realtime/slot-status-notification';

const EMPTY_BASKET_STATE: SlotBasketState = {
  inBasketByCurrentUser: false,
  inBasketByOtherUsers: false,
  totalBasketUsers: 0
};

function normalizeSlot(slot: SlotDTO): SlotDTO {
  return {
    ...slot,
    basketState: slot.basketState ? { ...slot.basketState } : { ...EMPTY_BASKET_STATE }
  };
}

function basketStatesEqual(a: SlotBasketState, b: SlotBasketState): boolean {
  return (
    a.inBasketByCurrentUser === b.inBasketByCurrentUser &&
    a.inBasketByOtherUsers === b.inBasketByOtherUsers &&
    a.totalBasketUsers === b.totalBasketUsers
  );
}

function updateBasketStateWithNotification(
  state: SlotBasketState,
  notification: SlotStatusNotification
): SlotBasketState {
  const payload = notification.basketState;
  if (!payload) {
    return state;
  }
  return {
    inBasketByCurrentUser: payload.inBasketByCurrentUser ?? state.inBasketByCurrentUser,
    inBasketByOtherUsers: payload.inBasketByOtherUsers ?? state.inBasketByOtherUsers,
    totalBasketUsers: payload.totalBasketUsers ?? state.totalBasketUsers
  };
}

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
      acc[service.id] = (service.slots ?? []).map(normalizeSlot);
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
    const inBasketByOtherUsers = notification.basketState?.inBasketByOtherUsers ?? false;
    const isForeignBasketUpdate =
      inBasketByOtherUsers &&
      (!!notification.userId && !!currentUserId ? notification.userId !== currentUserId : true);

    let foreignHoldWarning = state.foreignHoldWarning;
    const matchesForeignWarning =
      !!foreignHoldWarning &&
      foreignHoldWarning.serviceId === notification.serviceId &&
      (foreignHoldWarning.slotId === notification.slotId ||
        foreignHoldWarning.compositeSlotId === notification.compositeSlotId);

    const remainingHolds = (notification.basketState?.totalBasketUsers ?? 0) > 0;
    if (matchesForeignWarning && !remainingHolds) {
      foreignHoldWarning = null;
    }

    if (!slots || !slots.length) {
      return {
        ...state,
        foreignHoldWarning,
      };
    }

    let changed = false;
    const updatedSlots = slots.map(slot => {
      if (slot.slotId === notification.slotId || slot.slotId === notification.compositeSlotId) {
        let nextSlot = slot;
        const shouldUpdateStatus = notification.status !== ReservationStatus.IN_BASKET;
        if (shouldUpdateStatus && slot.status !== notification.status) {
          nextSlot = { ...nextSlot, status: notification.status };
          changed = true;
        }
        const currentBasketState = nextSlot.basketState ?? { ...EMPTY_BASKET_STATE };
        const updatedBasketState = updateBasketStateWithNotification(currentBasketState, notification);
        if (!basketStatesEqual(currentBasketState, updatedBasketState)) {
          nextSlot = { ...nextSlot, basketState: updatedBasketState };
          changed = true;
        }
        return nextSlot;
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