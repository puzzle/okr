import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, tap} from "rxjs";

export interface Team {
  id: number;
  name: string;
}

export interface OkrCycle {
  cycle: string;
}

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private httpClient: HttpClient) { }

  public getTeams(): Observable<Team[]> {
    return this.httpClient.get<Team[]>('http://localhost:4200/api/v1/teams').pipe(
      tap(data => console.log(data))
    );
  }

  public getQuarter(date = new Date()) {
    let cycleList: OkrCycle[] = [];

    let currentQuarter : number = Math.floor(date.getMonth() / 3 + 1);
    let currentYear : number = date.getFullYear();
    const currentCycle : OkrCycle = {
      cycle: (currentYear.toString().slice(-2) + "-" + currentQuarter.toString())
    }
    cycleList!.push(currentCycle);

    let pastQuarter: number;
    let year : number = currentYear;
    for (let i = 0; i < 4; i++) {
      if (currentQuarter == 1) {
        pastQuarter = 4;
        year -= 1;
      } else {
        pastQuarter = currentQuarter - 1;
      }
      currentQuarter = pastQuarter;
      const pastCycle : OkrCycle = {
        cycle: (year.toString().slice(-2) + "-" + pastQuarter.toString())
      }
      cycleList.push(pastCycle);
    }

    if (currentQuarter == 4) {
      currentYear += 1;
      currentQuarter = 1;
    } else {
      currentQuarter += 1;
    }
    const futureCycle : OkrCycle = {
      cycle: (currentYear.toString().slice(-2) + "-" + currentQuarter.toString())
    }
    cycleList.push(futureCycle);

    return cycleList!;
  }
}
