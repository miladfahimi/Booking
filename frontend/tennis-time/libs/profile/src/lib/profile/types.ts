export interface UserProfileDTO {
  id: number;
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  address: string;
  dateOfBirth: string; // Format: YYYY-MM-DD
  profilePicture: string;
  preferences: string;
  dateOfBirthPersian: string; // Format: YYYY-MM-DD in Persian calendar
}

export interface LoadingStatus {
  loaded: boolean;
  loading: boolean;
}
