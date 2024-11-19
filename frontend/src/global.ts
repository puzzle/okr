export {};
declare global {
  interface String {
    format(): string;
  }
}

String.prototype.format = function (...args) {
  const allArgs = Array.from(args).flat();
  return this.replace(/{(\d+)}/g, function (match, index) {
    return typeof allArgs[index] == 'undefined' ? match : allArgs[index];
  });
};
