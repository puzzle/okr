export function filterByKeyResultName(keyResultName: string) {
  return (index: number, element: HTMLElement) => isKeyResultName(element, keyResultName);
}

const isKeyResultName = (element: HTMLElement, keyResultName: string) => {
  return Cypress.$(element).find(`:contains("${keyResultName}")`).length > 0;
};

export function getKeyResults() {
  return cy.get('.key-result');
}
