
function silence() {
    console.log("Silence is golden.");
}

chrome.runtime.onInstalled.addListener(silence);

chrome.runtime.onInstalled.addListener(function () {
    chrome.declarativeContent.onPageChanged.removeRules(undefined, function () {
        chrome.declarativeContent.onPageChanged.addRules([{
            conditions: [
                new chrome.declarativeContent.PageStateMatcher({ pageUrl: { hostEquals: 'developer.chrome.com' } }),
                new chrome.declarativeContent.PageStateMatcher({ pageUrl: { hostContains: 'rtvslo.si' } }),
            ],
            actions: [new chrome.declarativeContent.ShowPageAction()]
        }]);
    });
});