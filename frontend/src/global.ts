export {};
declare global {
  interface String {
    format(): string;
  }
}

String.prototype.format = function(...args: any[]): string {
  args = args.flat(Infinity);
  return this.replace(/{(\d+)}/g, function(match, index) {
    return typeof args[index] == 'undefined' ? match : args[index];
  });
};
