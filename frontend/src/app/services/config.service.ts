import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';
import { ClientConfig } from '../shared/types/model/client-config';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private httpClient = inject(HttpClient);

  public config$: Observable<ClientConfig>;

  constructor() {
    this.config$ = this.httpClient.get<ClientConfig>('/config')
      .pipe(shareReplay());
  }
}
