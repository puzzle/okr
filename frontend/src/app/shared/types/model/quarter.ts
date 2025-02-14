export class Quarter {
  constructor(
    id: number, label: string, startDate: Date | null, endDate: Date | null, isBacklogQuarter: boolean
  ) {
    this.id = id;
    this.label = label;
    this.startDate = startDate;
    this.endDate = endDate;
    this.isBacklogQuarter = isBacklogQuarter;
  }

  readonly id: number;

  readonly label: string;

  readonly startDate: Date | null;

  readonly endDate: Date | null;

  readonly isBacklogQuarter: boolean;

  fullLabel(): string {
    return this.isCurrent() ? this.label + ' Aktuell' : this.label;
  }

  private isCurrent(): boolean {
    if (this.startDate === null || this.endDate === null) {
      return false;
    }
    return new Date(this.startDate) <= new Date() && new Date(this.endDate) >= new Date();
  }
}
