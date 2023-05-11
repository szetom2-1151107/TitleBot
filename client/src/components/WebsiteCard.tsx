import { Card, CardMedia, CardContent, Typography } from '@mui/material';

import './WebsiteCard.css';

type WebsiteCardProps = {

  // Icon to render in base64 encoding
  favicon: string;

  // Name of the website
  title: string;

  // url of the website
  url: string;
};

/**
 * Simple component to render how each website info card looks like.
 * This is in it's seperate component so it is easily extendable or modifable
 * based on user feedback and product team requests
 */
export const WebsiteCard = ({
  favicon,
  title,
  url
}: WebsiteCardProps) => {
  return (
    <Card variant='outlined' className='WebsiteCard'>
      <CardMedia
        component='img'
        sx={{ display: 'flex', width: '40px', height: '40px', paddingTop: '24px', paddingLeft: '12px'}}
        image={`data:image/x-icon;base64,${favicon}`}
      />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', textAlign: 'left', padding: '12px' }}>
        <Typography variant='h6'>{title}</Typography>
        <Typography variant='subtitle1' color='text.secondary'>{url}</Typography>
      </CardContent>
    </Card>
  );
};

export default WebsiteCard;
