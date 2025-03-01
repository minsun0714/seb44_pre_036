import { QuestionInputContainer } from '../style';
import { BlueButton } from '../../../common/style';
import Editor from './Editor';

const QuestionInput = () => {
  return (
    <QuestionInputContainer>
      <h4>What are the details of your problem?</h4>
      <p>
        Introduce the problem and expand on what you put in the title. Minimum
        20 characters.
      </p>
      <Editor />
      <BlueButton>Next</BlueButton>
    </QuestionInputContainer>
  );
};

export default QuestionInput;
