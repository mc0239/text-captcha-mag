import React from "react";
import NerCaptchaTask from "./NerCaptchaTask";
import CorefCaptchaTask from "./CorefCaptchaTask";

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
    const { task, onChange } = this.props;

    switch (task.taskType) {
      case "NER":
        return <NerCaptchaTask task={task} onChange={onChange} />;
      case "COREF":
        return <CorefCaptchaTask task={task} onChange={onChange} />;
      default:
        // TODO
        return "Prišlo je do napake :(";
    }
  }
}

export default CaptchaTask;
