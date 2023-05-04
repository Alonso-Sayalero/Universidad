//General config
const graph_container = d3.select('#graph');
const legend_container = d3.select('#legend');
const margin = {top: 10, right: 30, bottom: 30, left: 60}
let width = 1060 - margin.left - margin.right
let height = 500 - margin.top - margin.bottom;
const xTicks = 30;
const yTicks = 10;
const countryConfig = [{
    column: 'spain',
    color: '#cd2626',
    legendText: 'España'
},
{
    column: 'bangladesh',
    color: '#40826d',
    legendText: 'Bangladesh'
},
{
    column: 'colombia',
    color: '#e7df65',
    legendText: 'Colombia'
},
{
    column: 'egypt',
    color: '#2e0705',
    legendText: 'Egipto'
},
{
    column: 'france',
    color: '#321297',
    legendText: 'Francia'
},
{
    column: 'papua',
    color: '#16f34c',
    legendText: 'Papua Nueva Guinea'
},
{
    column: 'poland',
    color: '#e641b0',
    legendText: 'Polonia'
},
{
    column: 'portugal',
    color: '#29dbc7',
    legendText: 'Portugal'
},
{
    column: 'serbia',
    color: '#821bd0',
    legendText: 'Serbia'
},
{
    column: 'uganda',
    color: '#e05d00',
    legendText: 'Uganda'
},
{
    column: 'uk',
    color: '#ffa90f',
    legendText: 'Reino Unido'
},
{
    column: 'usa',
    color: '#1075f4',
    legendText: 'Estados Unidos'
},
{
    column: 'uruguay',
    color: '#51e3fb',
    legendText: 'Uruguay'
}];

//Axes and scales
const x = d3.scaleTime().range([0, width]);
const y = d3.scaleLinear().range([height, 0]);
const xAxis = d3.axisBottom(x).ticks(xTicks);
const yAxis = d3.axisLeft(y).ticks(yTicks);

//Date parsing
const parseDate = d3.timeParse('%Y');

//Horizontal grid lines
const gridLines = () => {
    return d3.axisLeft(y).ticks(yTicks);
}

//Colors
let all_colors = [];
countryConfig.forEach(d => {
    all_colors.push(d.color);
});
all_colors = d3.scaleOrdinal().range(all_colors);

//Line
const line = d3.line()
    .x(d => { return x(d.year); })
    .y(d => { return y(d.number); })
    .curve(d3.curveCardinal);

//Legend
const legend = legend_container.append('ul')
    .attr('class', 'legend--ul');

const key = legend.selectAll('key')
    .data(countryConfig)
    .enter()
    .append('li')
    .attr('class', 'legend--ul--li');

key.append('span')
    .attr('class', 'legend--span-line')
    .style('background-color', d => { return all_colors(d.color); });

key.insert('span')
    .attr('class', 'legend--span-label')
    .text(d => { return d.legendText });

//SVG
const svg = graph_container.append('svg')
    .attr('width', width + margin.left + margin.right)
    .attr('height', height + margin.top + margin.bottom);

const group = svg.append('g')
    .attr('transform', `translate(${margin.left}, ${margin.top})`);

//Creates the selected graph
async function draw_graph(){
    //Clean graph
    group.selectAll("g").remove();

    //Get data source
    const source = $("input[name='risk-radio']:checked").val();
    const data = await d3.csv(source);

    //List of countries to display
    const prodCountry = ['spain'];
    $("input[type='checkbox']:checked").each(function (){
        prodCountry.push($(this).val());  
    })

    data.forEach(d => {
        d.year = parseDate(d.year);
    });

    let colors = [];
    countryConfig.forEach(d => { 
        if (prodCountry.includes(d.column)){
            colors.push(d.color);
        }
    });
    colors = d3.scaleOrdinal().range(colors);

    const numbers = prodCountry.map(country => {
        return {
            category: country,
            datapoints: data.map(d => {
                return { year: d.year, number: +d[country] }
            })
        }
    });

    //Get maximum y domain
    let max = 0;
    numbers.forEach(d => {
        let aux = d.datapoints;
        aux.forEach(n => {
            let element = n.number;
            if(element > max){
                max = element;
            }
        })
    });

    //Axis domains
    x.domain(d3.extent(data, d => {return d.year;}));
    y.domain([0, max + 10000]);

    //Y axis
    group.append('g')
        .attr('class', 'y-axis')
        .call(yAxis);

    //X axis
    group.append('g')
        .attr('transform', `translate(0, ${height})`)
        .call(xAxis.tickFormat(d3.timeFormat('%Y')));

    //Grid lines
    group.append('g')
        .attr('class', 'grid')
        .attr('stroke-width', .75)
        .call(gridLines().tickSize(-width).tickFormat(''));

    //Create lines
    group.selectAll('.country')
        .data(numbers)
        .enter()
        .append('g')
        .attr('class', 'country')
        .append('path')
        .attr('class', 'line')
        .attr('d', d => { return line(d.datapoints) })
        .style('stroke', d => { return colors(d.category) })
        .style('stroke-width', '2')
        .style('fill', 'none');
}
draw_graph();