export interface UserInfo {
  id: number;
  name: string;
  surname: string;
  email: string;
  bonuses: number;
  role: {
    id: number;
    role: string;
  }
}
