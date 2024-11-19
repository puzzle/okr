export interface AuthConfig {
  activeProfile: string;
  issuer: string;
  clientId: string;
}

export interface CustomStyles {
  [key: string]: string;
}

export interface CustomizationConfig {
  title: string;
  favicon: string;
  logo: string;
  triangles: string;
  backgroundLogo: string;
  supportSiteUrl: string;
  customStyles: CustomStyles;
}
export interface ClientConfig extends AuthConfig, CustomizationConfig {}
