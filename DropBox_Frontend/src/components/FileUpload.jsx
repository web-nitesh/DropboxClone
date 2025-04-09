import React, { useState } from 'react';
import axios from 'axios';
import '../App.css';

export default function FileUpload({ onUploadSuccess }) {
  const [file, setFile] = useState(null);

  const handleChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert('Please select a file first.');
      return;
    }

    if (file.size === 0) {
      alert('Empty file selected. Please choose a non-empty file.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axios.post('http://localhost:8080/api/files/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      alert('File uploaded!');
      console.log('Upload response:', response.data);
      setFile(null); 
      onUploadSuccess(); 
    } catch (error) {
      alert('Upload failed');
      console.error('Upload error:', error.response?.data || error.message);
    }
  };

  return (
    <div className="upload-box">
      <h2>Upload a File</h2>
      <input type="file" onChange={handleChange} />
      <button onClick={handleUpload} className="upload-btn">
        Upload
      </button>
    </div>
  );
}
