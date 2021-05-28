const apiUrl = "https://192.168.99.101/captcha";


$( document ).ready(function() {
    $.ajax({
        type: "POST",
        url: apiUrl + "/ingest",
        data: JSON.stringify({
            articleUrl: window.location.hostname + window.location.pathname,
            text: extractText()
        }),
        contentType: "application/json"
    }).done(function (data, textStatus, jqXHR) {
        console.log("Data sent & processed.");
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error(errorThrown);
    }).always(function () { });

});

let captchaButton = null;
function getCaptchaButton() {
    if (captchaButton == null) {
        // create a button if it doesnt exist yet.
        captchaButton = document.createElement("button");
        captchaButton.id = "captcha-button";
        captchaButton.appendChild(document.createTextNode("captcha time!"));
        captchaButton.addEventListener('click', function () {
            fetchCaptchaTask();
        })
    }

    return captchaButton;
}

let captchaTaskSpace = null;
function getCaptchaTaskSpace() {
    if (captchaTaskSpace == null) {
        captchaTaskSpace = document.createElement("div");
        captchaTaskSpace.id = "captcha-space";
    }

    return captchaTaskSpace;
}

let captchaSubmitButton = null;
function getCaptchaSubmitButton() {
    if (captchaSubmitButton == null) {
        captchaSubmitButton = document.createElement("button");
        captchaSubmitButton.id = "captcha-submit";
        captchaSubmitButton.appendChild(document.createTextNode("submit"));
        captchaSubmitButton.addEventListener('click', function () {
            submitCaptchaResponse();
        })
    }

    return captchaSubmitButton;
}

let instanceId = -1;

function fetchCaptchaTask() {
    getCaptchaButton().textContent = "Loading...";
    getCaptchaTaskSpace().innerHTML = "";

    $.ajax({
        type: "POST",
        url: apiUrl + "/task/request",
        data: JSON.stringify({
            articleUrl: window.location.hostname + window.location.pathname,
        }),
        contentType: "application/json"
    }).done(function (data, textStatus, jqXHR) {
        console.log("Your task is: ", data);
        instanceId = data.id;
        renderTask(data);

    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error(errorThrown);
        getCaptchaButton().textContent = "Something went wrong. Click to try again."

    }).always(function() {
        getCaptchaButton().textContent = "Captcha time!";

    });
}

function submitCaptchaResponse() {
    const pieces = getCaptchaTaskSpace().querySelectorAll(".piece");
    let selectedIndexes = [];
    for (let i = 0; i < pieces.length; i++) {
        if (pieces[i].classList.contains("selected")) {
            selectedIndexes.push(i);
        }
    }
    console.log(selectedIndexes);

    getCaptchaSubmitButton().textContent = "submitting...";

    $.ajax({
        type: "POST",
        url: apiUrl + "/task/response",
        data: JSON.stringify({
            id: instanceId,
            indexes: selectedIndexes
        }),
        contentType: "application/json"
    }).done(function (data, textStatus, jqXHR) {
        console.log(data);
        getCaptchaTaskSpace().innerHTML = data;

    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error(errorThrown);

    }).always(function() {
        instanceId = -1;

    });
}

function extractText() {
    const articleTitle = document.querySelector("h1").textContent;
    const articleSubtitle = document.querySelector("div.subtitle").textContent;
    const articleLead = document.querySelector("p.lead").textContent;
    const articleBodyParas = Array.from(document.querySelector("div.article-body").querySelectorAll("p"));
    const articleBody = Array.from(document.querySelector("div.article-body").querySelectorAll("p")).map(p => p.textContent).join(" ");

    console.log("%c " + articleTitle, "font-size: 1.5em; font-weight: bold;")
    console.log("%c " + articleSubtitle, "font-size: 1.25em; font-weight: bold;")
    console.log("%c " + articleLead, "font-weight: bold;")
    console.log("%c " + articleBody, "")

    const allText = `${articleTitle}. ${articleSubtitle}. ${articleLead} ${articleBody}`;
    return allText;
}


function renderTask(task) {

    const metaPiece = document.createElement("span");
    metaPiece.textContent = "Your task id is " + task.id;
    metaPiece.className = "meta"
    getCaptchaTaskSpace().appendChild(metaPiece);

    for (word of task.words) {
        const piece = document.createElement("span");
        piece.className = "piece";
        piece.textContent = word + " ";
        piece.addEventListener('click', function () {
            if (piece.classList.contains("selected")) {
                piece.classList.remove("selected");
            } else {
                piece.classList.add("selected");
            }
        });
        getCaptchaTaskSpace().appendChild(piece);
    }

    getCaptchaTaskSpace().appendChild(getCaptchaSubmitButton());
}


try {
    const captchaInjectPosition = document.querySelector("div#article-comments-anchor");
    captchaInjectPosition.append(getCaptchaButton());
    captchaInjectPosition.append(getCaptchaTaskSpace());
} catch (e) {
    console.warn(e);
}
