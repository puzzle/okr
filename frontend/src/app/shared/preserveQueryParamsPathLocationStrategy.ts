import { APP_BASE_HREF, PathLocationStrategy, PlatformLocation } from '@angular/common';
import { UrlSerializer } from '@angular/router';
import { Inject, Injectable, Optional } from '@angular/core';

@Injectable()
export class PreserveQueryParamsPathLocationStrategy extends PathLocationStrategy {
  private get search(): string {
    return this.platformLocation?.search ?? '';
  }

  constructor(
    private platformLocation: PlatformLocation,
    private urlSerializer: UrlSerializer,
    @Optional() @Inject(APP_BASE_HREF) _baseHref?: string,
  ) {
    super(platformLocation, _baseHref);
  }

  override prepareExternalUrl(internal: string): string {
    const path = super.prepareExternalUrl(internal);
    const urlTree = this.urlSerializer.parse(path);

    const nextQueryParams = urlTree.queryParams;
    const existingURLSearchParams = new URLSearchParams(this.search);
    const existingQueryParams: { [key: string]: string } = {};
    existingURLSearchParams.forEach((value, key) => (existingQueryParams[key] = value));

    urlTree.queryParams = { ...existingQueryParams, ...nextQueryParams };
    console.log('existing ', this.search);
    console.log('next ', internal);
    console.log('merged ', urlTree.toString());
    return urlTree.toString();
  }
}
