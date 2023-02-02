import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { getNumberOrNull } from '../common';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private currentUrl: string;
  private previousUrl?: string;
  constructor(
    private location: Location,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.currentUrl = this.router.url;
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.previousUrl = this.currentUrl;
        this.currentUrl = event.url;
      }
    });
  }

  public addToSelectedObjectives(objectiveId: number) {
    this.route.queryParams
      .subscribe((params) => {
        let selectedObjectives: number[] =
          params['objectives']?.split(',') ?? [];
        selectedObjectives.push(objectiveId);
        this.router.navigate(['/'], {
          queryParams: {
            objectives: selectedObjectives.toString(),
            keyresults: params['keyresults'],
              quarter: params['quarter'],
          },
        });
      })
      .unsubscribe();
  }

  public removeFromSelectedObjectives(objectiveId: number) {
    this.route.queryParams
      .subscribe((params) => {
        let selectedObjectives: string[] = params['objectives'].split(',');
        selectedObjectives.forEach((item, index) => {
          if (getNumberOrNull(item) === objectiveId)
            selectedObjectives.splice(index, 1);
        });
        if (selectedObjectives.length == 0) {
          this.router.navigate(['/'], {
            queryParams: {
              keyresults: params['keyresults'],
                quarter: params['quarter'],
            },
          });
        } else {
          this.router.navigate(['/'], {
            queryParams: {
              objectives: selectedObjectives.toString(),
              keyresults: params['keyresults'],
                quarter: params['quarter'],
            },
          });
        }
      })
      .unsubscribe();
  }

  public addToSelectedKeyresults(keyResultId: number) {
    this.route.queryParams
      .subscribe((params) => {
        let selectedKeyResults: number[] =
          params['keyresults']?.split(',') ?? [];
        selectedKeyResults.push(keyResultId);
        this.router.navigate(['/'], {
          queryParams: {
            objectives: params['objectives'],
            keyresults: selectedKeyResults.toString(),
              quarter: params['quarter'],
          },
        });
      })
      .unsubscribe();
  }

  public removeFromSelectedKeyresult(keyResultId: number) {
    this.route.queryParams
      .subscribe((params) => {
        let selectedKeyResults: string[] = params['keyresults'].split(',');
        selectedKeyResults.forEach((item, index) => {
          if (getNumberOrNull(item) === keyResultId)
            selectedKeyResults.splice(index, 1);
        });
        if (selectedKeyResults.length === 0) {
          this.router.navigate(['/'], {
            queryParams: {
              objectives: params['objectives'],
                quarter: params['quarter'],
            },
          });
        } else {
          this.router.navigate(['/'], {
            queryParams: {
              objectives: params['objectives'],
              keyresults: selectedKeyResults.toString(),
                quarter: params['quarter'],
            },
          });
        }
      })
      .unsubscribe();
  }

  public navigate(location: string) {
    this.route.queryParams
      .subscribe((params) => {
        this.router.navigate([location], {
          queryParams: {
            objectives: params['objectives'],
            keyresults: params['keyresults'],
              quarter: params['quarter'],
          },
        });
      })
      .unsubscribe();
  }

  public back() {
    this.route.queryParams
      .subscribe((params) => {
        if (this.previousUrl !== this.currentUrl) {
          this.router.navigate([this.previousUrl!.split('?')[0]], {
            queryParams: {
              objectives: params['objectives'],
              keyresults: params['keyresults'],
                quarter: params['quarter'],
            },
          });
        } else {
          this.router.navigate(['/'], {
            queryParams: {
              objectives: params['objectives'],
              keyresults: params['keyresults'],
                quarter: params['quarter'],
            },
          });
        }
      })
      .unsubscribe();
  }

    changeQuarterFilter(value: number) {
        this.route.queryParams
            .subscribe((params) => {
                this.router.navigate(['/'], {
                    queryParams: {
                        objectives: params['objectives'],
                        keyresults: params['keyresults'],
                        quarter: value
                    },
                });
            })
            .unsubscribe();
    }
}
