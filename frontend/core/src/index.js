import {
  getRequestTaskButton,
  getRespondTaskButton,
  getStatusIndicator,
  getTaskSpace,
} from "./elements";
import {
  sendIngestRequest,
  sendTaskRequest,
  sendTaskSolution,
} from "./requests";

let instanceId = -1;

window.onload = (event) => {
  console.log("Welcome to TextCaptcha.");

  injectTextCaptcha();
  getTaskSpace().innerHTML = "";
  getTaskSpace().appendChild(getStatusIndicator());

  sendIngestRequest(getArticleUrl(), extractArticleText());

  getRequestTaskButton().addEventListener("click", function () {
    sendTaskRequest(getArticleUrl(), (task) => {
      instanceId = task.id;
      renderTask(task);
    });
  });
};

function renderTask(task) {
  const meta = document.createElement("span");
  meta.textContent = "Your task id is " + task.id;
  meta.classList.add("metadata");

  getTaskSpace().appendChild(meta);

  for (let word of task.words) {
    const piece = document.createElement("span");
    piece.classList.add("piece");
    piece.textContent = word + " ";

    piece.addEventListener("click", function () {
      if (piece.classList.contains("selected")) {
        piece.classList.remove("selected");
      } else {
        piece.classList.add("selected");
      }
    });

    getTaskSpace().appendChild(piece);
  }

  const submitButton = getRespondTaskButton();
  submitButton.addEventListener("click", function () {
    const pieces = getTaskSpace().querySelectorAll(".piece");
    let selectedIndexes = [];
    for (let i = 0; i < pieces.length; i++) {
      if (pieces[i].classList.contains("selected")) {
        selectedIndexes.push(i);
      }
    }
    console.log(selectedIndexes);

    sendTaskSolution(instanceId, selectedIndexes);
  });

  getTaskSpace().appendChild(submitButton);
}

function injectTextCaptcha() {
  try {
    // TODO this is rtvslo.si specific.
    const captchaInjectPosition = document.querySelector(
      "div#article-comments-anchor"
    );
    captchaInjectPosition.append(getTaskSpace());
  } catch (e) {
    console.warn(e);
  }
}

function extractArticleText() {
  // TODO this is rtvslo.si specific.
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

function getArticleUrl() {
  return window.location.hostname + window.location.pathname;
}
