import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Styles } from './shared/types/model/Styles';

@Injectable({
  providedIn: 'root',
})
export class StyleService {
  private _bodyClass: string | undefined;

  constructor(private httpClient: HttpClient) {}

  public loadAndSetStyles() {
    this.httpClient.get<Styles>('api/v2/styles').subscribe((styles) => {
      this._bodyClass = styles.styleClass;
    });
  }

  get bodyClass(): string | undefined {
    return this._bodyClass;
  }
}
