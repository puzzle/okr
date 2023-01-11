import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SvgService {
  constructor(private httpClient: HttpClient) {}

  public getSvg(svgName: string): Observable<string> {
    return this.httpClient.get(`../../assets/images/${svgName}`, {
      responseType: 'text',
    });
  }
}
