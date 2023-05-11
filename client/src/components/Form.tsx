import { useState, ChangeEvent } from 'react';

import { TextField, Button } from '@mui/material';

import './Form.css';

type FormProps = {

  // Callback function when submit button is clicked
  onSubmit: (url: string) => void;

  // Whether or not data is loading from the API call
  isDataLoading: boolean;

  // Optional error message to display
  errorMsg?: string;
};

/**
 * Component the user interacts with to submit website information requests
 */
export const Form = ({
  onSubmit,
  isDataLoading,
  errorMsg
}: FormProps) => {
  const [text, setText] = useState<string>('')

  const onInputChange = (e: ChangeEvent<{ value: string }>) => {
    setText(e.target.value);
  }

  return (
    <div className='Form'>
      <TextField
        id='outlined-basic'
        variant='outlined'
        placeholder='www.example.com'
        onChange={onInputChange}
        sx={{ width: '50vh' }}
        error={!!errorMsg}
        helperText={errorMsg}
      />
      <Button
        variant='contained'
        onClick={() => onSubmit(text)}
        disabled={isDataLoading}
        sx={{ margin: '10px 0 0 24px'}}
      >
        Submit
      </Button>
    </div>
  );
};

export default Form;
