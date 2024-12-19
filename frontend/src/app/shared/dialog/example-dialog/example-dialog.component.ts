import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import errorMessages from "../../../../assets/errors/error-messages.json";
import { formInputCheck } from "../../common";

@Component({
  selector: "app-example-dialog",
  templateUrl: "./example-dialog.component.html"
})
export class ExampleDialogComponent {
  hobbies = [
    "fishing",
    "football",
    "videogames",
    "tennis",
    "other"
  ];

  protected readonly errorMessages: any = errorMessages;

  protected readonly formInputCheck = formInputCheck;

  dialogForm = new FormGroup({
    name: new FormControl<string>("", [Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50)]),
    gender: new FormControl<string>("", [Validators.required]),
    hobby: new FormControl<string>("", [Validators.required])
  });

  constructor (public dialog: MatDialogRef<ExampleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any) {}

  save () {
    this.dialog.close({ data: this.dialogForm.value });
  }

  isTouchedOrDirty (name: string) {
    return this.dialogForm.get(name)?.dirty || this.dialogForm.get(name)?.touched;
  }

  // Check if any errors are present if not return empty array if yes return error keys
  getErrorKeysOfFormField (name: string) {
    const errors = this.dialogForm.get(name)?.errors;
    return errors == null ? [] : Object.keys(errors);
  }
}
