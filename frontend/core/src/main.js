import React from "react";
import ReactDOM from "react-dom";
import isFunction from "lodash/isFunction";
import App from "./App";

export default function init(containerElement, getTextFunction) {
  console.log("Welcome to TextCaptcha.");

  if (!isFunction(getTextFunction)) {
    console.error("getTextFunction is not a function: ", getTextFunction);
    return;
  }

  ReactDOM.render(
    React.createElement(App, { getText: getTextFunction }),
    containerElement
  );
}
