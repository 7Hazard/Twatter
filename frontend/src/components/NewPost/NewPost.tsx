import { useReducer, useState } from 'react'
import { useNavigate } from 'react-router'
import { authorizedPost } from '../../api'
import { ChartStyle } from '../../interfaces/Chart'
import Chart from '../Chart/Chart'
import './NewPost.scoped.css'

export default function () {
    const forceUpdate = useReducer(() => ({}), {})[1] as () => void
    const [chartInputs, setChartInputs] = useState<{ style: ChartStyle, jsonData: string }[]>([])
    const [content, setContent] = useState("")
    const navigate = useNavigate()

    let errors: string[] = []
    if(content == "") errors.push("Content is empty")
    else if(content.length > 1000) errors.push("Too many characters in content")

    let chartData = chartInputs.map((v, i) => {
        let data, dataerror
        try {
            if(v.jsonData == "")
                throw "No data"

            data = JSON.parse(v.jsonData)

            if(data.keys == undefined)
                throw "'keys' property missing"
            if(data.points == undefined)
                throw "'points' property missing"

            data.style = v.style
        } catch (error) {
            dataerror = error
            errors.push(`Chart ${i+1} error: ${error}`)
        }

        return {data, error: dataerror}
    })

    return (<>
        <div className="newpost-container">
            <div style={{display:"flex", alignItems: "center", gap: "1em", marginBottom:"1em"}}>
                <h1 style={{margin: "0"}}>New Post</h1>
                <button style={{height: "3em", flex: "1"}} disabled={errors.length > 0} onClick={e => {
                    authorizedPost("post", {
                        content,
                        charts: chartData.map(v => v.data)
                    }).then(e => {
                        navigate("/")
                    }).catch(e => {
                        alert("Could not publish post")
                        console.error(e);
                    })
                }}>Send</button>
            </div>
            
            {errors.length > 0 && <div style={{marginBottom:"1em", padding:"1em", border: "solid 2px", borderColor: "red" }}>
                {errors.map(e => <p style={{margin:"0"}}>{e}</p>)}
            </div>}

            <h3>Content</h3>
            <textarea name="content" id="content" onChange={e => setContent(e.target.value)}></textarea>

            <button onClick={e => {
                let ci = chartInputs.slice()
                ci.push({ style: ChartStyle.line, jsonData: "" })
                setChartInputs(ci)
            }}>Add chart</button>

            {chartInputs.map(({ style, jsonData }, i) => {
                let {data, error} = chartData[i]

                return (
                    <div className="chart-container" style={{border:"solid", padding:"1em"}}>
                        <div className="chart-container-row">
                            <h3>Chart {i+1}</h3>
                            <button onClick={e => {
                                chartInputs.splice(i, 1)
                                setChartInputs(chartInputs)
                                forceUpdate()
                            }}>Remove</button>
                        </div>
    
                        <div className="chart-container-row">
                            <h4>Style</h4>
                            <select onChange={e => {
                                chartInputs[i].style = e.target.value as ChartStyle
                                setChartInputs(chartInputs)
                                forceUpdate()
                            }}>
                                {Object.entries(ChartStyle).filter(([k, v]) => isNaN(Number(k))).map(([key, value]) => {
                                    let name = key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())
                                    return <option value={value} key={value}>{name}</option>
                                })}
                            </select>
                        </div>
    
                        <div>
                            <h4>Data</h4>
                            <textarea name="chart-data" id="chart-data" onChange={e => {
                                chartInputs[i].jsonData = e.target.value
                                setChartInputs(chartInputs)
                                forceUpdate()
                            }}></textarea>
                        </div>
    
                        <div>
                            <h4>Preview</h4>
                            <div className="chart-preview">
                                {jsonData == "" ? "No data" : (error ? `${error}` : <Chart data={data} />)}
                            </div>
                        </div>
                    </div>
                )
            })}
        </div>
    </>)
}

