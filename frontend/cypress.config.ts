import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://pitc.okr.localhost:4200',
    experimentalMemoryManagement: true,
    testIsolation: true,
    viewportWidth: 1920,
    viewportHeight: 1080
  },
  env: {
    LOGIN_URL: 'http://localhost:8544'
  }
});
