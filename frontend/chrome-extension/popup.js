const button = document.querySelector("button#extract");
if (button) {
    button.addEventListener('click', function () {
        console.log("wwwww");

        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.tabs.executeScript(
                tabs[0].id,
                { code: 'document.body.style.backgroundColor = "' + color + '";' });
        });


    });
} else {
    console.error("button#extract is ", button);
}
