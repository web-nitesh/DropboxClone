import React, { useState, useEffect } from 'react';
import FileUpload from './components/FileUpload';
import FileList from './components/FileList';
import axios from 'axios';
import './App.css';

function App() {
  const [files, setFiles] = useState([]);

  const fetchFiles = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/files');
      setFiles(response.data);
    } catch (error) {
      console.error('Failed to fetch files', error);
    }
  };

  useEffect(() => {
    fetchFiles(); // Load on first render
  }, []);

  return (
    <div className="container">
      <FileUpload onUploadSuccess={fetchFiles} />
      <FileList files={files} onDownload={fetchFiles} />
    </div>
  );
}

export default App;
