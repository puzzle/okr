import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KeyResultFormComponent } from './key-result-form.component';

const routes: Routes = [{ path: '', component: KeyResultFormComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class KeyResultFormRoutingModule {}
