# frontend/core

Includes all the core functionality for the TextCaptcha to work (making
requests, rendering tasks, etc).

## Building

Requires Node & npm installed.

Run `npm install` to install dependencies.

To produce a single javascript file (`dist/captcha.umd.js`), run
`npm run build`. This javascript file
can then be injected into a webpage where frontend should load up.

To produce a single javascript file and immediately copy it over to
`chrome-extension` and `integration-demo` directories, run `npm run deploy`.

## Usage

```html
<!-- include the umd script -->
<script src="path/to/captcha.umd.js"></script>

<!-- call TextCaptcha() function, passing in the DOM element where CAPTCHA
     should inject, a function that returns a text for CAPTCHA to use and a
     function that executes after CAPTCHA has been successfuly completed.
-->
<script>
  TextCaptcha(
    document.querySelector("#text-captcha"),
    function () {
      return document.querySelector("article").textContent;
    },
    function (captchaId) {
      console.log("CAPTCHA complete:", captchaId);
    }
  );
</script>
```
