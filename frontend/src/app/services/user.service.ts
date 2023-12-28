import { Injectable } from '@angular/core';
import { Observable, of, take, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../shared/types/model/User';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly API_URL = 'api/v1/users';

  private _user: User | undefined;
  private users: User[] | undefined;

  constructor(private httpClient: HttpClient) {}

  public initCurrentUser(): Observable<User> {
    if (this._user) {
      return of(this._user);
    }
    return this.httpClient.get<User>(this.API_URL + '/current').pipe(tap((u) => (this._user = u)));
  }

  public getUsers(): Observable<User[]> {
    if (this.users) {
      return of(this.users).pipe(take(1));
    }
    return this.httpClient.get<User[]>(this.API_URL).pipe(tap((users) => (this.users = users)));
  }

  public reloadUsers(): Observable<User[]> {
    this.users = undefined;
    return this.getUsers();
  }

  public getCurrentUser(): User {
    if (!this._user) {
      throw new Error('user should not be undefined here');
    }
    return this._user;
  }
}
