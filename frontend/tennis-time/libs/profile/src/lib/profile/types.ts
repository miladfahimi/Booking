export interface UserProfileDTO {
  id?: string;
  userId: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  address?: string;
  gender?: string;
  dateOfBirth?: string;
  profilePicture?: string;
  preferences?: string;
  dateOfBirthPersian?: string;
}

export interface UserSubscriptionDTO {
  id?: number;
  userId: string;
  subscriptionPlan?: string;
  status?: string;
  startDate?: string;
  endDate?: string;
  startDatePersian?: string;
  endDatePersian?: string;
}

export interface UserBookingHistoryDTO {
  id?: string;
  userId: string;
  reservationId?: string;
  serviceId?: string;
  bookingDate?: string;
  status?: string;
  bookingDatePersian?: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
  itArchived?: boolean;
  userNotified?: boolean;
}

export interface UserInitializationRequestDTO {
  userProfileDTO: UserProfileDTO;
  userSubscriptionDTO?: UserSubscriptionDTO;
  userBookingHistoryDTO?: UserBookingHistoryDTO[];
}

export interface UserInitializationResponseDTO {
  userId: string;
  userProfileDTO: UserProfileDTO;
  userSubscriptionDTO?: UserSubscriptionDTO;
  userBookingHistoryDTO?: UserBookingHistoryDTO[];
  userProfilesInitiated?: boolean;
}
