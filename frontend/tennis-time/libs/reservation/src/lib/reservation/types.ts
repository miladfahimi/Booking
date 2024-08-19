export interface UserReservationDTO {
  id: number;
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  address: string;
  dateOfBirth: string; // Format: YYYY-MM-DD
  reservationPicture: string;
  preferences: string;
  dateOfBirthPersian: string; // Format: YYYY-MM-DD in Persian calendar
}

export interface LoadingStatus {
  loaded: boolean;
  loading: boolean;
}

export interface Day {
  label: string;
  date: Date;
  jalaliDate: string;
  selected: boolean;
  isToday: boolean;
  areOtherMonths: boolean;
}
