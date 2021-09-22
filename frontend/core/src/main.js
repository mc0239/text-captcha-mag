import React from "react";
import ReactDOM from "react-dom";
import isFunction from "lodash/isFunction";
import App from "./App";

export default function init(
  containerElement,
  getTextFunction,
  onCompleteFunction
) {
  console.log("Welcome to TextCaptcha.");

  if (!isFunction(getTextFunction)) {
    console.error("getTextFunction is not a function: ", getTextFunction);
    return;
  }

  if (!isFunction(onCompleteFunction)) {
    console.error("onCompleteFunction is not a function: ", onCompleteFunction);
    return;
  }

  ReactDOM.render(
    React.createElement(App, {
      getText: getTextFunction,
      onComplete: onCompleteFunction,
    }),
    containerElement
  );
}
