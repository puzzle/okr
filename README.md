# okr

Open source OKR application

### After clone

1. Run `npm install` in root folder for adding all the dependencies
2. Run `npm run prestart` in /frontend folder for adding a pre-commit file to local /.git folder

### Frontend Tests

Run `npm test` or `jest` in frontend folder to run frontend tests on console

To run the jest tests in a run configuration, adjust the run configuration like this

- Node interpreter: 16.13.0
- Jest package: /frontend/node_modules/jest (28.1.3)
- Working Directory: ~/dev/okr/frontend

Then select all tests for the tests options. Now just run the
configuration and the test will be executed
