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
      compState: null,

      taskStates: null,
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
      const data = await ApiClient.comp.start({
        ...this.state.ingestData,
        taskType: taskType,
      });
      this.setState({
        currentState: AppState.TASK_SHOW,
        compState: data,
        taskStates: data.tasks.map(() => []),
      });
    } catch (e) {
      console.error(e);
      this.setState({ currentState: AppState.TASK_ERROR });
    }
  };

  makeTaskResponse = async (taskSolutions) => {
    this.setState({ currentState: AppState.TASK_SUBMITTING });
    try {
      const data = await ApiClient.comp.solve({
        taskSolutions: taskSolutions,
      });
      console.log(data);
      if (data?.complete === true) {
        this.setState({
          currentState: AppState.TASK_DONE,
          compState: data,
        });
        this.props.onComplete(data.linkId);
      } else {
        this.setState({
          currentState: AppState.TASK_SHOW,
          compState: data,
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

      case AppState.TASK_SHOW:
        return (
          <>
            {this.state.compState?.tasks.map((task, index) => {
              return (
                <CaptchaTask
                  key={index}
                  task={task}
                  onChange={(taskState) => {
                    this.setState((prevState) => {
                      return {
                        taskStates: {
                          ...prevState.taskStates,
                          [index]: taskState,
                        },
                      };
                    });
                  }}
                />
              );
            })}
            <div style={{ textAlign: "right" }}>
              <button
                onClick={() => {
                  this.makeTaskResponse([
                    {
                      taskType: this.state.compState?.tasks[0].taskType,
                      id: this.state.compState?.tasks[0].id,
                      content: this.state.taskStates[0],
                    },
                    {
                      taskType: this.state.compState?.tasks[1].taskType,
                      id: this.state.compState?.tasks[1].id,
                      content: this.state.taskStates[1],
                    },
                  ]);
                }}
              >
                Pošlji
              </button>
            </div>
          </>
        );
      case AppState.TASK_SUBMITTING:
      case AppState.TASK_LOADING:
        return "Nalaganje...";

      case AppState.TASK_DONE:
        return (
          <Start
            shouldRenderButtons={false}
            content={"✅ CAPTCHA uspešno zaključena."}
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
        <div className={style["tc-footer"]}>TextCAPTCHA 0.2</div>
      </div>
    );
  }
}

export default App;
