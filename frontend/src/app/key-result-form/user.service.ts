import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface User {
  id: number;

  username: string;

  firstname: string;

  lastname: string;

  email: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  public getAllUsers(): Observable<User[]> {
    return this.httpClient
      .get<User[]>('api/v1/users/')
      .pipe(tap((data) => console.log(data)));
  }
}
