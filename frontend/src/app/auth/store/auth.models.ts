export interface SignUpReq {
  username: string;
  email: string;
  phone: string;
  password: string;
  role: 'USER' | 'ADMIN'; // Assuming roles are either 'USER' or 'ADMIN'
}

export interface LoadingStatus {
  loaded: boolean;
  loading: boolean;
}

