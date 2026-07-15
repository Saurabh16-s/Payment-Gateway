import React, { useState } from 'react';
import client from '../api/client';

export default function FundForm({ accounts, onFunded }) {
  const [accountId, setAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!accountId || !amount) return;
    setLoading(true);
    try {
      const res = await client.post(`/accounts/${accountId}/fund`, { amount: Number(amount) });
      setResult(res.data);
      onFunded();
    } catch (err) {
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h3>Fund Account (test only)</h3>
      <form onSubmit={handleSubmit} className="form-row">
        <select value={accountId} onChange={(e) => setAccountId(e.target.value)}>
          <option value="">Select account</option>
          {accounts.map((acc) => (
            <option key={acc.id} value={acc.id}>{acc.name} (#{acc.id})</option>
          ))}
        </select>
        <input type="number" placeholder="Amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
        <button type="submit" disabled={loading}>{loading ? 'Funding...' : 'Fund'}</button>
      </form>
      {result && <p className="hint">New balance: ${result.balance}</p>}
    </div>
  );
}
