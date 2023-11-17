import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    experimentalMemoryManagement: true,
    numTestsKeptInMemory: 30,
  },
  env: {
    login_url: 'https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch',
  },
});
