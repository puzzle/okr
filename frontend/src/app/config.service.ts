import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';
import { ClientConfig } from './shared/types/model/ClientConfig';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  public config$: Observable<ClientConfig>;

  constructor(private httpClient: HttpClient) {
    this.config$ = this.httpClient.get<ClientConfig>('/config').pipe(shareReplay());
  }
}
