import React from "react";
import taskStyle from "./CaptchaTask.scss";
import corefTaskStyle from "./CorefCaptchaTask.scss";

class CorefCaptchaTask extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedIndexes: {},
    };
  }

  selectMention = (index) => {
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

    const mentionOfInterest = task.content.primaryWordsList.map(
      (token, index) => {
        return (
          <span
            key={index}
            className={
              (token.mention === true
                ? corefTaskStyle["word-highlighted"]
                : "") +
              " " +
              corefTaskStyle["word"]
            }
          >
            {token.word}{" "}
          </span>
        );
      }
    );

    const suggestedMentions = task.content.suggestedWordsLists.map(
      (mention, mentionIndex) => {
        const isSelectedMention = Boolean(
          this.state.selectedIndexes[mentionIndex]
        );

        return (
          <li key={mentionIndex}>
            <input
              type="checkbox"
              name={mentionIndex}
              checked={isSelectedMention}
              onChange={() => {
                this.selectMention(mentionIndex);
              }}
            />
            {mention.map((token, index) => {
              return (
                <span
                  key={index}
                  className={
                    (token.mention === true
                      ? corefTaskStyle["word-highlighted"]
                      : "") +
                    " " +
                    corefTaskStyle["word"]
                  }
                >
                  {token.word}{" "}
                </span>
              );
            })}
          </li>
        );
      }
    );

    return (
      <div className={"" + taskStyle["task-container"]}>
        <div style={{ textAlign: "left", fontSize: "1rem" }}>
          {mentionOfInterest}
        </div>
        <div style={{ textAlign: "left", fontSize: "0.95rem" }}>
          <ul>{suggestedMentions}</ul>
        </div>
        <div style={{ textAlign: "right" }}>
          <button
            onClick={() => {
              const result = Object.keys(this.state.selectedIndexes).filter(
                (i) => this.state.selectedIndexes[i] === true
              );
              onSubmit({
                taskType: "COREF",
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

export default CorefCaptchaTask;
