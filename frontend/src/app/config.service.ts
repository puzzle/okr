import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  public config$: Observable<any>;

  constructor(private httpClient: HttpClient) {
    this.config$ = this.httpClient.get('/config').pipe(shareReplay());
  }
}
