
export interface AuthConfig {
  activeProfile: string;
  issuer: string;
  clientId: string;
}

export interface CustomizationConfig {
  favicon: string;
  logo: string;
  customStyles: Map<string, string>;
}
export interface ClientConfig extends AuthConfig, CustomizationConfig {
}
