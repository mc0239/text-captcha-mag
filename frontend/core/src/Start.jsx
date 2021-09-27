import React from "react";

class Start extends React.Component {
  constructor(props) {
    super(props);

    this.state = {};
  }

  renderContent = () => {
    const { content } = this.props;

    if (!content) {
      return null;
    }

    return <div style={{ marginBottom: "2px" }}>{content}</div>;
  };

  renderButtons = () => {
    const { buttonText, onStart, shouldRenderButtons } = this.props;

    if (shouldRenderButtons === false) {
      return null;
    }

    return (
      <>
        <button onClick={() => onStart("NER")}>{buttonText} (NER)</button>
        <button onClick={() => onStart("COREF")}>{buttonText} (CoRef)</button>
      </>
    );
  };

  render() {
    return (
      <>
        {this.renderContent()}
        {this.renderButtons()}
      </>
    );
  }
}

export default Start;
