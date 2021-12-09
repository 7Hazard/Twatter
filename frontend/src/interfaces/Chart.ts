
export enum ChartStyle {
    line = "line",
    area = "area",
    bar = "bar",
}

export type ChartDataKey = {
    name: string
    stroke: string
    fill: string
    activeDot: { r: number }
}

export interface Chart {
    id: number
    data: ChartData
}

export interface ChartData {
    style: ChartStyle,
    keys: ChartDataKey[]
    points: any
}
