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
  },
  retries: {
    // Configure retry attempts for `cypress run`
    runMode: 2,
    // Configure retry attempts for `cypress open`
    openMode: 0,
  },
});
