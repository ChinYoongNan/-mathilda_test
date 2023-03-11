import { MatProgressButtonOptions } from 'mat-progress-buttons';
import { ThemePalette } from '@angular/material/core';
import { ProgressSpinnerMode } from '@angular/material/progress-spinner';

export class BarButtonOptions implements MatProgressButtonOptions {
  active: boolean;
  text: string;
  buttonColor: ThemePalette;
  raised: boolean;
  mode: ProgressSpinnerMode;
  disabled: boolean;
  fullWidth: boolean;
  customClass: string;

  constructor(text = 'Submit',
              disabled = true,
              buttonColor: ThemePalette = 'primary',
              customClass = '',
              active = false,
              raised = true,
              mode: ProgressSpinnerMode = 'indeterminate',
              fullWidth = true,
  ) {
    this.active = active;
    this.text = text;
    this.buttonColor = buttonColor;
    this.raised = raised;
    this.mode = mode;
    this.disabled = disabled;
    this.fullWidth = fullWidth;
    this.customClass = customClass;
  }
}
