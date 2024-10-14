import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://pitc.okr.localhost:4200',
    experimentalMemoryManagement: true,
  },
  env: {
    login_url: 'http://localhost:8544',
  },
});
