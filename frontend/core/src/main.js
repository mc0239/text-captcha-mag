import React from "react";
import ReactDOM from "react-dom";
import isFunction from "lodash/isFunction";
import App from "./App";

export default function init(
  containerElement,
  getTextFunction,
  flowCompleteFunction
) {
  console.log("Welcome to TextCaptcha.");

  if (!isFunction(getTextFunction)) {
    console.error("getTextFunction is not a function: ", getTextFunction);
    return;
  }

  if (!isFunction(flowCompleteFunction)) {
    console.error(
      "flowCompleteFunction is not a function: ",
      flowCompleteFunction
    );
    return;
  }

  ReactDOM.render(
    React.createElement(App, {
      getText: getTextFunction,
      flowComplete: flowCompleteFunction,
    }),
    containerElement
  );
}
