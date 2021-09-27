window.addEventListener("load", function (event) {
  console.log("Loaded TextCaptcha for rtvslo.si.");

  document.querySelector("a.btn-show-comments").addEventListener('click', () => {
    // Because comment form does not appear immediately after clicking on show comments link,
    // we quickly patch this by giving it a timeout (I'm sure there's a better way to solve this).
    setTimeout(() => {
      initializeCaptcha();
    }, 1000);
  });
});

function initializeCaptcha() {
  // add captcha inject point to the DOM:
  const captchaInjectPosition = document.querySelector(
    ".comment-form > .form-group"
  );

  const injectElement = document.createElement("div");
  injectElement.id = "text-captcha";

  captchaInjectPosition.appendChild(injectElement);

  TextCaptcha(injectElement, extractText, onCaptchaComplete);
}

function onCaptchaComplete(captchaId) {
  console.log("CAPTCHA complete: ", captchaId)
}

function extractText() {
  const articleTitle = document.querySelector("h1").textContent;
  const articleSubtitle = document.querySelector("div.subtitle").textContent;
  const articleLead = document.querySelector("p.lead").textContent;
  const articleBodyParas = Array.from(
    document.querySelector("div.article-body").querySelectorAll("p")
  );
  const articleBody = Array.from(
    document.querySelector("div.article-body").querySelectorAll("p")
  )
    .map((p) => p.textContent)
    .join(" ");

  console.log("%c " + articleTitle, "font-size: 1.5em; font-weight: bold;");
  console.log("%c " + articleSubtitle, "font-size: 1.25em; font-weight: bold;");
  console.log("%c " + articleLead, "font-weight: bold;");
  console.log("%c " + articleBody, "");

  const allText = `${articleTitle}. ${articleSubtitle}. ${articleLead} ${articleBody}`;
  return allText;
}