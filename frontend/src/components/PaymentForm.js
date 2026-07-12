import React, { useState } from 'react';
import client from '../api/client';
import { generateIdempotencyKey } from '../utils/idempotency';

export default function PaymentForm({ accounts, onPaymentMade }) {
  const [payerId, setPayerId] = useState('');
  const [payeeId, setPayeeId] = useState('');
  const [amount, setAmount] = useState('');
  const [idempotencyKey, setIdempotencyKey] = useState(generateIdempotencyKey());
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const submitPayment = async (reuseKey = false) => {
    if (!payerId || !payeeId || !amount) return;
    setLoading(true);
    setError(null);
    const keyToUse = reuseKey ? idempotencyKey : idempotencyKey;
    try {
      const res = await client.post(
        '/payments',
        { payerId: Number(payerId), payeeId: Number(payeeId), amount: Number(amount) },
        { headers: { 'Idempotency-Key': keyToUse } }
      );
      setResult(res.data);
      onPaymentMade();
    } catch (err) {
      setError(err.response?.data?.error || 'Payment failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    submitPayment(false);
  };

  const handleRetrySameKey = () => {
    submitPayment(true);
  };

  const handleReset = () => {
    setIdempotencyKey(generateIdempotencyKey());
    setResult(null);
    setError(null);
  };

  return (
    <div className="card">
      <h3>Make a Payment</h3>
      <form onSubmit={handleSubmit} className="form-stack">
        <select value={payerId} onChange={(e) => setPayerId(e.target.value)}>
          <option value="">From (payer)</option>
          {accounts.map((acc) => (
            <option key={acc.id} value={acc.id}>{acc.name} (#{acc.id})</option>
          ))}
        </select>
        <select value={payeeId} onChange={(e) => setPayeeId(e.target.value)}>
          <option value="">To (payee)</option>
          {accounts.map((acc) => (
            <option key={acc.id} value={acc.id}>{acc.name} (#{acc.id})</option>
          ))}
        </select>
        <input
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <div className="idempotency-box">
          <span className="label">Idempotency-Key</span>
          <code>{idempotencyKey}</code>
        </div>

        <div className="button-row">
          <button type="submit" disabled={loading}>
            {loading ? 'Processing...' : 'Send Payment'}
          </button>
          <button type="button" onClick={handleRetrySameKey} disabled={loading} className="secondary">
            Retry (same key)
          </button>
          <button type="button" onClick={handleReset} className="ghost">
            New Key
          </button>
        </div>
      </form>

      {error && <p className="error-text">{error}</p>}
      {result && (
        <div className="result-box">
          <p><strong>Payment #{result.id}</strong> — {result.status}</p>
          <p>${result.amount} sent</p>
        </div>
      )}
      <p className="hint">
        Tip: click "Retry (same key)" to prove the payment isn't double-processed —
        the server returns the cached response instead of charging again.
      </p>
    </div>
  );
}