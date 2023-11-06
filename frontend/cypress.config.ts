import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    defaultCommandTimeout: 10000,
  },
  env: {
    login_url: 'https://idp-mock-okr.ocp.cloudscale.puzzle.ch',
  },
});
