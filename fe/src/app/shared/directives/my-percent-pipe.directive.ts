import { Pipe, PipeTransform } from '@angular/core';
import { DebugHelper } from 'protractor/built/debugger';

@Pipe({ name: 'myPercent' })
export class MyPercentPipe implements PipeTransform {
  transform(value: any): string {
    value = value * 100;
    if (value === Infinity) {
      return value;
    } else if (value < 0) {
      return `${value.toFixed(2)}` + '%';
    } else if (value > 0) {
      return value.toFixed(2) + '%';
    } else if (value === 0) {
      return '0%';
    } else {
      return value;
    }

  }
}
