import React from 'react';

function ContactTable({ contacts, columns }) {
  if (!contacts || contacts.length === 0) {
    return <p>No contacts to display.</p>;
  }

  const defaultColumns = [
    { header: 'Name', accessor: 'name' },
    { header: 'Email', accessor: 'email' },
    { header: 'Phone', accessor: 'phone' },
    { header: 'Gender', accessor: 'gender' },
    { header: 'County', accessor: 'county' },
  ];

  const displayColumns = columns || defaultColumns;

  return (
    <table>
      <thead>
        <tr>
          {displayColumns.map(col => <th key={col.accessor}>{col.header}</th>)}
        </tr>
      </thead>
      <tbody>
        {contacts.map((contact, index) => (
          <tr key={contact.id || index}>
            {displayColumns.map(col => (
              <td key={col.accessor}>
                {col.accessor === 'createdAt'
                  ? new Date(contact[col.accessor]).toLocaleDateString()
                  : contact[col.accessor]}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default ContactTable;
