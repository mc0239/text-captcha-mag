function silence() {
  console.log("Silence is golden.");
}

chrome.runtime.onInstalled.addListener(silence);
