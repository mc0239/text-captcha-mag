// <!!! Specify URL of the captcha service
const ingestApiUrl = "http://localhost:8010";
const managerApiUrl = "http://localhost:8020";
// !!!>

const ingestUrl = ingestApiUrl + "/ingest";
const taskRequestUrl = managerApiUrl + "/task/request";
const taskResponseUrl = managerApiUrl + "/task/response";

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

async function taskResponse(instanceId, selectedIndexes) {
  const fetchOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      id: instanceId,
      indexes: selectedIndexes,
    }),
  };

  return await myFetch(taskResponseUrl, fetchOptions);
}

const ApiClient = {
  ingest: ingest,
  task: {
    request: taskRequest,
    response: taskResponse,
  },
};

export default ApiClient;
