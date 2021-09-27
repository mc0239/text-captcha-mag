// <!!! Specify URL of the captcha service
const ingestApiUrl = "https://nlp.cebular.net/captcha/i"; // "http://localhost:8010";
const managerApiUrl = "https://nlp.cebular.net/captcha/t"; // "http://localhost:8020";
// !!!>

const ingestUrl = ingestApiUrl + "/ingest";
const taskRequestUrl = managerApiUrl + "/task/request";
const taskResponseUrl = managerApiUrl + "/task/response";
const flowBeginUrl = managerApiUrl + "/flow/begin";
const flowContinueUrl = managerApiUrl + "/flow/continue";
const compStartUrl = managerApiUrl + "/comp/start";
const compSolveUrl = managerApiUrl + "/comp/solve";

async function myFetch(url, fetchOptions) {
  console.log("Making a request to ", url);

  const response = await fetch(url, fetchOptions);
  const json = await response.json();

  if (!response.ok) {
    throw json;
  }

  return json;
}

async function ingest(articleUrl, articleText) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      articleUrl: articleUrl,
      articleText: articleText,
    }),
  };

  return await myFetch(ingestUrl, fetchOptions);
}

async function taskRequest(articleIdentifyingObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(articleIdentifyingObject),
  };

  return await myFetch(taskRequestUrl, fetchOptions);
}

async function taskResponse(taskSolutionObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(taskSolutionObject),
  };

  return await myFetch(taskResponseUrl, fetchOptions);
}

async function flowBegin(articleIdentifyingObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(articleIdentifyingObject),
  };

  return await myFetch(flowBeginUrl, fetchOptions);
}

async function flowContinue(taskSolutionObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(taskSolutionObject),
  };

  return await myFetch(flowContinueUrl, fetchOptions);
}

async function compStart(articleIdentifyingObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(articleIdentifyingObject),
  };

  return await myFetch(compStartUrl, fetchOptions);
}

async function compSolve(taskSolutionsObject) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(taskSolutionsObject),
  };

  return await myFetch(compSolveUrl, fetchOptions);
}

const ApiClient = {
  ingest: ingest,
  task: {
    request: taskRequest,
    response: taskResponse,
  },
  flow: {
    begin: flowBegin,
    continue: flowContinue,
  },
  comp: {
    start: compStart,
    solve: compSolve,
  },
};

export default ApiClient;
