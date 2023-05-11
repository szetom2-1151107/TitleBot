import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';

describe('App', () => {
  beforeEach(() => {
    localStorage.clear();
    jest.spyOn(global, 'fetch').mockImplementation(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ favicon: 'favicon', title: 'title' }),
      })
    );
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('should render the form and website cards', async () => {
    render(<App />);

    // Assert that the form and website cards are rendered
    expect(screen.getByPlaceholderText(/example.com/i)).toBeDefined();
    expect(screen.getByRole('button')).toBeInTheDocument();

    fireEvent.click(screen.getByRole('button'));

    await waitFor(() => {
      expect(screen.getByText(/title/i)).toBeInTheDocument();
    });
  });

  it('should add a new website card when the form is submitted with a valid URL', async () => {
    render(<App />);

    // Fill out the form and submit it
    fireEvent.change(screen.getByPlaceholderText(/example.com/i), { target: { value: 'https://cnn.com' } });
    fireEvent.click(screen.getByRole('button'));

    await waitFor(() => {
      expect(screen.getByText('https://cnn.com')).toBeInTheDocument();
    });

    // Assert that the new website card is rendered
    expect(screen.getByText('title')).toBeInTheDocument();
  });

  it('should display an error message when the form is submitted with an invalid URL', async () => {
    jest.spyOn(global, 'fetch').mockImplementation(() =>
      Promise.resolve({
        ok: false,
      })
    );

    render(<App />);

    // Fill out the form and submit it with an invalid URL
    fireEvent.change(screen.getByPlaceholderText(/example.com/i), { target: { value: 'not-a-url' } });
    fireEvent.click(screen.getByRole('button'));

    // Assert that the error message is displayed
    await waitFor(() => {
      expect(screen.getByText('Invalid URL')).toBeInTheDocument();
    });
  });

  it('should display an error message when there is an error connecting to the API server', async () => {
    // Mock the fetch function to simulate an error
    jest.spyOn(global, 'fetch').mockImplementation(() => Promise.reject(new Error('API server error')));

    render(<App />);

    // Fill out the form and submit it
    fireEvent.change(screen.getByPlaceholderText(/example.com/i), { target: { value: 'https://example.com' } });
    fireEvent.click(screen.getByRole('button'));

    // Assert that the error message is displayed
    await waitFor(() => {
      expect(screen.getByText('Error connecting to APIServer')).toBeInTheDocument();
    });
  });
});
