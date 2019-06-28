import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'localdatetime'
})
export class LocaldatetimePipe implements PipeTransform {

  transform(localDateTime: string, args?: any): string {
    return localDateTime.replace('T', ' ');
  }

}
