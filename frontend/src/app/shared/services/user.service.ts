import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

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
  constructor() {}

  public getUsers(): Observable<User[]> {
    return of([
      {
        id: 1,
        username: 'alice',
        firstname: 'Alice',
        lastname: 'Wunderland',
        email: 'alice@wunderland.ch',
      },
      {
        id: 2,
        username: 'paco',
        firstname: 'Paco',
        lastname: 'Egiman',
        email: 'paco@egiman.ch',
      },
      {
        id: 2,
        username: 'robin',
        firstname: 'Robin',
        lastname: 'Papier',
        email: 'robin@papier.ch',
      },
    ]);
  }
}
