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

  render() {
    const { task, onSubmit } = this.props;

    if (!task) {
      // TODO
      return "...";
    }

    const tokens = task.content.map((word, index) => {
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
