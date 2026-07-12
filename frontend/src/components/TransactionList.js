import React from 'react';

export default function TransactionList({ accounts }) {
  return (
    <div className="card">
      <h3>Accounts</h3>
      {accounts.length === 0 ? (
        <p className="hint">No accounts yet — create one above.</p>
      ) : (
        <table className="account-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((acc) => (
              <tr key={acc.id}>
                <td>#{acc.id}</td>
                <td>{acc.name}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}