import { useState } from 'react';
import Form from './components/Form';
import { WebsiteInfo } from './AppInterfaces';
import WebsiteCard from './components/WebsiteCard';
import { v4 } from 'uuid';

import './App.css';

/**
 * Landing component to the client
 *  - Manages local state of data and form control (isLoading, error message)
 *  - Handles data persistance to local storage
 *      - Data persistance is done in the client to avoid having to make a API request to the APIServer
 *        everytime the page refreshs
 *      - Ideal for low data storage needs, no need for infranstructure costs of managing a database
 */
const App = () => {
  const localStorageDataKey = 'data';
  const [dataList, setDataList] = useState<WebsiteInfo[]>(getFromLocalStorage(localStorageDataKey));
  const [isDataLoading, setIsDataLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const onSubmit = async (url: string) => {
    setIsDataLoading(true);
    setErrorMsg('');

    let res;
    try {
      res = await fetch(`http://localhost:9000/getWebsiteInfo?url=${url}`);
    } catch (error) {
      setErrorMsg('Error connecting to APIServer');
    }

    if (res?.ok) {
      const data = await res?.json();
      const newDataList = [{ ...data, url: url}, ...dataList]
      setDataList(newDataList);
      localStorage.setItem(localStorageDataKey, JSON.stringify(newDataList));
    } else if (res?.ok === false) {
      setErrorMsg('Invalid URL');
    }
    setIsDataLoading(false);
  }

  const websiteCards = dataList.map(data => <WebsiteCard favicon={data.favicon} title={data.title} url={data.url} key={v4()}/>);

  return (
    <div className='App'>
      <header className='App-header'>
      <Form onSubmit={onSubmit} isDataLoading={isDataLoading} errorMsg={errorMsg}/>
      <div className='Card-list-container'>
        { websiteCards }
      </div>
      </header>
    </div>
  );
}

const getFromLocalStorage = (key: string) => {
  return JSON.parse(localStorage.getItem(key) || '[]');
};

export default App;
