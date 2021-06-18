window.addEventListener("load", function (event) {
  console.log("Loaded TextCaptcha for rtvslo.si.");

  // add captcha inject point to the DOM:
  const captchaInjectPosition = document.querySelector(
    "div#article-comments-anchor"
  );

  const injectElement = document.createElement("div");
  injectElement.id = "text-captcha";

  captchaInjectPosition.appendChild(injectElement);

  TextCaptcha(captchaInjectPosition, extractText);

});

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