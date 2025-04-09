import React from 'react';
import '../App.css';

export default function FileList({ files }) {
  const downloadFile = (id, name) => {
    fetch(`http://localhost:8080/api/files/${id}`)
      .then((res) => res.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', name);
        document.body.appendChild(link);
        link.click();
        link.remove();
      });
  };

  return (
    <div className="file-list">
      <h2>Uploaded Files</h2>
      <ul>
        {files.map((file) => (
          <li key={file.id} className="file-item">
            <span>{file.filename}</span>
            <button
              onClick={() => downloadFile(file.id, file.filename)}
              className="download-btn"
            >
              Download
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
