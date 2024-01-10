export const getRouteToUserDetails = (userId: number, teamId?: number) => {
  const teamFragment = teamId !== undefined ? `/${teamId}` : '';
  return `/team-management${teamFragment}/details/member/${userId}`;
};
export const getRouteToTeam = (teamId: number) => `/team-management/${teamId}`;
