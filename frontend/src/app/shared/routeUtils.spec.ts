import { getRouteToTeam, getRouteToUserDetails } from "./routeUtils";

describe("routeUtils",
  () => {
    describe("getRouteToUserDetails",
      () => {
        it("should not include team fragment when teamId is missing",
          () => {
            expect(getRouteToUserDetails(1))
              .toBe("/team-management/details/member/1");
          });

        it("should include team fragment when teamId is missing",
          () => {
            expect(getRouteToUserDetails(1,
              11))
              .toBe("/team-management/11/details/member/1");
          });

        it("should work with id=0",
          () => {
            expect(getRouteToUserDetails(0,
              0))
              .toBe("/team-management/0/details/member/0");
          });
      });

    describe("getRouteToTeam",
      () => {
        it("should work with normal id",
          () => {
            expect(getRouteToTeam(1))
              .toBe("/team-management/1");
          });

        it("should work with id=0",
          () => {
            expect(getRouteToTeam(0))
              .toBe("/team-management/0");
          });
      });
  });
