import React from "react";
import taskStyle from "./CaptchaTask.scss";
import nerTaskStyle from "./NerCaptchaTask.scss";

class NerCaptchaTask extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedIndexes: {},
    };
  }

  selectWord = (index) => {
    this.setState((state) => {
      const si = state.selectedIndexes;
      si[index] = !si[index];

      return {
        ...state,
        selectedIndexes: si,
      };
    });
  };

  annotation2text = (anno) => {
    if (anno.includes("PER")) {
      return "oseb";
    } else if (anno.includes("ORG")) {
      return "organizacij";
    } else if (anno.includes("LOC")) {
      return "krajev";
    } else if (anno.includes("PRO")) {
      return "storitev ali izdelkov";
    } else if (anno.includes("EVT")) {
      return "dogodkov";
    } else {
      console.error("");
      return "";
    }
  };

  render() {
    const { task, onSubmit } = this.props;

    if (!task) {
      // TODO
      return "...";
    }

    const { primaryAnnotation, words } = task.content;

    const tokens = words.map((word, index) => {
      const isSelected = Boolean(this.state.selectedIndexes[index]);
      return (
        <span
          key={index}
          className={
            (isSelected ? nerTaskStyle["word-selected"] : "") +
            " " +
            nerTaskStyle["word"]
          }
          onClick={() => {
            this.selectWord(index);
          }}
        >
          {word}{" "}
        </span>
      );
    });

    return (
      <div className={"" + taskStyle["task-container"]}>
        <div
          style={{
            textAlign: "left",
            fontSize: "0.95rem",
            marginBottom: "8px",
            marginLeft: "8px",
            color: "#888",
          }}
        >
          Označite imena{" "}
          <span style={{ color: "black" }}>
            {this.annotation2text(primaryAnnotation)}
          </span>
          .
        </div>
        <div style={{ textAlign: "left", fontSize: "1rem" }}>{tokens}</div>
        <div style={{ textAlign: "right" }}>
          <button
            onClick={() => {
              const result = Object.keys(this.state.selectedIndexes).filter(
                (i) => this.state.selectedIndexes[i] === true
              );
              onSubmit({
                taskType: "NER",
                indexes: result,
              });
            }}
          >
            Pošlji
          </button>
        </div>
      </div>
    );
  }
}

export default NerCaptchaTask;
