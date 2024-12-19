# OKR-Tool Frontend

This is the Angular Frontend for the OKR-Tool.

## DEVELOPING

Environment:

- We require Node.js ^18

Install:

- Install npm dependencies: `npm install`
- Make a clean install: `npm ci`
- Install the git hooks for the frontend: `npm run prestart`

Formatting:

- Check code formatting: `npm run check`
- Format the code: `npm run format`

Test and Run:

- Execute the tests: `npm run test`
- Start the frontend: `npm run start`

Build:

- Build for production: `npm run build`

## Formatting

We use the **prettier** code formatter for the frontend code:
https://www.npmjs.com/package/prettier

If you get a does not match pattern error, it is likely because of the custom words for the OKR tool
here is a regex101 project that showcases how the regex works:
https://regex101.com/r/VUyAt6/1
## Test Coverage

For generating the coverage of our frontend specs (jest), you can use following command:
`npx jest --coverage ` (`jest --coverage` if you have jest installed locally)

This generates the coverage, you can see it in `frontend/coverage/lcov-report/index.html`.
Open the html file in browser and you get a beautiful overview

## Cypress Tests

- local setup
  - start local Docker `docker-compose up`
  - start local Server: `OkrApplication-E2E`
  - start local Client: `npm run start`
- run selected Tests
  - npm run `npm run cypress:open`
  - in Cypress App, select `E2E Testing` and `Chrome` as Browser
- run all tests
  - npm run `npm run cypress:run`
  - in Cypress App, select `E2E Testing` and `Chrome` as Browser
- in case of failing Tests:
  - stop and restart local Server
  - stop and restart local Client
  - re-run Cypress Tests
