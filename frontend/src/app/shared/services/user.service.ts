import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Team } from './team.service';
import { HttpClient } from '@angular/common/http';

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

  public getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>('api/v1/users');
  }
}
