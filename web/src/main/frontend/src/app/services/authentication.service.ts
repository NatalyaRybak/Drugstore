import {User} from "../models/user";

﻿import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';

import {environment} from "../../environments/environment";
import {Role} from "../models/roles";
import {UserInfo} from "../models/userInfo";
import {EventEmitter} from '@angular/core';


@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private authDataSubject: BehaviorSubject<string>;
  private currentUserSubject: BehaviorSubject<UserInfo>;
  public currentUser: Observable<UserInfo>;
  public authData: Observable<string>;
  bonusesToUse : number;
  onBonusesClick: EventEmitter<number> = new EventEmitter();

  authenticated = false;

  constructor(private http: HttpClient) {
    this.authDataSubject = new BehaviorSubject<string>(localStorage.getItem('authData'));
    this.currentUserSubject = new BehaviorSubject<UserInfo>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
    this.authData = this.authDataSubject.asObservable();
  }

  public isEmployee(): boolean {
    return this.currentUserValue.role.role == Role.EMPLOYEE;
  }

  public isAuthed() {
    return !!this.currentUserSubject.value;
  }

  public get currentUserValue(): UserInfo {
    return this.currentUserSubject.value;
  }

  public get currentAuthDataValue(): string {
    return this.authDataSubject.value;
  }

  login(username: string, password: string) {
    return this.loginRequest(username, password)
      .pipe(map((user: User) => {
        console.log(user);
        this.setToken(user.token);
        this.authDataSubject.next(user.token);
        return user.token;
      }))
      .pipe(switchMap(user => this.getCurrUserInfo()))
      .pipe(map((user: UserInfo) => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        this.authenticated = true;
        return user;
      }));
  }

  register(name: string, surname: string, email: string, password: string) {
    return this.registerRequest(name, surname, email, password)
      .pipe(map((user: User) => {
        this.setToken(user.token);
        this.authDataSubject.next(user.token);
        return user.token;
      }))
      .pipe(switchMap(user => this.getCurrUserInfo()))
      .pipe(map((user: UserInfo) => {
        this.setUser(user);
        this.currentUserSubject.next(user);
        this.authenticated = true;
        return user;
      }));
  }

  logout() {
    this.authenticated = false;
    console.log("logout");
    this.removeCurrUser();
    this.removeToken();
    this.currentUserSubject.next(null);
    this.authDataSubject.next(null);
    this.currentUserSubject.next(null);
  }

  getUserMainPageUrl() {
    switch (this.currentUserValue.role.role) {
      case Role.USER:
        return '/profile';
      case Role.EMPLOYEE:
        return '/employee';
      default:
        return '/';
    }
  }

  private loginRequest(username: string, password: string) {
    return this.http.post(`${environment.apiUrl}/authenticate`, {username: username, password: password});
  }

  private registerRequest(name: string, surname: string, email: string, password: string) {
    return this.http.post(
      `${environment.apiUrl}/user/register`,
      {name: name, surname: surname, email: email, password: password}
      );
  }

  public resetPassword(email: string) {
    return this.http.post(
      `${environment.apiUrl}/user/reset-password`,
      email
    );
  }
  

  public checkResetToken(token: string) {
    const params = new HttpParams()
      .set('token', token);

    return this.http.get(
      `${environment.apiUrl}/user/password-reset-token/validate`,
      {params}
    );
  }

  public updatePassword(password: string, token: string) {
    return this.http.put(
      `${environment.apiUrl}/user/change-password`,
      {
        token: token,
        password: password
      }
    );
  }

  public removeCurrUser(){
    localStorage.removeItem('currentUser');
  }

  public updateUser(usedBonuses: number) {
    let user = this.currentUserValue;
    user.bonuses = user.bonuses - usedBonuses;
    this.setUser(user);
  }

  private getCurrUserInfo() {
    return this.http.get(`${environment.apiUrl}/user`);
  }

  private setUser(user) {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  private setToken(accessToken: string): void {
    localStorage.setItem('authData', accessToken);
  }

  public getToken(): string {
    return localStorage.getItem('authData');
  }

  public removeToken(): void {
    localStorage.removeItem('authData');
  }
}
