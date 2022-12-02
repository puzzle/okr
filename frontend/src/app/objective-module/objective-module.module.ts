import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ObjectiveDetailComponent } from './objective-detail/objective-detail.component';



@NgModule({
    declarations: [
        ObjectiveDetailComponent
    ],
    exports: [
        ObjectiveDetailComponent
    ],
    imports: [
        CommonModule
    ]
})
export class ObjectiveModuleModule { }
