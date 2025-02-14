export interface AuthConfig {
  activeProfile: string;
  issuer: string;
  clientId: string;
}

export type CustomStyles = Record<string, string>;

export interface CustomizationConfig {
  title: string;
  favicon: string;
  logo: string;
  triangles: string;
  backgroundLogo: string;
  helpSiteUrl: string;
  customStyles: CustomStyles;
}
export interface ClientConfig extends AuthConfig, CustomizationConfig {}
