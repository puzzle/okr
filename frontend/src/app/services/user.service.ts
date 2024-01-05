import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../shared/types/model/User';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly API_URL = 'api/v1/users';

  private _currentUser: User | undefined;
  private users: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);
  private usersLoaded = false;

  constructor(private httpClient: HttpClient) {}

  public initCurrentUser(): Observable<User> {
    if (this._currentUser) {
      return of(this._currentUser);
    }
    return this.httpClient.get<User>(this.API_URL + '/current').pipe(tap((u) => (this._currentUser = u)));
  }

  public getUsers(): Observable<User[]> {
    if (!this.usersLoaded) {
      this.usersLoaded = true;
      this.reloadUsers().subscribe();
    }
    return this.users.asObservable();
  }

  public reloadUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.API_URL).pipe(tap((users) => this.users.next(users)));
  }

  public getCurrentUser(): User {
    if (!this._currentUser) {
      throw new Error('user should not be undefined here');
    }
    return this._currentUser;
  }

  getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(this.API_URL + '/' + id);
  }
}
