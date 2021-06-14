import {
  getRequestTaskButton,
  getRespondTaskButton,
  getStatusIndicator,
  getTaskSpace,
} from "./elements";

// <!!! Specify URL of the captcha service
// const apiUrl = "https://192.168.99.101/captcha";
const apiUrl = "http://localhost:8080/";
// !!!>

const ingestUrl = apiUrl + "/ingest";
const taskRequestUrl = apiUrl + "/task/request";
const taskResponseUrl = apiUrl + "/task/response";

let articleIdentifyingObject = null;

export function sendIngestRequest(articleUrl, articleText) {
  getStatusIndicator().textContent = "Preparing TextCaptcha";

  window
    .fetch(ingestUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        articleUrl: articleUrl,
        articleText: articleText,
      }),
    })
    .then((response) => {
      if (response.ok === true) {
      response
        .json()
        .then((data) => {
          articleIdentifyingObject = data;
          getStatusIndicator().textContent = "OK!";
          getTaskSpace().innerHTML = "";
          getTaskSpace().appendChild(getRequestTaskButton());
        }).catch(error => {
          console.error(error);
          getStatusIndicator().textContent = "Something went wrong.";
        });
      } else {
        getStatusIndicator().textContent = "Something went wrong.";
      }
    })
    .catch((error) => {
      console.error(error);
      getStatusIndicator().textContent = "Something went wrong.";
    })
    .finally(() => {
      // nothing to do.
    });
}

export function sendTaskRequest(renderTaskCallback) {
  getRequestTaskButton().textContent = "Loading...";
  getTaskSpace().innerHTML = "";

  window
    .fetch(taskRequestUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(articleIdentifyingObject),
    })
    .then((response) => {
      if (response.ok === true) {
        response
          .json()
          .then((data) => renderTaskCallback(data))
          .catch((error) => console.error(error));
      } else {
        throw new Error("Response not OK ", response);
      }
    })
    .catch((reason) => {
      getRequestTaskButton().textContent =
        "Something went wrong... Click to try again.";
    })
    .finally(() => {
      getRequestTaskButton().textContent = "Request new task.";
    });
}

export function sendTaskSolution(instanceId, selectedIndexes) {
  getRespondTaskButton().textContent = "Submitting...";

  window
    .fetch(taskResponseUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id: instanceId,
        indexes: selectedIndexes,
      }),
    })
    .then((response) => {
      if (response.ok === true) {
        response
          .json()
          .then((data) => {
            getTaskSpace().innerHTML = data.content;
            getTaskSpace().appendChild(getRequestTaskButton());
          })
          .catch((error) => console.error(error));
      } else if (response.status === 400) {
        response.json().then((data) => {
          console.log(data);
          getTaskSpace().innerHTML = data.message;
          getTaskSpace().appendChild(getRequestTaskButton());
        }).catch(e => {
          getTaskSpace().innerHTML = "Something went wrong.";
          getTaskSpace().appendChild(getRequestTaskButton());
          console.error(error);
        });
      } else {
        throw new Error("Response not OK ", response);
      }
    })
    .catch((reason) => {
      console.error(reason);
    })
    .finally(() => {
      getRespondTaskButton().textContent = "Submit";
      // TODO clean up instanceId
    });
}
