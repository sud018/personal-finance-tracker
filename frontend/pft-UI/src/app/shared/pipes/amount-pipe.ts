import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'amount',
  standalone: true
})
export class AmountPipe implements PipeTransform {
  transform(value: number, currency: string = '$'): string {
    if (value == null) return `${currency}0.00`;
    return `${currency}${Math.abs(value).toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })}`;
  }
}