import React, { useState } from 'react';
import client from '../api/client';
import { generateIdempotencyKey } from '../utils/idempotency';

export default function RefundForm({ onRefundMade }) {
  const [paymentId, setPaymentId] = useState('');
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!paymentId || !amount) return;
    setLoading(true);
    setError(null);
    try {
      const res = await client.post(
        `/payments/${paymentId}/refund`,
        { amount: Number(amount) },
        { headers: { 'Idempotency-Key': generateIdempotencyKey() } }
      );
      setResult(res.data);
      onRefundMade();
    } catch (err) {
      setError(err.response?.data?.error || 'Refund failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h3>Refund a Payment</h3>
      <form onSubmit={handleSubmit} className="form-row">
        <input
          type="number"
          placeholder="Payment ID"
          value={paymentId}
          onChange={(e) => setPaymentId(e.target.value)}
        />
        <input
          type="number"
          placeholder="Refund amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Refunding...' : 'Refund'}
        </button>
      </form>
      {error && <p className="error-text">{error}</p>}
      {result && (
        <div className="result-box">
          <p><strong>Payment #{result.id}</strong> — {result.status}</p>
        </div>
      )}
    </div>
  );
}