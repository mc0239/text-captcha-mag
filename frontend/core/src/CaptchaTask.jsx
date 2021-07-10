import React from "react";
import style from "./CaptchaTask.scss";

class CaptchaTask extends React.Component {
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
    const { task, isLoading, onSubmit } = this.props;

    if (isLoading) {
      return "Nalaganje naloge...";
    }

    const tokens = task.content.map((word, index) => {
      const isSelected = Boolean(this.state.selectedIndexes[index]);
      return (
        <span
          key={index}
          className={
            (isSelected ? style["word-selected"] : "") + " " + style["word"]
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
      <div className={"" + style["task-container"]}>
        <div style={{ textAlign: "left", fontSize: "1rem" }}>{tokens}</div>
        <div style={{ textAlign: "right" }}>
          <button
            onClick={() => {
              const result = Object.keys(this.state.selectedIndexes).filter(
                (i) => this.state.selectedIndexes[i] === true
              );
              onSubmit(result);
            }}
          >
            Pošlji
          </button>
        </div>
      </div>
    );

    // JSON.stringify(task);
  }
}

export default CaptchaTask;
