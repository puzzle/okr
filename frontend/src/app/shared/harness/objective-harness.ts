import { AsyncFactoryFn, ComponentHarness } from '@angular/cdk/testing';
import { MatMenuHarness } from '@angular/material/menu/testing';

export class ObjectiveHarness extends ComponentHarness {
  static hostSelector = 'app-objective-column';
  protected getMatMenu: AsyncFactoryFn<MatMenuHarness> = this.locatorFor(MatMenuHarness);

  async isMenuOpen(): Promise<boolean> {
    const menu = await this.getMatMenu();
    return menu.isOpen();
  }

  async setMenuState(state: boolean): Promise<void> {
    const matMenu = await this.getMatMenu();
    if (state) {
      return matMenu.open();
    } else {
      return matMenu.close();
    }
  }
}
