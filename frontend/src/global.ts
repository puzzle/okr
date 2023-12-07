export {};
declare global {
  interface String {
    format(): string;
  }
}

String.prototype.format = function () {
  const args = Array.from(arguments).flat();
  return this.replace(/{(\d+)}/g, function (match, index) {
    return typeof args[index] == 'undefined' ? match : args[index];
  });
};
