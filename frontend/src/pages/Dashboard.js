import React, { useState } from 'react';
import AccountForm from '../components/AccountForm';
import AccountBalance from '../components/AccountBalance';
import PaymentForm from '../components/PaymentForm';
import RefundForm from '../components/RefundForm';
import TransactionList from '../components/TransactionList';

export default function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [refreshFlag, setRefreshFlag] = useState(0);

  const handleAccountCreated = (account) => {
    setAccounts((prev) => [...prev, account]);
  };

  const triggerRefresh = () => setRefreshFlag((f) => f + 1);

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>PayFlow</h1>
        <p>A minimal payment gateway — idempotent transactions, double-entry ledger, rate limiting</p>
      </header>

      <div className="grid">
        <div className="column">
          <AccountForm onAccountCreated={handleAccountCreated} />
          <TransactionList accounts={accounts} />
          <AccountBalance accounts={accounts} key={refreshFlag} />
        </div>
        <div className="column">
          <PaymentForm accounts={accounts} onPaymentMade={triggerRefresh} />
          <RefundForm onRefundMade={triggerRefresh} />
        </div>
      </div>
    </div>
  );
}