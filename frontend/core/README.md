# frontend/core

Includes all the core functionality for the TextCaptcha to work (making requests, rendering tasks, etc).

## Building

Requires Node & npm installed.

Run `npm install` to install dependencies.

To produce a single javascript file (`dist/main.js`), run `npm run webpack`. This javascript file
can then be injected into a webpage where frontend should load up.

To produce a single javascript file and immediately copy it over to `chrome-extension`
directory, run `npm run build:prod`.