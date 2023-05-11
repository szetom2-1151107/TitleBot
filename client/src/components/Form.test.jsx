import { fireEvent, render, screen } from '@testing-library/react';
import Form from './Form';

describe('Form', () => {
  const mockOnSubmit = jest.fn();
  const mockErrorMsg = 'Invalid URL';

  beforeEach(() => {
    mockOnSubmit.mockClear();
  });

  it('should render input field and submit button', () => {
    render(<Form onSubmit={mockOnSubmit} isDataLoading={false} errorMsg={''} />);

    expect(screen.getByPlaceholderText('www.example.com')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Submit' })).toBeInTheDocument();
  });

  it('should call onSubmit when submit button is clicked', () => {
    render(<Form onSubmit={mockOnSubmit} isDataLoading={false} errorMsg={''} />);

    const input = screen.getByPlaceholderText('www.example.com');
    const submitBtn = screen.getByRole('button', { name: 'Submit' });

    fireEvent.change(input, { target: { value: 'http://example.com' } });
    fireEvent.click(submitBtn);

    expect(mockOnSubmit).toHaveBeenCalledWith('http://example.com');
  });

  it('should disable submit button when data is loading', () => {
    render(<Form onSubmit={mockOnSubmit} isDataLoading={true} errorMsg={''} />);

    const submitBtn = screen.getByRole('button', { name: 'Submit' });

    expect(submitBtn).toBeDisabled();
  });

  it('should show error message when errorMsg prop is passed', () => {
    render(<Form onSubmit={mockOnSubmit} isDataLoading={false} errorMsg={mockErrorMsg} />);

    expect(screen.getByText(mockErrorMsg)).toBeInTheDocument();
  });
});