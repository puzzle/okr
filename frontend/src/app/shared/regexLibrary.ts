export const PERCENT_REGEX = '^-?[0-9][0-9]?(\\.[0-9]*)?$|^100$';
export const NUMBER_REGEX = '^-?[0-9][0-9]*.?[0-9]*?$';
export const CHAR_REGEX = /[^0-9.]/g;

export const THOUSAND_SEPERATOR = /\B(?=(\d{3})+(?!\d))/g;
