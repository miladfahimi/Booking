export interface UserReservationDTO {
  id: string;                          // Unique identifier for the reservation
  userId: string;                      // ID of the user making the reservation
  serviceId: string;                   // ID of the service being reserved
  providerId: string;                  // ID of the service provider
  status: string;                      // Status of the reservation (e.g., CONFIRMED, PENDING, CANCELED)
  reservationDate: string;             // Date of the reservation in Gregorian calendar (Format: YYYY-MM-DD)
  reservationDatePersian: string;      // Date of the reservation in Persian calendar (Format: YYYY-MM-DD)
  startTime: string;                   // Start time of the reservation (Format: HH:MM:SS)
  endTime: string;                     // End time of the reservation (Format: HH:MM:SS)
  email: string;                       // User's email
  firstName: string;                   // User's first name
  lastName: string;                    // User's last name
  phoneNumber: string;                 // User's phone number
  address: string;                     // User's address
  preferences: string;                 // User's preferences or special requirements
  reservationPicture: string;          // URL or path to an image associated with the reservation
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
