// <!!! Specify URL of the captcha service
// const apiUrl = "https://192.168.99.101/captcha";
const apiUrl = "http://localhost:8080";
// !!!>

const ingestUrl = apiUrl + "/ingest";
const taskRequestUrl = apiUrl + "/task/request";
const taskResponseUrl = apiUrl + "/task/response";

async function myFetch(url, fetchOptions) {
  console.log("Making a request to ", url);

  const response = await fetch(url, fetchOptions);
  const json = await response.json();

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
