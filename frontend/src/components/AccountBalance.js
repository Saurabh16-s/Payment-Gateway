import React, { useState } from 'react';
import client from '../api/client';

export default function AccountBalance({ accounts }) {
  const [selectedId, setSelectedId] = useState('');
  const [balance, setBalance] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchBalance = async () => {
    if (!selectedId) return;
    setLoading(true);
    try {
      const res = await client.get(`/accounts/${selectedId}/balance`);
      setBalance(res.data.balance);
    } catch (err) {
      setBalance('Error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h3>Check Balance</h3>
      <div className="form-row">
        <select value={selectedId} onChange={(e) => setSelectedId(e.target.value)}>
          <option value="">Select account</option>
          {accounts.map((acc) => (
            <option key={acc.id} value={acc.id}>
              {acc.name} (#{acc.id})
            </option>
          ))}
        </select>
        <button onClick={fetchBalance} disabled={loading}>
          {loading ? 'Checking...' : 'Check'}
        </button>
      </div>
      {balance !== null && (
        <p className="balance-display">
          Balance: <strong>${balance}</strong>
        </p>
      )}
    </div>
  );
}