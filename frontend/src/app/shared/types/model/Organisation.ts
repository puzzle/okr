import {Team} from "./Team";
import {OrganisationState} from "../enums/OrganisationState";

export interface Organisation {
  id: number;
  version: number;
  orgName: String;
  teams: Team[];
  state: OrganisationState;
}
