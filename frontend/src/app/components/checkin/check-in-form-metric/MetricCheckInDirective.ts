import { Directive, HostListener, forwardRef, ElementRef, Renderer2 } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[metricCheckIn]',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MetricCheckInDirective),
      multi: true,
    },
  ],
})
export class MetricCheckInDirective implements ControlValueAccessor {
  private onChange: (value: number | null) => void = () => {};
  protected readonly CHAR_REGEX = /[^0-9.]/g;

  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
  ) {}

  writeValue(value: any): void {
    // does not need to be implemented because the display value does not need to be modified
  }

  registerOnChange(fn: (value: number | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    // does not need to be implemented
  }

  setDisabledState(isDisabled: boolean) {
    this.renderer.setProperty(this.el.nativeElement, 'disabled', isDisabled);
  }

  @HostListener('input', ['$event.target.value'])
  handleInput(param: string): void {
    const value: string = param || '0';
    if (value.toString().at(0) == '-') {
      this.onChange(+('-' + value.toString().replace(this.CHAR_REGEX, '')));
      return;
    }
    this.onChange(Number(value.toString().replace(this.CHAR_REGEX, '')));
  }
}
