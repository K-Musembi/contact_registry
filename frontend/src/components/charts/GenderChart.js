import React, { useMemo } from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const GenderChart = ({ data }) => {
  const chartData = useMemo(() => {
    if (!data || data.length === 0) {
      return [];
    }

    const chartDataArray = [
      { name: 'Male', value: data.maleCount || 0 },
      { name: 'Female', value: data.femaleCount || 0 },
      { name: 'Not Specified', value: data.notSpecified || 0 },

    ]
    return chartDataArray.filter(entry => entry.value > 0);
  }, [data]);

  if (!data) return <p className="loading">Loading gender data...</p>;
  if (chartData.length === 0) return <p className="loading">No gender data available to display.</p>;

  return (
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          data={chartData}
          cx="50%"
          cy="50%"
          labelLine={false}
          outerRadius={100}
          fill="#8884d8"
          dataKey="value"
          nameKey="name"
          label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
        >
          {chartData.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  );
}

export default GenderChart;
