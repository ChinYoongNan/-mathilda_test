export default class SelectOption {
  value: string;
  label: string;
  nativeInput: string;

  constructor(value = '', label = '', nativeInput = '') {
    this.value = value;
    this.label = label;
    this.nativeInput = nativeInput;
  }
}
