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

      const newState = {
        ...state,
        selectedIndexes: si,
      };

      this.props.onChange(
        Object.keys(newState.selectedIndexes).filter(
          (i) => this.state.selectedIndexes[i] === true
        )
      );
      return newState;
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
    const { task } = this.props;

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
          tabIndex="0"
          role="checkbox"
          ariaChecked={Boolean(isSelected)}
          onClick={() => {
            this.selectWord(index);
          }}
          onKeyPress={(e) => {
            if (e.code === "Space") {
              this.selectWord(index);
            }
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
      </div>
    );
  }
}

export default NerCaptchaTask;
