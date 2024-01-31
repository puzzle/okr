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

  public getOrInitCurrentUser(): Observable<User> {
    if (this._currentUser) {
      return of(this._currentUser);
    }
    return this.reloadCurrentUser();
  }

  public reloadCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(this.API_URL + '/current').pipe(tap((u) => (this._currentUser = u)));
  }

  public getCurrentUser(): User {
    if (!this._currentUser) {
      throw new Error('user should not be undefined here');
    }
    return this._currentUser;
  }

  public getUsers(): Observable<User[]> {
    if (!this.usersLoaded) {
      this.usersLoaded = true;
      this.reloadUsers();
    }
    return this.users.asObservable();
  }

  public reloadUsers(): void {
    this.httpClient.get<User[]>(this.API_URL).subscribe((users) => this.users.next(users));
  }

  getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(this.API_URL + '/' + id);
  }

  setIsOkrChampion(user: User, isOkrChampion: boolean) {
    return this.httpClient.put(`${this.API_URL}/${user.id}/isokrchampion/${isOkrChampion}`, {});
  }
}
