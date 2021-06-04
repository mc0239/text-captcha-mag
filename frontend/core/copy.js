const fs = require("fs");
const path = require("path");

const from = path.resolve("./dist/main.js");
const to = path.resolve("../chrome-extension/main.js");

fs.copyFile(from, to, (error) => {
  if (error) {
    throw error;
  }

  console.log('\nCopied "' + from + '" to "' + to + '".\n');
});
