import {Component, SimpleChanges, OnChanges} from '@angular/core';
import {Counts} from "../../models/counts.model";

declare var jQuery: any;

@Component({
    selector: 'chart',
    inputs: ['counts'],
    template: `<div class="ui segment" id="graph" style="margin-top: 2em"></div>`
})
export class GraphComponent implements OnChanges {

    counts: Counts[];

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['counts'] && this.counts) {
            let dates: string[] = [];
            let media: number[] = [];
            let followers: number[] = [];
            let following: number[] = [];
            let lastDate: string;
            console.log(this.counts);
            this.counts.forEach(c => {
                let date = `${c.createdAt.getDay()}/${c.createdAt.getMonth()}/${c.createdAt.getFullYear()}`;
                if (lastDate === date) {
                    dates.pop();
                    media.pop();
                    followers.pop();
                    following.pop();
                }
                dates.push(date);
                media.push(c.numberOfMedia);
                followers.push(c.numberOfFollowers);
                following.push(c.numberOfFollowing);
                lastDate = date;
            });
            this.renderChart(dates, media, followers, following);
        }
    }

    renderChart(dates: string[], media: number[], followers: number[], following: number[]) {
        console.log(dates, media, followers, following);
        jQuery('#graph').highcharts({
            chart: {
                type: 'area'
            },
            title: {
                text: 'Latest Statistics'
            },
            series: [
                {
                    name: 'Media',
                    type: 'line',
                    yAxis: 0,
                    color: '#ffc838',
                    data: media
                },
                {
                    name: 'Followers',
                    type: 'spline',
                    yAxis: 1,
                    color: '#6dc993',
                    data: followers
                },
                {
                    name: 'Following',
                    type: 'spline',
                    yAxis: 2,
                    color: '#458eff',
                    data: following
                }
            ],
            yAxis: [
                {
                    title: {text: ""},
                    labels: {
                        style: {color: '#ffc838'}
                    },
                    opposite: true,
                    allowDecimals: false,
                    startOnTick: true,
                    endOnTick: true
                },
                {
                    title: {text: ""},
                    labels: {
                        style: {color: '#6dc993'}
                    },
                    allowDecimals: false,
                    startOnTick: true,
                    endOnTick: true
                },
                {
                    title: {text: ""},
                    labels: {
                        style: {color: '#458eff'}
                    },
                    allowDecimals: false,
                    startOnTick: true,
                    endOnTick: true
                }
            ],
            xAxis: [
                {
                    categories: dates
                }
            ],
            tooltip: {
                pointFormat: '<b>{series.name}</b>: {point.y}'
            },
            plotOptions: {
                area: {
                    pointStart: 1940,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
        });

    }
}