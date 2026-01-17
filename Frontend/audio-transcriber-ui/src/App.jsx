import { useState } from "react";
import axios from "axios";

function App() {
  const [file, setFile] = useState(null);
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleUpload = async () => {
    if (!file) {
      setError("Please select an audio file.");
      return;
    }

    setLoading(true);
    setError("");
    setResponse("");

    try {
      const formData = new FormData();
      formData.append("audioFiles", file);

      const res = await axios.post(
        "http://localhost:8080/api/v1/audio/analysis/from-files",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        },
      );

      setResponse(res.data.response || "No transcription received.");
    } catch {
      setError("Failed to transcribe audio. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>Audio Transcriber</h1>
        <p className="subtitle">
          Upload an audio file and get the transcription.
        </p>

        <input
          type="file"
          accept="audio/*"
          onChange={(e) => setFile(e.target.files[0])}
        />

        <button onClick={handleUpload} disabled={loading}>
          {loading ? "Processing..." : "Upload & Transcribe"}
        </button>

        {error && <p className="error">{error}</p>}

        {response && (
          <div className="result">
            <strong>Transcription:</strong>
            <p>{response}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
