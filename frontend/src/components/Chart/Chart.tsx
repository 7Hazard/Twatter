
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, AreaChart, Area, BarChart, Bar } from 'recharts';
import { ChartData, ChartStyle } from '../../interfaces/Chart';

export default function ({ data }: { data: ChartData }) {
    const { style, points, keys } = data

    if(!data) return <>`Error plotting chart: ${JSON.stringify(data)}`</>

    switch (style) {
        case ChartStyle.line: return (
            <LineChart
                width={500}
                height={300}
                data={points}
                margin={{
                    top: 5,
                    right: 30,
                    left: 20,
                    bottom: 5,
                }}
            >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                {keys.map(key => (
                    <Line type="monotone" dataKey={key.name} stroke={key.stroke} activeDot={key.activeDot} fill={key.fill} />
                ))}
            </LineChart>
        )
        case ChartStyle.area: return (
            <AreaChart
                width={500}
                height={400}
                data={points}
                margin={{
                    top: 10,
                    right: 30,
                    left: 0,
                    bottom: 0,
                }}
            >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                {keys.map(key => (
                    <Area type="monotone" dataKey={key.name} stroke={key.stroke} activeDot={key.activeDot} fill={key.fill} />
                ))}
            </AreaChart>
        )
        case ChartStyle.bar: return (
            <BarChart
                width={500}
                height={300}
                data={points}
                margin={{
                    top: 5,
                    right: 30,
                    left: 20,
                    bottom: 5,
                }}
            >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                {keys.map(key => (
                    <Bar type="monotone" dataKey={key.name} stroke={key.stroke} fill={key.fill} />
                ))}
            </BarChart>
        )
        default: return <>{`Error plotting chart: ${JSON.stringify(data)}`}</>
    }
}
