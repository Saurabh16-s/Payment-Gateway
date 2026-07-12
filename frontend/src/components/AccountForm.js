import React, { useState } from 'react';
import client from '../api/client';

export default function AccountForm({ onAccountCreated }) {
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const res = await client.post('/accounts', { name });
      onAccountCreated(res.data);
      setName('');
    } catch (err) {
      setError('Failed to create account');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h3>Create Account</h3>
      <form onSubmit={handleSubmit} className="form-row">
        <input
          type="text"
          placeholder="Account name (e.g. Alice)"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Creating...' : 'Create'}
        </button>
      </form>
      {error && <p className="error-text">{error}</p>}
    </div>
  );
}