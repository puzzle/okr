import { ChangeDetectionStrategy, Component } from "@angular/core";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  readonly PATH_PREFIX = "../assets/icons/";

  constructor (private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer) {
    this.matIconRegistry.addSvgIcon("pz-search", this.domSanitizer.bypassSecurityTrustResourceUrl(this.PATH_PREFIX + "search-icon.svg"));
    this.matIconRegistry.addSvgIcon("pz-menu-icon", this.domSanitizer.bypassSecurityTrustResourceUrl(this.PATH_PREFIX + "three-dot-menu-icon.svg"));
  }
}
