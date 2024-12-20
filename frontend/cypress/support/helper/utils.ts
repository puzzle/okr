import { v4 as uuidv4 } from 'uuid';
import { keyCodeDefinitions } from 'cypress-real-events/keyCodeDefinitions';

export const uniqueSuffix = (value: string): string => {
  return `${value}-${uuidv4()}`;
};

export function pressUntilContains(text: string, key: keyof typeof keyCodeDefinitions) {
  const condition = (element: HTMLElement) => element.innerText.includes(text);
  pressUntil(key, condition);
}

export function doUntilSelector(
  selector: string, tab: () => void, limit = 100, count = 0
) {
  const condition = (element: HTMLElement) => Cypress.$(element)
    .is(selector);
  doUntil(
    condition, tab, limit, count
  );
}

function pressUntil(key: keyof typeof keyCodeDefinitions, condition: (elem: HTMLElement) => boolean) {
  doUntil(condition, () => cy.realPress(key));
}

function doUntil(
  condition: (element: HTMLElement) => boolean,
  tab: () => void,
  limit = 100,
  count = 0
) {
  if (count >= limit) return;

  cy.focused()
    .then((element) => {
      if (condition(element.get(0))) {
        return;
      } else {
        tab();
        doUntil(
          condition, tab, limit, count + 1
        );
      }
    });
}
