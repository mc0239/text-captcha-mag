import "./elements.scss";

//

let statusIndicator = null;

export function getStatusIndicator() {
  if (statusIndicator === null) {
    statusIndicator = document.createElement("div");
    statusIndicator.id = "t-captcha-status";
    statusIndicator.textContent = "Idle";
  }

  return statusIndicator;
}

//

let requestTaskButton = null;

export function getRequestTaskButton() {
  if (requestTaskButton === null) {
    requestTaskButton = document.createElement("button");
    requestTaskButton.id = "t-captcha-request";
    requestTaskButton.textContent = "Solve TextCaptcha";
  }

  return requestTaskButton;
}

//

let respondTaskButton = null;

export function getRespondTaskButton() {
  if (respondTaskButton === null) {
    respondTaskButton = document.createElement("button");
    respondTaskButton.id = "t-captcha-respond";
    respondTaskButton.textContent = "Submit";
  }

  return respondTaskButton;
}

//

let taskSpace = null;

export function getTaskSpace() {
  if (taskSpace === null) {
    taskSpace = document.createElement("div");
    taskSpace.id = "t-captcha-space";
    taskSpace.classList.add("border-gradient");
  }

  return taskSpace;
}
