import { render, screen } from '@testing-library/react';
import { WebsiteCard } from './WebsiteCard';

describe('WebsiteCard', () => {
  const props = {
    favicon: 'some-base-64-encoded-image-data',
    title: 'Example Website',
    url: 'https://www.example.com',
  };

  it('renders the website title and url', () => {
    render(<WebsiteCard {...props} />);
    expect(screen.getByText('Example Website')).toBeInTheDocument();
    expect(screen.getByText('https://www.example.com')).toBeInTheDocument();
  });

  it('renders the website favicon', () => {
    render(<WebsiteCard {...props} />);
    const image = screen.getByRole('img');
    expect(image).toBeInTheDocument();
    expect(image).toHaveAttribute('src', 'data:image/x-icon;base64,some-base-64-encoded-image-data');
  });
});
