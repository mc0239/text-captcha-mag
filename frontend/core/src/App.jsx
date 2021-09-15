import React from "react";
import ApiClient from "./ApiClient";
import style from "./App.scss";
import CaptchaTask from "./task/CaptchaTask";
import Start from "./Start";

function getArticleUrl() {
  return window.location.hostname + window.location.pathname;
}

export const AppState = {
  INGEST_LOADING: 1,
  INGEST_DONE: 2,
  INGEST_ERROR: 3,

  TASK_LOADING: 4,
  TASK_SHOW: 5,
  TASK_SUBMITTING: 6,
  TASK_DONE: 7,
  TASK_ERROR: 8,
};

class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      currentState: AppState.INGEST_LOADING,

      ingestData: null,
      flowState: null,
    };
  }

  componentDidMount() {
    this.makeIngestRequest();
  }

  makeIngestRequest = async () => {
    try {
      const data = await ApiClient.ingest(
        getArticleUrl(),
        this.props.getText()
      );
      this.setState({
        currentState: AppState.INGEST_DONE,
        ingestData: data,
      });
    } catch (e) {
      console.error(e);
      this.setState({ currentState: AppState.INGEST_ERROR });
    }
  };

  makeTaskRequest = async (taskType) => {
    this.setState({ currentState: AppState.TASK_LOADING });
    try {
      const data = await ApiClient.flow.begin({
        ...this.state.ingestData,
        taskType: taskType,
      });
      this.setState({
        currentState: AppState.TASK_SHOW,
        flowState: data,
      });
    } catch (e) {
      console.error(e);
      this.setState({ currentState: AppState.TASK_ERROR });
    }
  };

  makeTaskResponse = async (content) => {
    this.setState({ currentState: AppState.TASK_SUBMITTING });
    try {
      const data = await ApiClient.flow["continue"]({
        id: this.state.flowState.task.id,
        ...content,
      });
      console.log(data);
      if (data?.flowComplete === true) {
        this.setState({
          currentState: AppState.TASK_DONE,
          flowState: data,
        });
        this.props.flowComplete(data.flowId);
      } else {
        this.setState({
          currentState: AppState.TASK_SHOW,
          flowState: data,
        });
      }
    } catch (e) {
      console.error(e);
      this.setState({ currentState: AppState.TASK_ERROR });
    }
  };

  renderContent = () => {
    const { currentState } = this.state;

    switch (currentState) {
      case AppState.INGEST_LOADING:
        return "Poteka obdelava teksa in priprava nalog...";

      case AppState.INGEST_DONE:
        return (
          <>
            <Start
              buttonText="Reši nalogo"
              onStart={(taskType) => {
                this.makeTaskRequest(taskType);
              }}
            />
          </>
        );

      case AppState.INGEST_ERROR:
        return "Prišlo je do napake.";

      case AppState.TASK_LOADING:
      case AppState.TASK_SHOW:
      case AppState.TASK_SUBMITTING:
        return (
          <CaptchaTask
            task={this.state.flowState?.task}
            isLoading={
              this.state.currentState === AppState.TASK_LOADING ||
              this.state.currentState === AppState.TASK_SUBMITTING
            }
            onSubmit={(content) => {
              this.makeTaskResponse(content);
            }}
          />
        );

      case AppState.TASK_DONE:
        return (
          <Start
            buttonText="Reši novo nalogo"
            onStart={(taskType) => {
              this.makeTaskRequest(taskType);
            }}
            content={"Naloga uspešno zaključena."}
          />
        );

      case AppState.TASK_ERROR:
        return (
          <Start
            buttonText="Reši novo nalogo"
            onStart={(taskType) => {
              this.makeTaskRequest(taskType);
            }}
            content={"Prišlo je do napake."}
          />
        );

      default:
        console.warn(
          "Trying to render content for undefined state: ",
          currentState
        );
        return null;
    }
  };

  render() {
    return (
      <div className={style["tc-main"]}>
        <div className={style["tc-content"]}>{this.renderContent()}</div>
        <div className={style["tc-footer"]}>TextCAPTCHA 0.1</div>
      </div>
    );
  }
}

export default App;
