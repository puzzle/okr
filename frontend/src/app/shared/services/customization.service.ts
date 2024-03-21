import { Injectable } from '@angular/core';
import { ConfigService } from "../../config.service";
import { CustomizationConfig } from "../types/model/ClientConfig";

@Injectable({
  providedIn: 'root',
})
export class CustomizationService {
  private currentConfig?: CustomizationConfig;

  constructor(private configService: ConfigService) {
    configService.config$.subscribe(config => {
      this.updateCustomizations(config);
    })
  }


  private updateCustomizations(config: CustomizationConfig) {
    this.setFavicon(config.favicon);
    this.setStyleCustomizations(config.customStyles);

    this.currentConfig = config;
  }

  private setFavicon(favicon: string) {
    if (!favicon || this.currentConfig?.favicon === favicon) {
      return;
    }

    document.getElementById("favicon")?.setAttribute("src", favicon);
  }

  private setStyleCustomizations(customStylesMap: Map<string, string>) {
    if (!customStylesMap || this.areStylesTheSame(customStylesMap)) {
      return;
    }

    this.removeStyles(this.currentConfig?.customStyles)
    this.setStyles(customStylesMap);
  }

  private areStylesTheSame(customStylesMap: Map<string, string>) {
    return JSON.stringify(this.currentConfig?.customStyles) === JSON.stringify(customStylesMap);
  }

  private removeStyles(customStylesMap: Map<string, string> | undefined) {
    if (!customStylesMap) {
      return;
    }
    const styles = document?.getElementById("html")!.style;
    if (!styles) {
      return
    }

    Array.from(customStylesMap.entries()).forEach(([varName, varValue]) => {
      styles.setProperty(`--${varName}`, varValue);
    });
  }

  private setStyles(customStylesMap: Map<string, string>) {
    if (!customStylesMap) {
      return;
    }
    const styles = document?.getElementById("html")!.style;
    if (!styles) {
      return
    }

    Array.from(customStylesMap.keys()).forEach((varName) => {
      styles.removeProperty(`--${varName}`);
    });
  }
}
