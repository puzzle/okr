import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'app-drawer-content',
  templateUrl: './drawer-content.component.html',
  styleUrls: ['./drawer-content.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DrawerContentComponent implements OnInit, OnChanges {
  @Input() drawerContent!: { id: string; type: string };
  constructor(private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit() {}

  ngOnChanges(changes: SimpleChanges) {
    this.changeDetectorRef.markForCheck();
    console.log(this.drawerContent);
  }
}
