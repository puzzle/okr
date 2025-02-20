export abstract class PageObjectMapperBase {
  abstract validatePage(): void;

  static do<T extends PageObjectMapperBase>(this: new () => T): T {
    return new this();
  }

  run(arg: any) {
    return this;
  }

  checkForToaster(content: any, type: 'success' | 'error') {
    cy.get('#toast-container')
      .find(`.toast-${type}`)
      .contains(content)
      .click();
    return this;
  }

  validateUrlParameter(key: string, values: any[]) {
    cy.url()
      .then((url) => {
        const params = new URL(url).searchParams;
        const queryParamValues = params.get(key)
          ?.split(',');
        expect(queryParamValues).to.have.length(values.length);
        values.forEach((v) => expect(queryParamValues).to.include(v));
      });
  }
}
