import { Inject, Injectable } from '@angular/core';
import { ConfigService } from '../../config.service';
import { CustomizationConfig, CustomStyles } from '../types/model/ClientConfig';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class CustomizationService {
  private currentConfig?: CustomizationConfig;

  constructor(
    configService: ConfigService,
    @Inject(DOCUMENT) private document: Document,
  ) {
    configService.config$.subscribe((config) => {
      this.updateCustomizations(config);
    });
  }

  private updateCustomizations(config: CustomizationConfig) {
    this.setTitle(config.title);
    this.setFavicon(config.favicon);
    this.setStyleCustomizations(config.customStyles);

    this.currentConfig = config;
  }

  private setFavicon(favicon: string) {
    if (!favicon || this.currentConfig?.favicon === favicon) {
      return;
    }

    this.document.getElementById('favicon')?.setAttribute('href', favicon);
  }

  private setTitle(title: string) {
    if (!title || this.currentConfig?.title === title) {
      return;
    }
    this.document.querySelector('title')!.innerHTML = title;
  }

  private setStyleCustomizations(customStylesMap: CustomStyles) {
    if (!customStylesMap || this.areStylesTheSame(customStylesMap)) {
      return;
    }

    this.removeStyles(this.currentConfig?.customStyles);
    this.setStyles(customStylesMap);
  }

  private areStylesTheSame(customStylesMap: CustomStyles) {
    return JSON.stringify(this.currentConfig?.customStyles) === JSON.stringify(customStylesMap);
  }

  private setStyles(customStylesMap: CustomStyles | undefined) {
    if (!customStylesMap) {
      return;
    }

    const styles = this.document.querySelector('html')!.style;
    if (!styles) {
      return;
    }

    Object.entries(customStylesMap).forEach(([varName, varValue]) => {
      styles.setProperty(`--${varName}`, varValue);
    });
  }

  private removeStyles(customStylesMap: CustomStyles | undefined) {
    if (!customStylesMap) {
      return;
    }

    const styles = this.document.querySelector('html')!.style;
    if (!styles) {
      return;
    }
    Object.keys(customStylesMap).forEach((varName) => {
      styles.removeProperty(`--${varName}`);
    });
  }
}
