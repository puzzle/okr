import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { getNumberOrNull } from '../common';
import { first, Observable, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private currentUrl: string;
  private previousUrl?: string;

  getPreviousUrl(): string | undefined {
    return this.previousUrl;
  }

  setPreviousUrl(url: string) {
    this.previousUrl = url;
  }

  constructor(private location: Location, private route: ActivatedRoute, private router: Router) {
    this.currentUrl = this.router.url;
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.previousUrl = this.currentUrl;
        this.currentUrl = event.url;
      }
    });
  }

  public addToSelectedObjectives(objectiveId: number) {
    this.route.queryParams.pipe(first()).subscribe((params) => {
      let selectedObjectives: number[] = params['objectives']?.split(',') ?? [];
      selectedObjectives.push(objectiveId);
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: {
          objectives: selectedObjectives.toString(),
          keyresults: params['keyresults'],
          quarterFilter: params['quarterFilter'],
          teamFilter: params['teamFilter'],
        },
      });
    });
  }

  public removeFromSelectedObjectives(objectiveId: number) {
    this.route.queryParams.pipe(first()).subscribe((params) => {
      const selectedObjectives = (params['objectives']?.split(',') ?? []).filter(
        (item: string | null | undefined) => getNumberOrNull(item) !== objectiveId
      );
      const queryParams = {
        objectives: selectedObjectives.join(','),
        keyresults: params['keyresults'],
        quarterFilter: params['quarterFilter'],
        teamFilter: params['teamFilter'],
      };
      const navigationParams = {
        relativeTo: this.route,
        queryParams,
      };
      if (selectedObjectives.length === 0) {
        this.router.navigate(['/'], {
          queryParams: {
            keyresults: params['keyresults'],
            teamFilter: params['teamFilter'],
            quarterFilter: params['quarterFilter'],
          },
        });
      } else {
        this.router.navigate([], navigationParams);
      }
    });
  }

  public addToSelectedKeyresults(keyResultId: number) {
    this.route.queryParams.pipe(first()).subscribe((params) => {
      const selectedKeyResults = (params['keyresults']?.split(',') ?? []).concat(keyResultId);
      const queryParams = {
        objectives: params['objectives'],
        keyresults: selectedKeyResults.join(','),
        quarterFilter: params['quarterFilter'],
        teamFilter: params['teamFilter'],
      };
      const navigationParams = {
        relativeTo: this.route,
        queryParams,
      };
      this.router.navigate([], navigationParams);
    });
  }

  public removeFromSelectedKeyresult(keyResultId: number) {
    this.route.queryParams.pipe(first()).subscribe((params) => {
      let selectedKeyResults: string[] = params['keyresults'] ? params['keyresults'].split(',') : [];
      selectedKeyResults = selectedKeyResults.filter((item) => getNumberOrNull(item) !== keyResultId);
      const queryParams = {
        objectives: params['objectives'],
        keyresults: selectedKeyResults.join(','),
        quarterFilter: params['quarterFilter'],
        teamFilter: params['teamFilter'],
      };
      const navigationParams = {
        relativeTo: this.route,
        queryParams,
      };
      if (selectedKeyResults.length === 0) {
        this.router.navigate(['/'], {
          queryParams: {
            objectives: params['objectives'],
            teamFilter: params['teamFilter'],
            quarterFilter: params['quarterFilter'],
          },
        });
      } else {
        this.router.navigate([], navigationParams);
      }
    });
  }

  public navigate(location: string) {
    this.route.queryParams.pipe(first()).subscribe((queryParams) => {
      const navExtras = {
        queryParams: {
          ...queryParams,
        },
      };
      this.router.navigate([location], navExtras);
    });
  }

  public back() {
    this.route.queryParams.pipe(first()).subscribe((params) => {
      if (this.previousUrl !== this.currentUrl) {
        this.router.navigate([this.previousUrl!.split('?')[0]], {
          queryParams: {
            objectives: params['objectives'],
            keyresults: params['keyresults'],
            quarterFilter: params['quarterFilter'],
            teamFilter: params['teamFilter'],
          },
        });
      } else {
        this.router.navigate(['/'], {
          queryParams: {
            objectives: params['objectives'],
            keyresults: params['keyresults'],
            quarterFilter: params['quarterFilter'],
            teamFilter: params['teamFilter'],
          },
        });
      }
    });
  }

  public changeQuarterFilter(value: number | null | undefined): Observable<any> {
    return this.route.queryParams.pipe(
      first(),
      switchMap((queryParams) => {
        const navExtras = {
          queryParams: {
            ...queryParams,
            quarterFilter: value ?? undefined,
          },
        };
        return this.router.navigate(['/'], navExtras);
      })
    );
  }

  public changeTeamFilter(value: number[]): Observable<any> {
    return this.route.queryParams.pipe(
      first(),
      switchMap((queryParams) => {
        const navExtras = {
          queryParams: {
            ...queryParams,
            teamFilter: value.length > 0 ? value.toString() : undefined,
          },
        };
        return this.router.navigate(['/'], navExtras);
      })
    );
  }
}
